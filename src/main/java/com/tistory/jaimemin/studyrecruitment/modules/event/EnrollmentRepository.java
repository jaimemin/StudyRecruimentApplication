package com.tistory.jaimemin.studyrecruitment.modules.event;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title EnrollmentRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-04-30
 */
@Repository
@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
