package com.yukotsiuba.email_notification.service.impl;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;
import com.yukotsiuba.email_notification.mapper.EmailMessageMapper;
import com.yukotsiuba.email_notification.repository.EmailMessageRepository;
import com.yukotsiuba.email_notification.service.EmailMessageService;
import com.yukotsiuba.email_notification.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageServiceImpl implements EmailMessageService {

    private final EmailMessageRepository emailRepository;

    private final MailSender mailSender;

    @Override
    public void handleMessageReceived(EmailMessageDto message) {
        EmailMessage emailMessage = EmailMessageMapper.toEntity(message);
        EmailMessage sentEmail = sendEmail(emailMessage);
        emailRepository.save(sentEmail);
    }

    private EmailMessage sendEmail(EmailMessage emailMessage) {
        try {
            EmailMessageDto messageDto = EmailMessageMapper.toDto(emailMessage);
            log.info("Sending email message {}", messageDto);
            mailSender.sendEmail(messageDto);
            updateEmailStatus(emailMessage, EmailStatus.SENT, null);
            return emailMessage;
        } catch (MailException ex) {
            log.error("Could not send email: {}", ex.getMessage());
            updateEmailStatus(emailMessage, EmailStatus.ERROR, ex.getMessage());
            return emailMessage;
        }
    }

    private void updateEmailStatus(EmailMessage emailMessage, EmailStatus status, String errorMessage) {
        emailMessage.setStatus(status);
        emailMessage.setLastSentTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        emailMessage.increaseAttempts();
        emailMessage.setErrorMessage(errorMessage);
    }

    @Override
    public void sendFailedEmails() {
        List<EmailMessage> failedEmails = emailRepository.findByStatus(EmailStatus.ERROR);
        for (EmailMessage email : failedEmails) {
            EmailMessage sentEmail = sendEmail(email);
            emailRepository.save(sentEmail);
        }
    }
}
