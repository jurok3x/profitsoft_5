package com.yukotsiuba.email_notification.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;
import org.testcontainers.containers.KafkaContainer;

import java.util.Map;

@TestConfiguration
@Profile("test")
public class KafkaTopicTestConfig {

    @Value("${kafka.topic.article}")
    private String articleTopic;

    @Autowired
    private KafkaContainer kafkaContainer;

    @Bean
    public KafkaAdmin kafkaTestAdmin() {
        Map<String, Object> configs = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic articleReceivedTopicTest() {
        return new NewTopic(articleTopic, 2, (short) 1);
    }

}
