package com.adopet.tutor.mapper;

import com.adopet.Mapper;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.TutorForm;
import org.springframework.stereotype.Component;

@Component
public class TutorFormToTutorEntityMapper implements Mapper<TutorForm, TutorEntity> {

    @Override
    public TutorEntity map(TutorForm source) {
        return new TutorEntity(source.name(), source.email(), source.password());
    }
}
