package com.yukotsiuba.email_notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestEmailNotificationApplication.class})
class EmailNotificationApplicationTests {

	@Test
	void contextLoads() {
	}

}
