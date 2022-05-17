package com.tistory.jaimemin.studyrecruitment.modules.notification;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    List<Notification> findByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean b);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean b);
}
