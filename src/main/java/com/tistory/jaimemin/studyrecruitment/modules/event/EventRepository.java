package com.tistory.jaimemin.studyrecruitment.modules.event;

import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jaime
 * @title EventRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-04-23
 */
@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(value = "Event.withEnrollments", type = EntityGraph.EntityGraphType.LOAD)
    List<Event> findByStudyOrderByStartDateTime(Study study);
}
