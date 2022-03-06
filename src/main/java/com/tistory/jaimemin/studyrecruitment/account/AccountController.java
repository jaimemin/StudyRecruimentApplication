package com.tistory.jaimemin.studyrecruitment.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jaime
 * @title AccountController
 * @see\n <pre>
 * </pre>
 * @since 2022-03-06
 */
@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String singUpForm(Model model) {
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }
}
