package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Event;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author jaime
 * @title EventService
 * @see\n <pre>
 * </pre>
 * @since 2022-04-23
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Study study, Event event, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);

        return eventRepository.save(event);
    }
}
