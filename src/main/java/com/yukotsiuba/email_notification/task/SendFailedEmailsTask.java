package com.yukotsiuba.email_notification.task;

import com.yukotsiuba.email_notification.service.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendFailedEmailsTask {

    private final MailSender mailSender;

    @Scheduled(fixedRate = 300000)
    public void retryFailedEmails() {
        mailSender.sendFailedEmails();
    }

}
