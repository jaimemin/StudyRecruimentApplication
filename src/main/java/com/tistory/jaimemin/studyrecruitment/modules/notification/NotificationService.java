package com.tistory.jaimemin.studyrecruitment.modules.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jaime
 * @title NotificationService
 * @see\n <pre>
 * </pre>
 * @since 2022-05-17
 */
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(notification -> {
            notification.setChecked(true);
        });

        notificationRepository.saveAll(notifications);
    }
}
