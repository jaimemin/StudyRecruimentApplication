package com.tistory.jaimemin.studyrecruitment.modules.study.validator;

import com.tistory.jaimemin.studyrecruitment.modules.study.StudyRepository;
import com.tistory.jaimemin.studyrecruitment.modules.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author jaime
 * @title StudyFormValidator
 * @see\n <pre>
 * </pre>
 * @since 2022-04-10
 */
@Component
@RequiredArgsConstructor
public class StudyFormValidator implements Validator {

    private final StudyRepository studyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyForm studyForm = (StudyForm) target;

        if (studyRepository.existsByPath(studyForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "해당 스터디 경로 값을 사용할 수 없습니다.");
        }
    }
}
