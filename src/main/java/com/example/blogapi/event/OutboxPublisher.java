package com.example.blogapi.event;


import com.example.blogapi.entity.OutboxEvent;
import com.example.blogapi.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishEvent(){

        List<OutboxEvent> events = outboxEventRepository
                .findTop100ByPublishedFalseOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {

            try {

                kafkaTemplate.send(
                        getTopic(event.getEventType()),
                        event.getId().toString(),
                        event.getPayload()
                ).get();

                event.setPublished(true);
                outboxEventRepository.save(event);

                log.info(
                        "Published outbox event id={} to Kafka",
                        event.getId()
                );
            } catch (InterruptedException e){

                Thread.currentThread().interrupt();

                log.error(
                        "Thread interrupted while publishing event id={}",
                        event.getId(),
                        e
                );

            } catch (ExecutionException e){

                log.error(
                        "Failed to publish outbox event id={}",
                        event.getId(),
                        e
                );
            }
        }
    }

    private String getTopic(String eventType){
        return switch (eventType) {
            case "POST_CREATED" -> "post-created";
            default -> throw new IllegalArgumentException(
                    "Unknown event type: " + eventType
            );
        };
    }
}
