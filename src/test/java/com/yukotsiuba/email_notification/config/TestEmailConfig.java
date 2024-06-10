package com.yukotsiuba.email_notification.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestEmailConfig {

    @Value("${spring.mail.port}")
    private Integer port;


    @Bean
    public GreenMail greenMail() {
        return new GreenMail(new ServerSetup(port, null, "smtp"));
    }
}
