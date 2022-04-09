package com.tistory.jaimemin.studyrecruitment.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author jaime
 * @title ConsoleEmailService
 * @see\n <pre>
 * </pre>
 * @since 2022-04-09
 */
@Slf4j
@Profile("local")
@Component
public class ConsoleEmailService implements EmailService {

    @Override
    public void send(EmailMessage emailMessage) {
      log.info("sent email: {}", emailMessage.getMessage());
    }
}
