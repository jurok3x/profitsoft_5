package com.yukotsiuba.email_notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import org.apache.kafka.common.serialization.Deserializer;

public class EmailMessageDeserializer implements Deserializer<EmailMessageDto> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public EmailMessageDto deserialize(String topic, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, EmailMessageDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
