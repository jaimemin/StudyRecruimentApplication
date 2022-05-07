package com.tistory.jaimemin.studyrecruitment.modules.zone;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jaime
 * @title ZoneRepository
 * @see\n <pre>
 * </pre>
 * @since 2022-04-07
 */
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Zone findByCityAndProvince(String cityName, String provinceName);
}
