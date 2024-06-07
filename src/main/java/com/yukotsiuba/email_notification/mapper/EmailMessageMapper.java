package com.yukotsiuba.email_notification.mapper;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class EmailMessageMapper {

    @Value(value = "${service.email}")
    private static String emailAddress;

    private EmailMessageMapper() {}

    public static SimpleMailMessage toSimpleMailMessage (EmailMessageDto messageDto) {
        if (messageDto == null) {
            return null;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(messageDto.getTo());
        message.setSubject(messageDto.getSubject());
        message.setText(messageDto.getText());
        message.setFrom(emailAddress);

        return message;
    }

    public static EmailMessageDto toDto(EmailMessage message) {
        if (message == null) {
            return null;
        }

        return EmailMessageDto.builder()
                .subject(message.getSubject())
                .to(message.getTo())
                .text(message.getContent())
                .build();
    }

    public static EmailMessage toEntity(EmailMessageDto messageDto) {
        if (messageDto == null) {
            return null;
        }

        return EmailMessage.builder()
                .to(messageDto.getTo())
                .content(messageDto.getText())
                .subject(messageDto.getSubject())
                .build();
    }
}
