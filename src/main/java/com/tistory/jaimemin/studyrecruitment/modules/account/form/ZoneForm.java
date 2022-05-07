package com.tistory.jaimemin.studyrecruitment.modules.account.form;

import com.tistory.jaimemin.studyrecruitment.modules.zone.Zone;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jaime
 * @title ZoneForm
 * @see\n <pre>
 * </pre>
 * @since 2022-04-07
 */
@Getter
@Setter
public class ZoneForm {

    private String zoneName;

    public String getCityName() {
        return zoneName.substring(0, zoneName.indexOf("("));
    }

    public String getProvinceName() {
        return zoneName.substring(zoneName.indexOf("/") + 1);
    }

    public String getLocalNameOfCity() {
        return zoneName.substring(zoneName.indexOf("(") + 1, zoneName.indexOf(")"));
    }

    public Zone getZone() {
        return Zone.builder()
                .city(this.getCityName())
                .localNameOfCity(this.getLocalNameOfCity())
                .province(this.getProvinceName())
                .build();
    }
}
