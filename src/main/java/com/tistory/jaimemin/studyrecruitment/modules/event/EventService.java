package com.tistory.jaimemin.studyrecruitment.modules.event;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import com.tistory.jaimemin.studyrecruitment.modules.event.form.EventForm;
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

        if (enrollment.isAttended()) {
            return;
        }

        event.removeEnrollment(enrollment);
        enrollmentRepository.delete(enrollment);
        event.acceptNextWaitingEnrollment();
    }

    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(true);
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(false);
    }
}