package com.yukotsiuba.email_notification.listener;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleListener {

    private final MailSender mailSender;

    @KafkaListener(topics = "${kafka.topic.article}")
    public void articleCreated(EmailMessageDto emailMessageDto) {
        log.info("Received email message {}", emailMessageDto);
        mailSender.sendEmail(emailMessageDto);
    }
}
