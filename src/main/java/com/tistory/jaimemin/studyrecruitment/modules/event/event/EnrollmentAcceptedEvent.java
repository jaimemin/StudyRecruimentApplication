package com.tistory.jaimemin.studyrecruitment.modules.event.event;

import com.tistory.jaimemin.studyrecruitment.modules.event.Enrollment;

/**
 * @author jaime
 * @title EnrollmentAcceptedEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
public class EnrollmentAcceptedEvent extends EnrollmentEvent {

    public EnrollmentAcceptedEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청을 확인했습니다. 모임에 참석하세요.");
    }
}
