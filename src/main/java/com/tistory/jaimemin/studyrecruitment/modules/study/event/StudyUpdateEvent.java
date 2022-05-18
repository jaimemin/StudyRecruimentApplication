package com.tistory.jaimemin.studyrecruitment.modules.study.event;

import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author jaime
 * @title StudyUpdateEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
@Getter
@RequiredArgsConstructor
public class StudyUpdateEvent {

    private final Study study;

    private final String message;
}
