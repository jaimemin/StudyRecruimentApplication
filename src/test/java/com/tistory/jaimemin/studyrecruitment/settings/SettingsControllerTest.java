package com.tistory.jaimemin.studyrecruitment.settings;

import com.tistory.jaimemin.studyrecruitment.WithAccount;
import com.tistory.jaimemin.studyrecruitment.account.AccountRepository;
import com.tistory.jaimemin.studyrecruitment.account.AccountService;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

//    @BeforeEach
//    void beforeEach() {
//        SignUpForm signUpForm = new SignUpForm();
//        signUpForm.setNickname("jaimemin");
//        signUpForm.setEmail("jaimemin@tistory.com");
//        signUpForm.setPassword("12341234");
//        accountService.processNewAccount(signUpForm);
//    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    // @WithUserDetails(value = "jaimemin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithAccount("jaimemin")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우.";

        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("jaimemin");

        assertEquals(bio, account.getBio());
    }

    @WithAccount("jaimemin")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfileError() throws Exception {
        String bio = "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우. " +
                "짧은 소개를 수정하는 경우."; // 35자 넘음

        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account account = accountRepository.findByNickname("jaimemin");

        assertNull(account.getBio());
    }

    @WithAccount("jaimemin")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        String bio = "짧은 소개를 수정하는 경우.";

        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }
}