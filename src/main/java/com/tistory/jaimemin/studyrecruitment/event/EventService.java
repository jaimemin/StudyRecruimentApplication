package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Event;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import com.tistory.jaimemin.studyrecruitment.event.form.EventForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    public Event createEvent(Study study, Event event, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);

        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm, event);

        // TODO: FIRST_COME_FIRST_SERVED에 대해서는 자동으로 추가 인원의 참가 신청을 확정 상태로 변경해야 함
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }
}
