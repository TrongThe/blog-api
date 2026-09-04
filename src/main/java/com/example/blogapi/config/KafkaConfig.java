package com.example.blogapi.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic postCreatedTopic(){
        return new NewTopic(
                "post-created",
                3,
                (short) 1
        );
    }
}
