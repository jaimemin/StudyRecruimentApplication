package com.tistory.jaimemin.studyrecruitment.modules.event.event;

import com.tistory.jaimemin.studyrecruitment.infra.config.AppProperties;
import com.tistory.jaimemin.studyrecruitment.infra.mail.EmailMessage;
import com.tistory.jaimemin.studyrecruitment.infra.mail.EmailService;
import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.event.Enrollment;
import com.tistory.jaimemin.studyrecruitment.modules.event.Event;
import com.tistory.jaimemin.studyrecruitment.modules.notification.Notification;
import com.tistory.jaimemin.studyrecruitment.modules.notification.NotificationRepository;
import com.tistory.jaimemin.studyrecruitment.modules.notification.NotificationType;
import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

/**
 * @author jaime
 * @title EnrollmentEventListener
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class EnrollmentEventListener {

    private final NotificationRepository notificationRepository;

    private final AppProperties appProperties;

    private final TemplateEngine templateEngine;

    private final EmailService emailService;

    @EventListener
    public void handleEnrollmentEvent(EnrollmentEvent enrollmentEvent) {
        Enrollment enrollment = enrollmentEvent.getEnrollment();
        Account account = enrollment.getAccount();
        Event event = enrollment.getEvent();
        Study study = event.getStudy();

        if (account.isStudyEnrollmentResultByEmail()) {
            sendEmail(enrollmentEvent
                    , account
                    , event
                    , study);
        }

        if (account.isStudyEnrollmentResultByWeb()) {
            createNotification(enrollmentEvent
                    , account
                    , event
                    , study);
        }
    }

    private void sendEmail(EnrollmentEvent enrollmentEvent, Account account, Event event, Study study) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodedPath() + "/events/" + event.getId());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", enrollmentEvent.getMessage());
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("StudyRecruitment, " + event.getTitle() + " 모임 참가 신청 결과입니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.send(emailMessage);
    }

    private void createNotification(EnrollmentEvent enrollmentEvent, Account account, Event event, Study study) {
        Notification notification = new Notification();
        notification.setTitle(study.getTitle() + " / " + event.getTitle());
        notification.setLink("/study/" + study.getEncodedPath() + "/events/" + event.getId());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(enrollmentEvent.getMessage());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.EVENT_ENROLLMENT);
        notificationRepository.save(notification);
    }
}