package com.tistory.jaimemin.studyrecruitment.modules.event;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.event.event.EnrollmentAcceptedEvent;
import com.tistory.jaimemin.studyrecruitment.modules.event.event.EnrollmentRejectedEvent;
import com.tistory.jaimemin.studyrecruitment.modules.study.Study;
import com.tistory.jaimemin.studyrecruitment.modules.event.form.EventForm;
import com.tistory.jaimemin.studyrecruitment.modules.study.event.StudyUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
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

    private final ApplicationEventPublisher eventPublisher;

    public Event createEvent(Study study, Event event, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy()
                , "'" + event.getTitle() + "' 모임을 만들었습니다."));

        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm, event);
        event.acceptWaitingList();
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy()
                , "'" + event.getTitle() + "' 모임 정보를 수정했으니 확인하세요."));
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy()
                , "'" + event.getTitle() + "' 모임을 취소했습니다."));
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
        eventPublisher.publishEvent(new EnrollmentAcceptedEvent(enrollment));
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
        eventPublisher.publishEvent(new EnrollmentRejectedEvent(enrollment));
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(true);
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(false);
    }
}
