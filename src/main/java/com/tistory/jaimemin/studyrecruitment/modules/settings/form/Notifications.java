package com.tistory.jaimemin.studyrecruitment.modules.settings.form;

import lombok.Data;

/**
 * @author jaime
 * @title Notifications
 * @see\n <pre>
 * </pre>
 * @since 2022-04-02
 */
@Data
public class Notifications {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

}
