package com.adopet.tutor.mapper;

import com.adopet.Mapper;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.Tutor;
import org.springframework.stereotype.Component;

@Component
public class TutorEntityToTutorMapper implements Mapper<TutorEntity, Tutor> {

    @Override
    public Tutor map(TutorEntity source) {
        return new Tutor(source.getId(), source.getName(), source.getEmail());
    }
}
