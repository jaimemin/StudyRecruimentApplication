package com.tistory.jaimemin.studyrecruitment.modules.study.event;

import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import lombok.Getter;

/**
 * @author jaime
 * @title StudyCreatedEvent
 * @see\n <pre>
 * </pre>
 * @since 2022-05-14
 */
@Getter
public class StudyCreatedEvent {

    private Study study;

    public StudyCreatedEvent(Study study) {
        this.study = study;
    }
}
