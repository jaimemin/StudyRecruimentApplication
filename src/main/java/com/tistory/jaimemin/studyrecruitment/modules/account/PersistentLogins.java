package com.tistory.jaimemin.studyrecruitment.modules.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author jaime
 * @title PersistentLogins
 * @see\n <pre>
 * </pre>
 * @since 2022-03-27
 */
@Table(name = "persistent_logins")
@Entity
@Getter
@Setter
public class PersistentLogins {

    @Id
    @Column(length = 64)
    private String series;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(name = "last_used", nullable = false, length = 64)
    private LocalDateTime lastUsed;

}
