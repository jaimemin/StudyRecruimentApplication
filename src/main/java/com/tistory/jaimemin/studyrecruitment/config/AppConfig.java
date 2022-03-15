package com.tistory.jaimemin.studyrecruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jaime
 * @title AppConfig
 * @see\n <pre>
 * </pre>
 * @since 2022-03-16
 */
@Configuration
public class AppConfig {

    /**
     * BcryptPasswordEncoder
     * 해싱하는데 시간이 좀 걸림 (해커들의 시도 횟수를 줄이기 위해)
     * 공부해볼 키워드: bcrypt + salt
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
