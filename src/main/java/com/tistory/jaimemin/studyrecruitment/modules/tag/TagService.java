package com.tistory.jaimemin.studyrecruitment.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title TagService
 * @see\n <pre>
 * </pre>
 * @since 2022-04-13
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    
    private final TagRepository tagRepository;

    public Tag findOrCreateNewTag(String tagTitle) {
        Tag tag = tagRepository.findByTitle(tagTitle).orElse(null);

        if (tag == null) {
            tag = tagRepository.save(Tag.builder()
                    .title(tagTitle)
                    .build());
        }

        return tag;
    }
}
