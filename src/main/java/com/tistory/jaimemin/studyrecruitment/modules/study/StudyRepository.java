package com.tistory.jaimemin.studyrecruitment.modules.study;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import com.tistory.jaimemin.studyrecruitment.modules.tag.Tag;
import com.tistory.jaimemin.studyrecruitment.modules.zone.Zone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryExtension {

    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Study findByPath(String path);

    @EntityGraph(value = "Study.withTagsAndManagers", type = EntityGraph.EntityGraphType.LOAD)
    Study findStudyWithTagsByPath(String path);

    @EntityGraph(value = "Study.withZonesAndManagers", type = EntityGraph.EntityGraphType.LOAD)
    Study findStudyWithZonesByPath(String path);

    @EntityGraph(value = "Study.withManagers", type = EntityGraph.EntityGraphType.LOAD)
    Study findStudyWithManagersByPath(String path);

    @EntityGraph(value = "Study.withMembers", type = EntityGraph.EntityGraphType.LOAD)
    Study findStudyWithMembersByPath(String path);

    @EntityGraph(value = "Study.withTagsAndZones", type = EntityGraph.EntityGraphType.LOAD)
    Study findStudyWithTagsAndZonesById(Long id);

    @EntityGraph(attributePaths = {"members", "managers"})
    Study findStudyWithManagersAndMembersById(Long id);

    @EntityGraph(attributePaths = {"zones", "tags"})
    List<Study> findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean published, boolean closed);

    Study findStudyOnlyByPath(String path);

    List<Study> findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);

    List<Study> findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);
}
