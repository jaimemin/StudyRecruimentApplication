package com.tistory.jaimemin.studyrecruitment.domain;

import com.tistory.jaimemin.studyrecruitment.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return isNotClosed() && !isAttended(userAccount) && !isAlreadyEnrolled(userAccount);
    }

    public boolean isDisenrollableFor(UserAccount userAccount) {
        return isNotClosed() && !isAttended(userAccount) && isAlreadyEnrolled(userAccount);
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

    public boolean canAccept(Enrollment enrollment) {
        return notAttended(enrollment) && !enrollment.isAccepted();
    }

    public boolean canReject(Enrollment enrollment) {
        return notAttended(enrollment) && enrollment.isAccepted();
    }

    private boolean notAttended(Enrollment enrollment) {
        return this.eventType == EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setEvent(null);
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        return this.eventType == EventType.FIRST_COME_FIRST_SERVED
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments();
    }

    public Enrollment getTheFirstWaitingEnrollment() {
        for (Enrollment enrollment : this.enrollments) {
            if (!enrollment.isAccepted()) {
                return enrollment;
            }
        }

        return null;
    }

    public void acceptNextWaitingEnrollment() {
        if (!this.isAbleToAcceptWaitingEnrollment()) {
            return;
        }

        Enrollment enrollmentToAccept = this.getTheFirstWaitingEnrollment();

        if (enrollmentToAccept != null) {
            enrollmentToAccept.setAccepted(true);
        }
    }

    public void acceptWaitingList() {
        if (!this.isAbleToAcceptWaitingEnrollment()) {
            return;
        }

        List<Enrollment> waitingList = getWaitingList();
        int numberToAccept
                = (int) Math.min(this.limitOfEnrollments - this.getNumberOfAcceptedEnrollments(), waitingList.size());
        waitingList.subList(0, numberToAccept).forEach(enrollment -> {
            enrollment.setAccepted(true);
        });
    }

    private List<Enrollment> getWaitingList() {
        return this.enrollments.stream()
                .filter(enrollment -> !enrollment.isAccepted())
                .collect(Collectors.toList());
    }

    public void accept(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()) {
            enrollment.setAccepted(true);
        }
    }

    public void reject(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE) {
            enrollment.setAccepted(false);
        }
    }
}
