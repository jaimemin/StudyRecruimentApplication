package com.tistory.jaimemin.studyrecruitment.modules.study;

import com.querydsl.jpa.JPQLQuery;
import com.tistory.jaimemin.studyrecruitment.modules.account.QAccount;
import com.tistory.jaimemin.studyrecruitment.modules.tag.QTag;
import com.tistory.jaimemin.studyrecruitment.modules.zone.QZone;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * @author jaime
 * @title StudyRepositoryExtensionImpl
 * @see\n <pre>
 * </pre>
 * @since 2022-05-18
 */
public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.title.containsIgnoreCase(keyword))
                .or(study.tags.any().title.containsIgnoreCase(keyword))
                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QTag.tag)
                .fetchJoin()
                .leftJoin(study.zones, QZone.zone)
                .fetchJoin()
                .leftJoin(study.members, QAccount.account)
                .fetchJoin()
                .distinct();

        return query.fetch();
    }
}
