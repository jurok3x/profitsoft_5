package com.yukotsiuba.email_notification;

import com.icegreen.greenmail.util.GreenMail;
import com.yukotsiuba.email_notification.config.KafkaProducerTestConfig;
import com.yukotsiuba.email_notification.config.KafkaTopicTestConfig;
import com.yukotsiuba.email_notification.config.TestEmailConfig;
import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;
import com.yukotsiuba.email_notification.repository.EmailMessageRepository;
import com.yukotsiuba.email_notification.service.EmailMessageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@Import({TestEmailNotificationApplication.class, KafkaProducerTestConfig.class, KafkaTopicTestConfig.class, TestEmailConfig.class})
public class TestMain {

    @Value("${kafka.topic.article}")
    private String articleTopic;

    @Autowired
    KafkaOperations<String, EmailMessageDto> kafkaOperations;

    @Autowired
    EmailMessageRepository emailMessageRepository;

    @Autowired
    private GreenMail greenMail;

    @SpyBean
    private EmailMessageService service;

    @BeforeEach
    public void setup() {
        greenMail.start();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    @Test
    void integrationTest() throws Exception {
        EmailMessageDto messageDto = prepareMessage();
        kafkaOperations.send(articleTopic, messageDto);
        verify(service, after(5000)).handleMessageReceived(any());

        //Verify that mail is sent
        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertTrue(receivedMessages.length == 1);
        MimeMessage receivedMessage = receivedMessages[0];
        assertEquals(messageDto.getContent(), receivedMessage.getContent().toString());
        assertEquals(messageDto.getSubject(), receivedMessage.getSubject());

        //Verify that receivedMessage stored in ES
        List<EmailMessage> messages = emailMessageRepository.findByStatus(EmailStatus.SENT);
        assertFalse(messages.isEmpty());
    }

    private EmailMessageDto prepareMessage() {
        return EmailMessageDto.builder()
                .content("Congratulations you have published article Top IT News New.")
                .to("test@example.com")
                .subject("New Article")
                .build();
    }
}
