package com.adopet.tutor.mapper;

import com.adopet.Mapper;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.TutorDTO;
import org.springframework.stereotype.Component;

@Component
public class TutorEntityToTutorMapper implements Mapper<TutorEntity, TutorDTO> {

    @Override
    public TutorDTO map(TutorEntity source) {
        return new TutorDTO(source.getId(), source.getName(), source.getEmail());
    }
}
