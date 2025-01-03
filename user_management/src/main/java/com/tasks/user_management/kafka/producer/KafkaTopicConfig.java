package com.tasks.user_management.kafka.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    @Bean
    KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(TopicsNames.USER_CREATED.getTopicName()).build(),
                TopicBuilder.name(TopicsNames.USER_UPDATED.getTopicName()).build(),
                TopicBuilder.name(TopicsNames.USER_DELETED.getTopicName()).build(),
                TopicBuilder.name(TopicsNames.USER_AUTHENTICATED.getTopicName()).build(),
                TopicBuilder.name(TopicsNames.USER_ROLE_UPDATED.getTopicName()).build(),
                TopicBuilder.name(TopicsNames.USER_LOGGED_OUT.getTopicName()).build()
        );
    }
}
