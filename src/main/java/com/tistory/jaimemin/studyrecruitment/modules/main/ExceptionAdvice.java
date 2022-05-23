package com.tistory.jaimemin.studyrecruitment.modules.main;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.account.CurrentAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jaime
 * @title ExceptionAdvice
 * @see\n <pre>
 * </pre>
 * @since 2022-05-24
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public String handleRuntimeException(@CurrentAccount Account account
            , HttpServletRequest request
            , RuntimeException e) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), request.getRequestURI());
        } else {
            log.info("requeted '{}'", request.getRequestURI());
        }

        log.error("bad request {}", e);

        return "error";
    }
}
