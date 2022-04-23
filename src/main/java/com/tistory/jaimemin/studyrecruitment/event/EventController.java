package com.tistory.jaimemin.studyrecruitment.event;

import com.tistory.jaimemin.studyrecruitment.account.CurrentAccount;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Event;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import com.tistory.jaimemin.studyrecruitment.event.form.EventForm;
import com.tistory.jaimemin.studyrecruitment.event.validator.EventValidator;
import com.tistory.jaimemin.studyrecruitment.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author jaime
 * @title EventController
 * @see\n <pre>
 * </pre>
 * @since 2022-04-23
 */
@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

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

}
