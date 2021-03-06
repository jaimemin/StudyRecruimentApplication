package com.tistory.jaimemin.studyrecruitment.modules.study.event;

import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author jaime
 * @title StudyCreatedEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-14
 */
@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private final Study study;

}
