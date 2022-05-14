package com.tistory.jaimemin.studyrecruitment.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title AccountRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-03-06
 */
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);
}
