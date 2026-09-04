package com.example.blogapi.event;


import com.example.blogapi.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostEventProducer {

    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "post_created";

    public void sendPostCreatedEvent(PostCreatedEvent event){
        kafkaTemplate.send(
                TOPIC,
                event.postId().toString(),
                event
        );
    }
}
