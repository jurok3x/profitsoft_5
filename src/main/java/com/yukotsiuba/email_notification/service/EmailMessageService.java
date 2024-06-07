package com.yukotsiuba.email_notification.service;

import com.yukotsiuba.email_notification.dto.EmailMessageDto;
import com.yukotsiuba.email_notification.entity.EmailMessage;
import com.yukotsiuba.email_notification.entity.EmailStatus;

import java.util.List;

public interface EmailMessageService {

    void handleMessageReceived(EmailMessageDto message);

    void sendFailedEmails();
}
