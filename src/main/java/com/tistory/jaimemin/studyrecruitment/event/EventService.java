package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Enrollment;
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

    private final EnrollmentRepository enrollmentRepository;

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
        event.acceptWaitingList();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public void newEnrollment(Event event, Account account) {
        if (enrollmentRepository.existsByEventAndAccount(event, account)) {
            return;
        }

        Enrollment enrollment = Enrollment.builder()
                .enrolledAt(LocalDateTime.now())
                .accepted(event.isAbleToAcceptWaitingEnrollment())
                .account(account)
                .build();
        event.addEnrollment(enrollment);
        enrollmentRepository.save(enrollment);
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
        event.removeEnrollment(enrollment);
        enrollmentRepository.delete(enrollment);
        event.acceptNextWaitingEnrollment();
    }
}
