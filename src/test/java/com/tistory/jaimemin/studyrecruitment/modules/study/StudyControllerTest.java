package com.tistory.jaimemin.studyrecruitment.modules.study;

import com.tistory.jaimemin.studyrecruitment.infra.AbstractContainerBaseTest;
import com.tistory.jaimemin.studyrecruitment.infra.MockMvcTest;
import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountFactory;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountRepository;
import com.tistory.jaimemin.studyrecruitment.modules.account.WithAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jaime
 * @title StudyControllerTest
 * @see\n <pre>
 * </pre>
 * @since 2022-04-11
 */
@MockMvcTest
public class StudyControllerTest extends AbstractContainerBaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    AccountFactory accountFactory;

    @Autowired
    StudyFactory studyFactory;

    @Autowired 
    protected StudyService studyService;
    
    @Autowired 
    protected StudyRepository studyRepository;
    
    @Autowired
    protected AccountRepository accountRepository;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 개설 폼 조회")
    void createStudyForm() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 개설 - 완료")
    void createStudy_success() throws Exception {
        mockMvc.perform(post("/new-study")
                        .param("path", "test-path")
                        .param("title", "study title")
                        .param("shortDescription", "짧은 소개")
                        .param("fullDescription", "긴 소개")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/test-path"));

        Study study = studyRepository.findByPath("test-path");
        assertNotNull(study);

        Account account = accountRepository.findByNickname("jaimemin");
        assertTrue(study.getManagers().contains(account));
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 개설 - 실패")
    void createStudy_fail() throws Exception {
        mockMvc.perform(post("/new-study")
                        .param("path", "wrong path")
                        .param("title", "study title")
                        .param("shortDescription", "짧은 소개")
                        .param("fullDescription", "긴 소개")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("studyForm"))
                .andExpect(model().attributeExists("account"));

        Study study = studyRepository.findByPath("test-path");
        assertNull(study);
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 조회")
    void viewStudy() throws Exception {
        Study study = new Study();
        study.setPath("test-path");
        study.setTitle("test study");
        study.setShortDescription("짧은 소개");
        study.setFullDescription("<p>긴 소개</p>");

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        studyService.createNewStudy(study, jaimemin);

        mockMvc.perform(get("/study/test-path"))
                .andExpect(view().name("study/view"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 가입")
    void joinStudy() throws Exception {
        Account testAccount = accountFactory.createAccount("testAccount");

        Study study = studyFactory.createStudy("test-study", testAccount);

        mockMvc.perform(get("/study/" + study.getPath() + "/join"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        Account jaimemin = accountRepository.findByNickname("jaimemin");

        assertTrue(study.getMembers().contains(jaimemin));
    }

    @Test
    @WithAccount("jaimemin")
    @DisplayName("스터디 탈퇴")
    void leaveStudy() throws Exception {
        Account testAccount = accountFactory.createAccount("testAccount");
        Study study = studyFactory.createStudy("test-study", testAccount);

        Account jaimemin = accountRepository.findByNickname("jaimemin");
        studyService.addMember(study, jaimemin);

        mockMvc.perform(get("/study/" + study.getPath() + "/leave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        assertFalse(study.getMembers().contains(jaimemin));
    }
    
}
