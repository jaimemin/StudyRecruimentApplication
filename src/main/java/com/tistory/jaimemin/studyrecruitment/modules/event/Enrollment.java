package com.tistory.jaimemin.studyrecruitment.modules.event;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author jaime
 * @title Enrollment
 * @see\n <pre>
 * </pre>
 * @since 2022-04-20
 */
@NamedEntityGraph(
        name = "Enrollment.withEventAndStudy",
        attributeNodes = {
                @NamedAttributeNode(value = "event", subgraph = "study")
        },
        subgraphs = @NamedSubgraph(name = "study", attributeNodes = @NamedAttributeNode("study"))
)
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
