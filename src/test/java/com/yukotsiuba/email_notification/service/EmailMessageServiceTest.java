package com.yukotsiuba.email_notification.service;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;
import com.yukotsiuba.email_notification.repository.EmailMessageRepository;
import com.yukotsiuba.email_notification.service.impl.EmailMessageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class EmailMessageServiceTest {

    private EmailMessageRepository emailRepository;

    private MailSender mailSender;

    private EmailMessageService emailMessageService;

    @BeforeEach
    void setUp() {
        emailRepository = mock(EmailMessageRepository.class);
        mailSender = mock(MailSender.class);
        emailMessageService = new EmailMessageServiceImpl(emailRepository, mailSender);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(emailRepository);
    }

    @Test
    void testSendMessage() {
        EmailMessageDto messageDto = prepareMessageDto();
        EmailMessage message = prepareMessage();
        when(emailRepository.save(any(EmailMessage.class))).thenReturn(message);
        doNothing().when(mailSender).sendEmail(any(EmailMessageDto.class));

        emailMessageService.handleMessageReceived(messageDto);

        verify(mailSender).sendEmail(any(EmailMessageDto.class));
        verify(emailRepository).save(argThat(argument -> argument.getStatus().equals(EmailStatus.SENT)));
    }

    @Test
    void testErrorSendingMessage() {
        EmailMessageDto messageDto = prepareMessageDto();
        EmailMessage message = prepareMessage();
        when(emailRepository.save(any(EmailMessage.class))).thenReturn(message);
        doThrow(new MailException("Mail sending failed") {}).when(mailSender).sendEmail(any(EmailMessageDto.class));

        emailMessageService.handleMessageReceived(messageDto);

        verify(mailSender).sendEmail(any(EmailMessageDto.class));
        verify(emailRepository).save(argThat(argument -> argument.getStatus().equals(EmailStatus.ERROR)));
    }

    @Test
    void testSendErrorMessages() {
        List<EmailMessage> messages = prepareUnsentEmails();
        when(emailRepository.findByStatus(any())).thenReturn(messages);
        doNothing().when(mailSender).sendEmail(any(EmailMessageDto.class));

        emailMessageService.sendFailedEmails();

        verify(emailRepository).findByStatus(any());
        verify(emailRepository, times(messages.size())).save(any());
        verify(mailSender, times(messages.size())).sendEmail(any(EmailMessageDto.class));
    }

    private EmailMessageDto prepareMessageDto() {
        return EmailMessageDto.builder()
                .content("Congratulations you have published article Top IT News.")
                .to("test@example.com")
                .subject("New Article")
                .build();
    }

    private EmailMessage prepareMessage() {
        return EmailMessage.builder()
                .content("Congratulations you have published article Top IT News.")
                .to("test@example.com")
                .subject("New Article")
                .status(EmailStatus.SENT)
                .build();
    }

    private List<EmailMessage> prepareUnsentEmails() {
        List<EmailMessage> messages = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            EmailMessage message = prepareMessage();
            message.setStatus(EmailStatus.ERROR);
            messages.add(message);
        }

        return messages;
    }
}