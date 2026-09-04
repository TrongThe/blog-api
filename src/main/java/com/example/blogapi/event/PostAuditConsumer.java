package com.example.blogapi.event;


import com.example.blogapi.entity.ActivityLog;
import com.example.blogapi.repository.ActivityLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostAuditConsumer {

    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 1000)
    )
    @KafkaListener(
            topics = "post-created",
            groupId = "blog-audit"
    )
    public void consume(String payload) {

        try {
            PostCreatedEvent event = objectMapper.readValue(payload, PostCreatedEvent.class);

            if (activityLogRepository.existsByPostIdAndAction(event.postId(), "POST_CREATED")){

                log.info("Activity log already exists for postId={}", event.postId());

                return;
            }

            ActivityLog activityLog = ActivityLog.builder()
                    .userId(event.authorId())
                    .postId(event.postId())
                    .action("POST_CREATED")
                    .build();

            activityLogRepository.save(activityLog);

            log.info(
                    "AUDIT: User '{}' created post #{}",
                    event.authorUsername(),
                    event.postId()
            );
        } catch (JsonProcessingException e){
            log.error("Failed to deserialize PostCreatedEvent", e);
        }
    }

    @DltHandler
    public void handleDlt(String payload){
        log.error("Message moved to DLT: {}", payload);
    }
}
