package com.yukotsiuba.email_notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import org.apache.kafka.common.serialization.Serializer;

public class EmailMessageSerializer implements Serializer<EmailMessageDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, EmailMessageDto data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing EmailMessageDto", e);
        }
    }
}
