package com.tistory.jaimemin.studyrecruitment.modules.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.jaimemin.studyrecruitment.infra.AbstractContainerBaseTest;
import com.tistory.jaimemin.studyrecruitment.infra.MockMvcTest;
import com.tistory.jaimemin.studyrecruitment.modules.account.form.TagForm;
import com.tistory.jaimemin.studyrecruitment.modules.account.form.ZoneForm;
import com.tistory.jaimemin.studyrecruitment.modules.tag.Tag;
import com.tistory.jaimemin.studyrecruitment.modules.tag.TagRepository;
import com.tistory.jaimemin.studyrecruitment.modules.zone.Zone;
import com.tistory.jaimemin.studyrecruitment.modules.zone.ZoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class SettingsControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ZoneRepository zoneRepository;

    private Zone testZone = Zone.builder()
            .city("test")
            .localNameOfCity("????????????")
            .province("????????????")
            .build();

    @BeforeEach
    void beforeEach() {
        zoneRepository.save(testZone);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }

//    @BeforeEach
//    void beforeEach() {
//        SignUpForm signUpForm = new SignUpForm();
//        signUpForm.setNickname("jaimemin");
//        signUpForm.setEmail("jaimemin@tistory.com");
//        signUpForm.setPassword("12341234");
//        accountService.processNewAccount(signUpForm);
//    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ???")
    @Test
    void updateProfileForm() throws Exception {
        String bio = "?????? ????????? ???????????? ??????.";

        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    // @WithUserDetails(value = "jaimemin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithAccount("jaimemin")
    @DisplayName("????????? ???????????? - ????????? ??????")
    @Test
    void updateProfile() throws Exception {
        String bio = "?????? ????????? ???????????? ??????.";

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
    @DisplayName("????????? ???????????? - ????????? ??????")
    @Test
    void updateProfileError() throws Exception {
        String bio = "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????. " +
                "?????? ????????? ???????????? ??????."; // 35??? ??????

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
    @DisplayName("???????????? ?????? ???")
    @Test
    void updatePasswordForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("jaimemin")
    @DisplayName("???????????? ?????? - ????????? ??????")
    @Test
    void updatePasswordSuccess() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                .param("newPassword", "12341234")
                .param("newPasswordConfirm", "12341234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("jaimemin");

        assertTrue(passwordEncoder.matches("12341234", account.getPassword()));
    }

    @WithAccount("jaimemin")
    @DisplayName("???????????? ?????? - ????????? ?????? - ???????????? ?????????")
    @Test
    void updatePasswordError() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12341234")
                        .param("newPasswordConfirm", "12345678")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().hasErrors());
    }

    @WithAccount("jaimemin")
    @DisplayName("?????? ?????? ???")
    @Test
    void updatNotificationsForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_NOTIFICATIONS_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notifications"));
    }

    @WithAccount("jaimemin")
    @DisplayName("?????? ?????? - ????????? ??????")
    @Test
    void updateNotificationsSuccess() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_NOTIFICATIONS_URL)
                        .param("studyCreatedByEmail", String.valueOf(false))
                        .param("studyCreatedByWeb", String.valueOf(true))
                        .param("studyEnrollmentResultByEmail", String.valueOf(false))
                        .param("studyEnrollmentResultByWeb", String.valueOf(true))
                        .param("studyUpdatedByEmail", String.valueOf(false))
                        .param("studyUpdatedByWeb", String.valueOf(true))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_NOTIFICATIONS_URL))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("jaimemin");

        assertTrue(account.isStudyUpdatedByWeb());
        assertTrue(account.isStudyCreatedByWeb());
        assertTrue(account.isStudyEnrollmentResultByWeb());
        assertFalse(account.isStudyCreatedByEmail());
        assertFalse(account.isStudyEnrollmentResultByEmail());
        assertFalse(account.isStudyUpdatedByEmail());
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ???")
    @Test
    void updateAccountForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ???????????? - ????????? ??????")
    @Test
    void updateAccount_success() throws Exception {
        String newNickname = "jaimemin2";

        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("jaimemin2"));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ???????????? - ????????? ??????")
    @Test
    void updateAccount_failure() throws Exception {
        String newNickname = "??\\_(???)_/??";

        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount("jaimemin")
    @DisplayName("?????? ?????? ???")
    @Test
    void updateTagsForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_TAGS_URL))
                .andExpect(view().name(SettingsController.SETTINGS_TAGS_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ??????")
    @Test
    void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag").orElseGet(null);
        Account jaimemin = accountRepository.findByNickname("jaimemin");

        assertNotNull(newTag);
        assertTrue(jaimemin.getTags().contains(newTag));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ??????")
    @Test
    void removeTag() throws Exception {
        Account jaimemin = accountRepository.findByNickname("jaimemin");
        Tag newTag = tagRepository.save(Tag.builder()
                .title("newTag")
                .build());
        accountService.addTag(jaimemin, newTag);

        assertTrue(jaimemin.getTags().contains(newTag));

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(jaimemin.getTags().contains(newTag));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ?????? ???")
    @Test
    void updateZonesForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ZONES_URL))
                .andExpect(view().name(SettingsController.SETTINGS_ZONES_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ?????? ??????")
    @Test
    void addZone() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(SettingsController.SETTINGS_ZONES_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());

        assertTrue(jaimemin.getZones().contains(zone));
    }

    @WithAccount("jaimemin")
    @DisplayName("????????? ?????? ?????? ??????")
    @Test
    void removeZone() throws Exception {
        Account jaimemin = accountRepository.findByNickname("jaimemin");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        accountService.addZone(jaimemin, zone);

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(SettingsController.SETTINGS_ZONES_URL + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(jaimemin.getTags().contains(zone));
    }
}