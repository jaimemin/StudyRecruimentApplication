package com.tistory.jaimemin.studyrecruitment.modules.study.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author jaime
 * @title StudyDescriptionForm
 * @see\n <pre>
 * </pre>
 * @since 2022-04-11
 */
@Data
public class StudyDescriptionForm {

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;
}
