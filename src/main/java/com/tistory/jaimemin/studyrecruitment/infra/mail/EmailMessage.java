package com.tistory.jaimemin.studyrecruitment.infra.mail;

import lombok.Builder;
import lombok.Data;

/**
 * @author jaime
 * @title EmailMessage
 * @see\n <pre>
 * </pre>
 * @since 2022-04-09
 */
@Data
@Builder
public class EmailMessage {

    // 수신자
    private String to;
    
    // 제목
    private String subject;
    
    // 메시지
    private String message;
}
