package com.tistory.jaimemin.studyrecruitment.settings.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author jaime
 * @title Profile
 * @see\n <pre>
 * </pre>
 * @since 2022-03-31
 */
@Data
public class Profile {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String occupation;

    @Length(max = 50)
    private String location;

    private String profileImage;

}
