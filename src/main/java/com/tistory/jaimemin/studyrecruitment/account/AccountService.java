package com.tistory.jaimemin.studyrecruitment.account;

import com.tistory.jaimemin.studyrecruitment.account.form.SignUpForm;
import com.tistory.jaimemin.studyrecruitment.config.AppProperties;
import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Tag;
import com.tistory.jaimemin.studyrecruitment.domain.Zone;
import com.tistory.jaimemin.studyrecruitment.mail.EmailMessage;
import com.tistory.jaimemin.studyrecruitment.mail.EmailService;
import com.tistory.jaimemin.studyrecruitment.settings.form.Notifications;
import com.tistory.jaimemin.studyrecruitment.settings.form.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author jaime
 * @title AccountService
 * @see\n <pre>
 * </pre>
 * @since 2022-03-16
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private static final String SIMPLE_LINK_TEMPLATE = "mail/simple-link";

    private final AccountRepository accountRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final TemplateEngine templateEngine;

    private final AppProperties appProperties;

    /**
     * @Transactional 붙이지 않으면 persist 상태 유지 안됨 (detached)
     *
     * @param signUpForm
     */
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignUpConfirmEmail(newAccount);

        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();

        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        Context context = getSignUpConfirmContext(newAccount);
        String message = templateEngine.process(SIMPLE_LINK_TEMPLATE, context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("Study-Recruitment, 회원 가입 인증")
                .message(message)
                .build();

        emailService.send(emailMessage);
    }

    public void login(Account account) {
        /**
         * 정석대로라면 AuthenticationManager를 통해서 인증을 해야함
         */
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account)
                , account.getPassword()
                , List.of(new SimpleGrantedAuthority("ROLE USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    /**
     * 읽을 떄 데이터 변경이 없으므로 성능을 위해 readOnly = true
     *
     * @param emailOrNickname
     * @return
     * @throws UsernameNotFoundException
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);

        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    /**
     * Transactional이 걸린 Service 계층에서 데이터 변경이 이루어져야 영속성 Context에서 변경이 이루어짐
     * 
     * @param account
     */
    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        modelMapper.map(profile, account);
        
        /**
         * account가 준영속상태이므로 명시적으로 save
         */
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));

        /**
         * account가 준영속상태이므로 명시적으로 save
         */
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);

        /**
         * account가 준영속상태이므로 명시적으로 save
         */
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);

        /**
         * account가 준영속상태이므로 명시적으로 save
         */
        accountRepository.save(account);
        login(account);
    }

    public void sendLoginLink(Account account) {
        account.generateEmailCheckToken();
        Context context = getLoginLinkContext(account);
        String message = templateEngine.process(SIMPLE_LINK_TEMPLATE, context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("Study-Recruitment, 로그인 링크")
                .message(message)
                .build();

        emailService.send(emailMessage);
    }

    public void addTag(Account account, Tag tag) {
        /**
         * 영속 상태로 만들기 위해
         * accountRepository.getOne()은 lazy loading
         * 현 상황에서는 findById와 getOne() 둘 다 같은 동작
         */
        Optional<Account> currentAccount = accountRepository.findById(account.getId());
        currentAccount.ifPresent(a -> a.getTags().add(tag));
    }

    public Set<Tag> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());

        return byId.orElseThrow().getTags();
    }

    public void removeTag(Account account, Tag tag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getTags().remove(tag));
    }

    public Set<Zone> getZones(Account account) {
        return accountRepository.findById(account.getId())
                .orElseThrow()
                .getZones();
    }

    public void addZone(Account account, Zone zone) {
        accountRepository.findById(account.getId()).ifPresent(a -> {
            a.getZones().add(zone);
        });
    }


    public void removeZone(Account account, Zone zone) {
        accountRepository.findById(account.getId()).ifPresent(a -> {
            a.getZones().remove(zone);
        });
    }

    private Context getSignUpConfirmContext(Account newAccount) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token="
                + newAccount.getEmailCheckToken()
                + "&email="
                + newAccount.getEmail());
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "Study-Recruitment 서비스를 사용하려면 링크를 사용하세요.");
        context.setVariable("host", appProperties.getHost());

        return context;
    }

    private Context getLoginLinkContext(Account account) {
        Context context = new Context();
        context.setVariable("link", "/login-by-email?token="
                + account.getEmailCheckToken()
                + "&email="
                + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "Study-Recruitment 로그인하기");
        context.setVariable("message", "로그인 하려면 하단 링크를 사용하세요.");
        context.setVariable("host", appProperties.getHost());

        return context;
    }
}
