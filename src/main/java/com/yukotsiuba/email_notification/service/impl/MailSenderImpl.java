package com.yukotsiuba.email_notification.service.impl;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;
import com.yukotsiuba.email_notification.repository.EmailMessageRepository;
import com.yukotsiuba.email_notification.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {

    private final JavaMailSender mailSender;

    private final EmailMessageRepository emailMessageRepository;

    @Override
    public void sendEmail(EmailMessageDto emailMessageDto) {
        log.info("Sending email message {}", emailMessageDto);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessageDto.getTo());
        message.setSubject(emailMessageDto.getSubject());
        message.setText(emailMessageDto.getText());
        message.setFrom("noreply@example.com");

        try {
            mailSender.send(message);
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(emailMessageDto.getTo())
                    .content(emailMessageDto.getText())
                    .subject(emailMessageDto.getSubject())
                    .attempts(1)
                    .status(EmailStatus.SENT)
                    .lastSentTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                    .build();
            emailMessageRepository.save(emailMessage);
        } catch (Exception ex) {
            log.error("Could not send email: {}", ex.getMessage());
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(emailMessageDto.getTo())
                    .content(emailMessageDto.getText())
                    .subject(emailMessageDto.getSubject())
                    .attempts(1)
                    .status(EmailStatus.ERROR)
                    .lastSentTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                    .build();
            emailMessageRepository.save(emailMessage);
        }
    }

    @Override
    public void sendFailedEmails() {
        List<EmailMessage> failedEmails = emailMessageRepository.findByStatus(EmailStatus.ERROR);

        for (EmailMessage email : failedEmails) {
            try {
                sendEmail(EmailMessageDto.builder()
                        .subject(email.getSubject())
                        .to(email.getTo())
                        .text(email.getContent())
                        .build());
                email.setStatus(EmailStatus.SENT);
                email.setErrorMessage(null);
                email.setAttempts(email.getAttempts() + 1);
            } catch (MailException e) {
                email.setErrorMessage(e.getMessage());
                email.setAttempts(email.getAttempts() + 1);
                email.setLastSentTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            }
            emailMessageRepository.save(email);
        }
    }
}
