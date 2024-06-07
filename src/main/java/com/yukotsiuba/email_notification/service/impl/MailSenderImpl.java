package com.yukotsiuba.email_notification.service.impl;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.mapper.EmailMessageMapper;
import com.yukotsiuba.email_notification.service.EmailMessageService;
import com.yukotsiuba.email_notification.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailMessageDto emailMessageDto) {
        log.info("Sending email message {}", emailMessageDto);
        SimpleMailMessage message = EmailMessageMapper.toSimpleMailMessage(emailMessageDto);
        mailSender.send(message);
    }
}
