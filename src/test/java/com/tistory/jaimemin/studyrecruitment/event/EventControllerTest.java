package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.WithAccount;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Event;
import com.tistory.jaimemin.studyrecruitment.domain.EventType;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import com.tistory.jaimemin.studyrecruitment.study.StudyControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends StudyControllerTest {

    private static final int ENROLLMENT_LIMIT = 2;

    @Autowired
    EventService eventService;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithAccount("jaimemin")
    void newEnrollmentToFirstComeFirstServedEventAccepted() throws Exception {
        Account testUser = createAccount("testUser");
        Study study = createStudy("test-study", testUser);
        Event event = creatEvent("test-event"
                , EventType.FIRST_COME_FIRST_SERVED
                , ENROLLMENT_LIMIT
                , study
                , testUser);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        isAccepted(jaimemin, event);
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉차서)")
    @WithAccount("jaimemin")
    void newEnrollmentToFirstComeFirstServedEventNotAccepted() throws Exception {
        Account testUser = createAccount("testUser");
        Study study = createStudy("test-study", testUser);
        Event event = creatEvent("test-event"
                , EventType.FIRST_COME_FIRST_SERVED
                , ENROLLMENT_LIMIT
                , study
                , testUser);

        Account may = createAccount("may");
        Account june = createAccount("june");
        eventService.newEnrollment(event, may);
        eventService.newEnrollment(event, june);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        isNotAccepted(jaimemin, event);
    }

    @Test
    @DisplayName("참가신청 확정자가 선착순 모임에 참가 신청을 취소하는 경우, 바로 다음 대기자를 자동으로 신청 확인한다.")
    @WithAccount("jaimemin")
    void acceptedAccountCancelEnrollmentToFirstComeFirstServedEventNotAccepted() throws Exception {
        Account jaimemin = accountRepository.findByNickname("jaimemin");
        Account testUser = createAccount("testUser");
        Account may = createAccount("may");
        Study study = createStudy("test-study", testUser);
        Event event = creatEvent("test-event"
                , EventType.FIRST_COME_FIRST_SERVED
                , ENROLLMENT_LIMIT
                , study
                , testUser);

        eventService.newEnrollment(event, may);
        eventService.newEnrollment(event, jaimemin);
        eventService.newEnrollment(event, testUser);

        isAccepted(may, event);
        isAccepted(jaimemin, event);
        isNotAccepted(testUser, event);

        // jaimemin 모임 참가 취소
        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(may, event);
        isAccepted(testUser, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, jaimemin));
    }

    @Test
    @DisplayName("참가신청 비확정자가 선착순 모임에 참가 신청을 취소하는 경우, 기존 확정자를 그대로 유지하고 새로운 확정자는 없다.")
    @WithAccount("jaimemin")
    void notAcceptedAccountCancelEnrollmentToFirstComeFirstServedEventNotAccepted() throws Exception {
        Account jaimemin = accountRepository.findByNickname("jaimemin");
        Account testUser = createAccount("testUser");
        Account may = createAccount("may");
        Study study = createStudy("test-study", testUser);
        Event event = creatEvent("test-event"
                , EventType.FIRST_COME_FIRST_SERVED
                , ENROLLMENT_LIMIT
                , study
                , testUser);

        eventService.newEnrollment(event, may);
        eventService.newEnrollment(event, testUser);
        eventService.newEnrollment(event, jaimemin);

        isAccepted(may, event);
        isAccepted(testUser, event);
        isNotAccepted(jaimemin, event);

        // 애초에 jaimemin은 모임에 가입되어있지도 않다 -> 모임 참석 취소 요청해도 변경되는 것은 없음
        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        isAccepted(may, event);
        isAccepted(testUser, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, jaimemin));
    }

    @Test
    @DisplayName("관리자 확인 모임에 참가 신청 - 대기중")
    @WithAccount("jaimemin")
    void newEnrollmentToConfirmativeEventNotAccepted() throws Exception {
        Account testUser = createAccount("testUser");
        Study study = createStudy("test-study", testUser);
        Event event = creatEvent("test-event"
                , EventType.CONFIRMATIVE
                , ENROLLMENT_LIMIT
                , study
                , testUser);

        mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        isNotAccepted(jaimemin, event);
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private void isNotAccepted(Account account, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private Event creatEvent(String eventTitle
            , EventType eventType
            , int enrollmentLimit
            , Study study
            , Account account) {
        Event event = Event.builder()
                .eventType(eventType)
                .limitOfEnrollments(enrollmentLimit)
                .title(eventTitle)
                .createdDateTime(LocalDateTime.now())
                .endEnrollmentDateTime(LocalDateTime.now().plusDays(1))
                .startDateTime(LocalDateTime.now().plusDays(1).plusHours(5))
                .endDateTime(LocalDateTime.now().plusDays(1).plusHours(10))
                .enrollments(new ArrayList<>())
                .build();

        return eventService.createEvent(study, event, account);
    }
}