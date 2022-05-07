package com.tistory.jaimemin.studyrecruitment.modules.account.validator;

import com.tistory.jaimemin.studyrecruitment.modules.account.AccountRepository;
import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.account.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author jaime
 * @title NicknameValidator
 * @see\n <pre>
 * </pre>
 * @since 2022-04-02
 */
@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        Account account = accountRepository.findByNickname(nicknameForm.getNickname());

        if (account == null) {
            return;
        }

        errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
    }
}
