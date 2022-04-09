package com.tistory.jaimemin.studyrecruitment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jaime
 * @title AppProperties
 * @see\n <pre>
 * </pre>
 * @since 2022-04-09
 */
@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
}
