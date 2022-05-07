package com.tistory.jaimemin.studyrecruitment.modules.account;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * @author jaime
 * @title UserAccount
 * @see\n <pre>
 * </pre>
 * @since 2022-03-26
 */
@Getter
public class UserAccount extends User {

    private Account account;

    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }
}
