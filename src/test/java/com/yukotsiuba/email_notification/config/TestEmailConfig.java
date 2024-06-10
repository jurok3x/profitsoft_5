package com.yukotsiuba.email_notification.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestEmailConfig {

    @Bean
    public GreenMail greenMail() {
        return new GreenMail(new ServerSetup(3025, null, "smtp"));
    }
}
