package com.tistory.jaimemin.studyrecruitment.settings;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title Profile
 * @see\n <pre>
 * </pre>
 * @since 2022-03-31
 */
@Data
@NoArgsConstructor
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }

}
