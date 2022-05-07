package com.tistory.jaimemin.studyrecruitment.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jaime
 * @title AccountFactory
 * @see\n <pre>
 * </pre>
 * @since 2022-05-08
 */
@Component
@RequiredArgsConstructor
public class AccountFactory {

    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account jaimemin = new Account();
        jaimemin.setNickname(nickname);
        jaimemin.setEmail(nickname + "@email.com");
        accountRepository.save(jaimemin);

        return jaimemin;
    }
}
