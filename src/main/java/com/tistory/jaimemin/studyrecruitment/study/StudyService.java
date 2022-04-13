package com.tistory.jaimemin.studyrecruitment.study;

import com.tistory.jaimemin.studyrecruitment.domain.Account;
import com.tistory.jaimemin.studyrecruitment.domain.Study;
import com.tistory.jaimemin.studyrecruitment.study.form.StudyDescriptionForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
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

    private final ModelMapper modelMapper;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);

        return newStudy;
    }

    public Study getStudyToUpdate(Account account, String path) {
        Study study = this.getStudy(path);

        if (!account.isManagerOf(study)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }

        return study;
    }

    public Study getStudy(String path) {
        Study study = studyRepository.findByPath(path);

        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }

        return study;
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm, study);
    }

    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }

    public void updateStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void disableStudyBanner(Study study) {
        study.setUseBanner(false);
    }
}
