package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title EventRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-04-23
 */
@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {
}
