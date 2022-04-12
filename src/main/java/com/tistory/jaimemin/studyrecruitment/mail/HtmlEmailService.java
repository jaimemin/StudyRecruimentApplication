package com.tistory.jaimemin.studyrecruitment.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author jaime
 * @title HtmlEmailService
 * @see\n <pre>
 * </pre>
 * @since 2022-04-09
 */
@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            /**
             * 첨부 파일 보낼거면 true
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            /**
             * html 템플릿으로 보낼거면 true
             * plaintext로 보낼거면 false
             */
            mimeMessageHelper.setText(emailMessage.getMessage(), true);

            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getMessage());
        } catch (MessagingException e) {
            log.error("[HtmlEmailService.send()] error {}", e.getMessage());

            throw new RuntimeException(e);
        }
    }
}
