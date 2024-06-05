package com.yukotsiuba.email_notification.service;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;

public interface MailSender {
    void sendEmail(EmailMessageDto emailMessageDto);

    void sendFailedEmails();
}
