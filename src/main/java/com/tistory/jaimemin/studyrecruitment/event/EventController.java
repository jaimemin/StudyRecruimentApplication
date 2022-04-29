package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.account.CurrentAccount;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Event;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import com.tistory.jaimemin.studyrecruitment.event.form.EventForm;
import com.tistory.jaimemin.studyrecruitment.event.validator.EventValidator;
import com.tistory.jaimemin.studyrecruitment.study.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title EventController
 * @see\n <pre>
 * </pre>
 * @since 2022-04-23
 */
@Slf4j
@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final StudyService studyService;

    private final EventService eventService;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventValidator);
    }

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account
            , @PathVariable String path
            , Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);

        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(new EventForm());

        return "event/form";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@CurrentAccount Account account
            , @PathVariable String path
            , @Valid EventForm eventForm
            , Errors errors
            , Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(study);

            return "event/form";
        }

        Event event = eventService.createEvent(study
                , modelMapper.map(eventForm, Event.class)
                , account);

        return "redirect:/study/" + study.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id
            , Model model) {
        model.addAttribute(account);
        model.addAttribute(eventRepository.findById(id).orElseThrow());
        model.addAttribute(studyService.getStudy(path));

        return "event/view";
    }

    @GetMapping("/events")
    public String viewStudyEvents(@CurrentAccount Account account
            , @PathVariable String path
            , Model model) {
        Study study = studyService.getStudy(path);
        List<Event> events = eventRepository.findByStudyOrderByStartDateTime(study);
        List<Event> newEvents = new ArrayList<>();
        List<Event> oldEvents = new ArrayList<>();

        events.forEach(event -> {
            if (event.getEndDateTime().isBefore(LocalDateTime.now())) {
                oldEvents.add(event);
            } else {
                newEvents.add(event);
            }
        });

        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute("newEvents", newEvents);
        model.addAttribute("oldEvents", oldEvents);

        return "study/events";
    }

    @GetMapping("/events/{id}/edit")
    public String updateEventForm(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id
            , Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        Event event = eventRepository.findById(id).orElseThrow();

        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(event);
        model.addAttribute(modelMapper.map(event, EventForm.class));

        return "event/update-form";
    }

    @PostMapping("/events/{id}/edit")
    public String updatEventSubmit(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id
            , @Valid EventForm eventForm
            , Errors errors
            , Model model) {
        Study study = studyService.getStudyToUpdate(account, path);
        Event event = eventRepository.findById(id).orElseThrow();
        eventForm.setEventType(event.getEventType());

        eventValidator.validateUpdateForm(eventForm, event, errors);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute(event);

            return "event/update-form";
        }

        eventService.updateEvent(event, eventForm);

        return "redirect:/study/" + study.getEncodedPath() + "/events/" + event.getId();
    }

    // @PostMapping("/events/{id}/delete")
    @DeleteMapping("/events/{id}")
    public String cancelEvent(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        eventService.deleteEvent(eventRepository.findById(id).orElseThrow());

        return "redirect:/study/" + study.getEncodedPath() + "/events";
    }

    @PostMapping("/events/{id}/enroll")
    public String newEnrollment(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id) {
        Study study = studyService.getStudyToEnroll(path);
        eventService.newEnrollment(eventRepository.findById(id).orElseThrow(), account);

        return "redirect:/study/" + study.getEncodedPath() + "/events/" + id;
    }

    @PostMapping("/events/{id}/disenroll")
    public String cancelEnrollment(@CurrentAccount Account account
            , @PathVariable String path
            , @PathVariable Long id) {
        Study study = studyService.getStudyToEnroll(path);
        eventService.cancelEnrollment(eventRepository.findById(id).orElseThrow(), account);

        return "redirect:/study/" + study.getEncodedPath() + "/events/" + id;
    }
}
