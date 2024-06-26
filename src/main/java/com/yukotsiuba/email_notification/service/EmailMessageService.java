package com.yukotsiuba.email_notification.service;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;

public interface EmailMessageService {

    void handleMessageReceived(EmailMessageDto message);

    void sendFailedEmails();
}
