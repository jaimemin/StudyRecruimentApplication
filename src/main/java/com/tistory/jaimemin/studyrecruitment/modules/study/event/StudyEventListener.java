package com.tistory.jaimemin.studyrecruitment.modules.study.event;

import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title StudyEventListener
 * @see\n <pre>
 * </pre>
 * @since 2022-05-14
 */
@Slf4j
@Async
@Component
@Transactional(readOnly = true)
public class StudyEventListener {

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyCreatedEvent.getStudy();

        log.info( "{} is created.", study.getTitle());
        // TODO: 이메일 보내거나, DB에 Notification 정보를 저장
    }
}
