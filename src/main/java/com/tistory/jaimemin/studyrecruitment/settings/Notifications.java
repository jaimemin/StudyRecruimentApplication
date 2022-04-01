package com.tistory.jaimemin.studyrecruitment.settings;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title Notifications
 * @see\n <pre>
 * </pre>
 * @since 2022-04-02
 */
@Data
@NoArgsConstructor
public class Notifications {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public Notifications(Account account) {
        this.studyCreatedByEmail = account.isStudyCreatedByEmail();
        this.studyCreatedByWeb = account.isStudyCreatedByWeb();
        this.studyEnrollmentResultByEmail = account.isStudyEnrollmentResultByEmail();
        this.studyEnrollmentResultByWeb = account.isStudyUpdatedByWeb();
        this.studyUpdatedByEmail = account.isStudyUpdatedByEmail();
        this.studyUpdatedByWeb = account.isStudyUpdatedByWeb();
    }
}
