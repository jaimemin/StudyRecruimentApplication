package com.tistory.jaimemin.studyrecruitment.settings;

import com.tistory.jaimemin.studyrecruitment.account.AccountService;
import com.tistory.jaimemin.studyrecruitment.account.CurrentUser;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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

    private final AccountService accountService;

    @GetMapping(SETTINGS_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new Profile(account));

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
    public String updateProfile(@CurrentUser Account account
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
}