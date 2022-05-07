package com.tistory.jaimemin.studyrecruitment.modules.main;

import com.tistory.jaimemin.studyrecruitment.infra.MockMvcTest;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountRepository;
import com.tistory.jaimemin.studyrecruitment.modules.account.AccountService;
import com.tistory.jaimemin.studyrecruitment.modules.account.form.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jaime
 * @title MainControllerTest
 * @see\n <pre>
 * </pre>
 * @since 2022-03-26
 */
@MockMvcTest
public class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("jaimemin");
        signUpForm.setEmail("jaimemin@tistory.com");
        signUpForm.setPassword("12341234");
        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 성공")
    @Test
    void loginWithEmail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "jaimemin@tistory.com")
                .param("password", "12341234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jaimemin"));
    }

    @DisplayName("닉네임으로 로그인 성공")
    @Test
    void loginWithNickname() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "jaimemin")
                        .param("password", "12341234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jaimemin"));
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "error")
                        .param("password", "1234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}
