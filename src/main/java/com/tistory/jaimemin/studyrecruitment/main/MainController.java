package com.tistory.jaimemin.studyrecruitment.main;

import com.tistory.jaimemin.studyrecruitment.account.CurrentUser;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jaime
 * @title MainController
 * @see\n <pre>
 * </pre>
 * @since 2022-03-26
 */
@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
