package com.tistory.jaimemin.studyrecruitment.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.jaimemin.studyrecruitment.account.AccountService;
import com.tistory.jaimemin.studyrecruitment.account.CurrentAccount;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Tag;
import com.tistory.jaimemin.studyrecruitment.domain.Zone;
import com.tistory.jaimemin.studyrecruitment.settings.form.*;
import com.tistory.jaimemin.studyrecruitment.settings.validator.NicknameValidator;
import com.tistory.jaimemin.studyrecruitment.settings.validator.PasswordFormValidator;
import com.tistory.jaimemin.studyrecruitment.tag.TagRepository;
import com.tistory.jaimemin.studyrecruitment.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jaime
 * @title SettingsController
 * @see\n <pre>
 * </pre>
 * @since 2022-03-31
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {

    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";

    static final String SETTINGS_PROFILE_URL = "/settings/profile";

    static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";

    static final String SETTINGS_PASSWORD_URL = "/settings/password";

    static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";

    static final String SETTINGS_NOTIFICATIONS_URL = "/settings/notifications";

    static final String SETTINGS_ACCOUNT_VIEW_NAME = "settings/account";

    static final String SETTINGS_ACCOUNT_URL = "/settings/account";

    static final String SETTINGS_TAGS_VIEW_NAME = "settings/tags";

    static final String SETTINGS_TAGS_URL = "/settings/tags";

    static final String SETTINGS_ZONES_VIEW_NAME = "settings/zones";

    static final String SETTINGS_ZONES_URL = "/settings/zones";

    private final TagRepository tagRepository;

    private final ZoneRepository zoneRepository;

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    private final NicknameValidator nicknameValidator;

    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(SETTINGS_PROFILE_URL)
    public String updateProfileForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));

        return SETTINGS_PROFILE_VIEW_NAME;
    }

    /**
     * Profile 디폴트 생성자 없을 경우 NullPointerException
     *
     * @param account
     * @param profile
     * @param errors
     * @param model
     * @return
     */
    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentAccount Account account
            , @Valid @ModelAttribute Profile profile
            , Errors errors
            , Model model
            , RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_PROFILE_VIEW_NAME;
        }

        /**
         * account가 영속성 상태여야 @Transactional 안에서 업데이트 진행
         * @CurrentUser로 받은 account는 준영속성 상태
         */
        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다.");

        return "redirect:" + SETTINGS_PROFILE_URL;
    }

    @GetMapping(SETTINGS_PASSWORD_URL)
    public String updatePasswordForm(@CurrentAccount Account account
            , Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());

        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PASSWORD_URL)
    public String updatePassword(@CurrentAccount Account account
            , @Valid PasswordForm passwordForm
            , Errors errors
            , Model model
            , RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_PASSWORD_VIEW_NAME;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "패스워드를 성공적으로 변경했습니다.");

        return "redirect:" + SETTINGS_PASSWORD_URL;
    }

    @GetMapping(SETTINGS_NOTIFICATIONS_URL)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));

        return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    @PostMapping(SETTINGS_NOTIFICATIONS_URL)
    public String updateNotifications(@CurrentAccount Account account
            , @Valid Notifications notifications
            , Errors errors
            , Model model
            , RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_NOTIFICATIONS_VIEW_NAME;
        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");

        return "redirect:" + SETTINGS_NOTIFICATIONS_URL;
    }

    @GetMapping(SETTINGS_ACCOUNT_URL)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));

        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    @PostMapping(SETTINGS_ACCOUNT_URL)
    public String updateAccount(@CurrentAccount Account account
            , @Valid NicknameForm nicknameForm
            , Errors errors
            , Model model
            , RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_ACCOUNT_VIEW_NAME;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");

        return "redirect:" + SETTINGS_ACCOUNT_URL;
    }

    @GetMapping(SETTINGS_TAGS_URL)
    public String updateTags(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        Set<Tag> tags = accountService.getTags(account);
        List<String> allTags = tagRepository.findAll().stream()
                .map(Tag::getTitle)
                .collect(Collectors.toList());

        model.addAttribute("tags", tags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toList()));
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));
        model.addAttribute(account);

        return SETTINGS_TAGS_VIEW_NAME;
    }

    @PostMapping(SETTINGS_TAGS_URL + "/add")
    @ResponseBody
    public ResponseEntity addTags(@CurrentAccount Account account
            , Model model
            , @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title).orElseGet(() ->
            tagRepository.save(Tag.builder()
                    .title(tagForm.getTagTitle())
                    .build())
        );

        accountService.addTag(account, tag);

        return ResponseEntity.ok().build();
    }

    @PostMapping(SETTINGS_TAGS_URL + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account
            , Model model
            , @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title).orElseGet(null);

        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);

        return ResponseEntity.ok().build();
    }

    @GetMapping(SETTINGS_ZONES_URL)
    public String updateZonesForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        Set<Zone> zoneSet = accountService.getZones(account);
        List<String> zones = zoneSet.stream().map(Zone::toString).collect(Collectors.toList());
        List<String> allZones = zoneRepository.findAll().stream()
                        .map(Zone::toString)
                        .collect(Collectors.toList());

        model.addAttribute(account);
        model.addAttribute("zones", zones);
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return SETTINGS_ZONES_VIEW_NAME;
    }

    @PostMapping(SETTINGS_ZONES_URL + "/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentAccount Account account
            , @RequestBody ZoneForm zoneForm) {
        Zone zone
                = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());

        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);

        return ResponseEntity.ok().build();
    }

    @PostMapping(SETTINGS_ZONES_URL + "/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account
            , @RequestBody ZoneForm zoneForm) {
        Zone zone
                = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());

        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);

        return ResponseEntity.ok().build();
    }
}
