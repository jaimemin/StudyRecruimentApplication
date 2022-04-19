package com.tistory.jaimemin.studyrecruitment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author jaime
 * @title Enrollment
 * @see\n <pre>
 * </pre>
 * @since 2022-04-20
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

}
