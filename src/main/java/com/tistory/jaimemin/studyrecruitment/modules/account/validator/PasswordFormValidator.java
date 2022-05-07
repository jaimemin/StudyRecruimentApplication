package com.tistory.jaimemin.studyrecruitment.modules.account.validator;

import com.tistory.jaimemin.studyrecruitment.modules.account.form.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author jaime
 * @title PasswordFormValidator
 * @see\n <pre>
 * </pre>
 * @since 2022-04-02
 */
public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;

        if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword","wrong.value", "입력한 패스워드가 일치하지 않습니다.");
        }
    }
}
