package com.tistory.jaimemin.studyrecruitment.modules.study;

import com.tistory.jaimemin.studyrecruitment.modules.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jaime
 * @title StudyFactory
 * @see\n <pre>
 * </pre>
 * @since 2022-05-08
 */
@Component
@RequiredArgsConstructor
public class StudyFactory {

    @Autowired
    StudyService studyService;

    public Study createStudy(String path, Account manager) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study, manager);

        return study;
    }
}
