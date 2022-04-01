package com.tistory.jaimemin.studyrecruitment.settings.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author jaime
 * @title PasswordForm
 * @see\n <pre>
 * </pre>
 * @since 2022-04-02
 */
@Data
public class PasswordForm {

    @Length(min = 8, max = 50)
    private String newPassword;

    @Length(min = 8, max = 50)
    private String newPasswordConfirm;
}
