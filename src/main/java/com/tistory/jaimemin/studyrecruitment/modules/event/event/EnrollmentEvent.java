package com.tistory.jaimemin.studyrecruitment.modules.event.event;

import com.tistory.jaimemin.studyrecruitment.modules.event.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author jaime
 * @title EnrollmentEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    protected final Enrollment enrollment;

    protected final String message;
}
