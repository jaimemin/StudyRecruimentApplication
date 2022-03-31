package com.tistory.jaimemin.studyrecruitment.settings;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.Data;

/**
 * @author jaime
 * @title Profi
 * @see\n <pre>
 * </pre>
 * @since 2022-03-31
 */
@Data
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
