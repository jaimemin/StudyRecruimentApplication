package com.tistory.jaimemin.studyrecruitment.modules.study.event;

import com.tistory.jaimemin.studyrecruitment.infra.config.AppProperties;
import com.tistory.jaimemin.studyrecruitment.infra.mail.EmailMessage;
import com.tistory.jaimemin.studyrecruitment.infra.mail.EmailService;
import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountPredicates;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountRepository;
import com.tistory.jaimemin.studyrecruitment.modules.notification.Notification;
import com.tistory.jaimemin.studyrecruitment.modules.notification.NotificationRepository;
import com.tistory.jaimemin.studyrecruitment.modules.notification.NotificationType;
import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import com.tistory.jaimemin.studyrecruitment.modules.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jaime
 * @title StudyEventListener
 * @see\n <pre>
 * </pre>
 * @since 2022-05-14
 */
@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;

    private final AccountRepository accountRepository;

    private final EmailService emailService;

    private final TemplateEngine templateEngine;

    private final AppProperties appProperties;

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyRepository.findStudyWithTagsAndZonesById(studyCreatedEvent.getStudy().getId());
        Iterable<Account> accounts
                = accountRepository.findAll(AccountPredicates.findByTagsAndZones(study.getTags(), study.getZones()));
        accounts.forEach(account -> {
            if (account.isStudyCreatedByEmail()) {
                sendEmail(study
                        , account
                        , "새로운 스터디가 생겼습니다."
                        , "StudyRecruitment, '" + study.getTitle() + "' 스터디가 생겼습니다.");
            }

            if (account.isStudyCreatedByWeb()) {
                createNotification(study
                        , account
                        , study.getShortDescription()
                        , NotificationType.STUDY_CREATED);
            }
        });
    }

    @EventListener
    public void handleStudyUpdateEvent(StudyUpdateEvent studyUpdateEvent) {
        Study study = studyRepository.findStudyWithManagersAndMembersById(studyUpdateEvent.getStudy().getId());
        Set<Account> accounts = new HashSet<>();
        accounts.addAll(study.getManagers());
        accounts.addAll(study.getMembers());
        accounts.forEach(account -> {
            if (account.isStudyUpdatedByEmail()) {
                sendEmail(study
                        , account
                        , studyUpdateEvent.getMessage()
                        , "StudyRecruitment, '" + study.getTitle() + "' 스터디에 새 소식이 있습니다.");
            }

            if (account.isStudyUpdatedByWeb()) {
                createNotification(study
                        , account
                        , studyUpdateEvent.getMessage()
                        , NotificationType.STUDY_UPDATED);
            }
        });
    }

    private void createNotification(Study study
            , Account account
            , String message
            , NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setAccount(account);
        notification.setNotificationType(notificationType);

        notificationRepository.save(notification);
    }

    private void sendEmail(Study study
            , Account account
            , String contextMessage
            , String emailSubject) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/study/" + study.getEncodedPath());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", contextMessage);
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);
        EmailMessage emailMessage = EmailMessage.builder()
                .subject(emailSubject)
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.send(emailMessage);
    }
}
