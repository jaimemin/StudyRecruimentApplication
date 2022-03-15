package com.tistory.jaimemin.studyrecruitment.account;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.ObjectUtils;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author jaime
 * @title AccountController
 * @see\n <pre>
 * </pre>
 * @since 2022-03-06
 */
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String singUpForm(Model model) {
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        accountService.processNewAccount(signUpForm);

        // TODO 회원 가입 처리
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(@RequestParam String token
            , @RequestParam String email
            , Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";

        if (account == null) {
            model.addAttribute("error", "wrong.email");

            return view;
        }

        if (!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");

            return view;
        }

        account.completeSignUp();

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        return view;
    }
}
