package com.tistory.jaimemin.studyrecruitment.domain;

import com.tistory.jaimemin.studyrecruitment.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title Event
 * @see\n <pre>
 * </pre>
 * @since 2022-04-20
 */
@NamedEntityGraph(
        name = "Event.withEnrollments",
        attributeNodes = @NamedAttributeNode("enrollments")
)
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column
    private Integer limitOfEnrollments;

    @OneToMany(mappedBy = "event")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public boolean isEnrollableFor(UserAccount userAccount) {
        return isNotClosed() && !isAlreadyEnrolled(userAccount);
    }

    public boolean isDisenrollableFor(UserAccount userAccount) {
        return isNotClosed() && isAttended(userAccount);
    }

    public boolean isNotClosed() {
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    public boolean isAttended(UserAccount userAccount) {
        Account account = userAccount.getAccount();

        for (Enrollment enrollment : this.enrollments) {
            if (enrollment.getAccount().equals(account)
                    && enrollment.isAttended()) {
                return true;
            }
        }

        return false;
    }

    public boolean isAlreadyEnrolled(UserAccount userAccount) {
        Account account = userAccount.getAccount();

        for (Enrollment enrollment : this.enrollments) {
            if (enrollment.getAccount().equals(account)) {
                return true;
            }
        }

        return false;
    }

    public int numberOfRemainingSpots() {
        return this.limitOfEnrollments - (int) getNumberOfAcceptedEnrollments();
    }

    public long getNumberOfAcceptedEnrollments() {
        return this.enrollments.stream()
                .filter(Enrollment::isAccepted)
                .count();
    }
}
