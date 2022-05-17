package com.tistory.jaimemin.studyrecruitment.modules.notification;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title NotificationRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-05-15
 */
@Transactional(readOnly = true)
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByAccountAndChecked(Account account, boolean checked);
}
