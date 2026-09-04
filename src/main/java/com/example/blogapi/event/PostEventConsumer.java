package com.example.blogapi.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostEventConsumer {

    @KafkaListener(
            topics = "post-created",
            groupId = "blog-api"
    )
    public void consumer(PostCreatedEvent event){
        log.info(
                "Received PostCreatedEvent: postId={}, title={}, authorId={}, authorUsername={}",
                event.postId(),
                event.title(),
                event.authorId(),
                event.authorUsername()
        );
    }
}
