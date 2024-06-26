package com.yukotsiuba.email_notification.config;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.testcontainers.containers.KafkaContainer;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@Profile("test")
public class KafkaProducerTestConfig {

    @Value("${kafka.topic.article}")
    private String articleTopic;

    @Autowired
    private KafkaContainer kafkaContainer;

    @Bean
    public ProducerFactory<String, EmailMessageDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaContainer.getBootstrapServers());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                EmailMessageSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EmailMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
