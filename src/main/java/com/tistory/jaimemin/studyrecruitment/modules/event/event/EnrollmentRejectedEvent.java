package com.tistory.jaimemin.studyrecruitment.modules.event.event;

import com.tistory.jaimemin.studyrecruitment.modules.event.Enrollment;

/**
 * @author jaime
 * @title EnrollmentRejectedEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
public class EnrollmentRejectedEvent extends EnrollmentEvent {

    public EnrollmentRejectedEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청을 거절했습니다.");
    }
}
