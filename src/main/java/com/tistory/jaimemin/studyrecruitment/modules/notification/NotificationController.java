package com.tistory.jaimemin.studyrecruitment.modules.notification;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.account.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title NotificationController
 * @see\n <pre>
 * </pre>
 * @since 2022-05-17
 */
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications
                = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
        long numberOfChecked = notificationRepository.countByAccountAndChecked(account, true);
        putCategorizedNotifications(model, notifications, numberOfChecked, notifications.size());
        notificationService.markAsRead(notifications);
        model.addAttribute("isNew", true);

        return "notification/list";
    }

    @GetMapping("/notifications/old")
    public String getOldNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications
                = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
        long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account, false);
        putCategorizedNotifications(model, notifications, notifications.size(), numberOfNotChecked);
        model.addAttribute("isNew", false);

        return "notification/list";
    }

    @DeleteMapping("/notifications")
    public String deleteNotifications(@CurrentAccount Account account) {
        notificationRepository.deleteByAccountAndChecked(account, true);

        return "redirect:/notifications";
    }

    private void putCategorizedNotifications(Model model
            , List<Notification> notifications
            , long numberOfChecked
            , long numberOfNotChecked) {
        List<Notification> newStudyNotifications = new ArrayList<>();
        List<Notification> eventEnrollmentNotifications = new ArrayList<>();
        List<Notification> watchingStudyNotifications = new ArrayList<>();

        for (Notification notification : notifications) {
            switch(notification.getNotificationType()) {
                case STUDY_CREATED:
                    newStudyNotifications.add(notification);

                    break;
                case EVENT_ENROLLMENT:
                    eventEnrollmentNotifications.add(notification);

                    break;
                case STUDY_UPDATED:
                    watchingStudyNotifications.add(notification);

                    break;
            }
        }

        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newStudyNotifications", newStudyNotifications);
        model.addAttribute("eventEnrollmentNotifications", eventEnrollmentNotifications);
        model.addAttribute("watchingStudyNotifications", watchingStudyNotifications);
    }
}
