package com.tistory.jaimemin.studyrecruitment.modules.account;

import com.querydsl.core.types.Predicate;
import com.tistory.jaimemin.studyrecruitment.modules.tag.Tag;
import com.tistory.jaimemin.studyrecruitment.modules.zone.Zone;

import java.util.Set;

/**
 * @author jaime
 * @title AccountPredicates
 * @see\n <pre>
 * </pre>
 * @since 2022-05-15
 */
public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;

        return account.zones.any().in(zones)
                .and(account.tags.any().in(tags));
    }
}
