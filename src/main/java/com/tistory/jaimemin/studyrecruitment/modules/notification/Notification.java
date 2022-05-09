package com.tistory.jaimemin.studyrecruitment.modules.notification;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author jaime
 * @title Notification
 * @see\n <pre>
 * </pre>
 * @since 2022-05-09
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    
    private String link;

    private String message;

    private boolean checked;

    @ManyToOne
    private Account account;

    private LocalDateTime createdLocalDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
