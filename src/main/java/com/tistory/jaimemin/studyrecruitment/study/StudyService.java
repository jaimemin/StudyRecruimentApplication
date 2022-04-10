package com.tistory.jaimemin.studyrecruitment.study;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title StudyService
 * @see\n <pre>
 * </pre>
 * @since 2022-04-10
 */
@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);

        return newStudy;
    }
}
