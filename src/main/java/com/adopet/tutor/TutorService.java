package com.adopet.tutor;

import com.adopet.MapStructMapper;
import com.adopet.exceptions.EmailAlreadyExistsException;
import com.adopet.tutor.dto.TutorDto;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TutorService {

    private final TutorRepository repository;

    private final MapStructMapper mapper;

    public List<TutorDto> getAllTutores() {
        List<TutorEntity> tutorEntityList = repository.findAll();
        return tutorEntityList.stream().map(mapper::toTutorDto).toList();
    }

    public TutorDto getTutorById(UUID id) {
        Optional<TutorEntity> tutorEntityOptional = repository.findById(id);
        return tutorEntityOptional.map(mapper::toTutorDto).orElse(null);
    }

    public TutorDto insertNewTutor(TutorForm tutorForm) {
        checkEmail(tutorForm.email());
        TutorEntity tutorEntity = mapper.toTutorEntity(tutorForm);
        TutorEntity saved = repository.save(tutorEntity);
        return mapper.toTutorDto(saved);
    }

    public TutorDto updateTutor(UUID id, TutorForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        checkEmail(tutorForm.email());
        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = mapper.updateTutorEntityFromForm(tutorForm, old);
        return mapper.toTutorDto(repository.save(updated));
    }

    public TutorDto patchTutor(UUID id, TutorPatchForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        checkEmail(tutorForm.email());
        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = mapper.updateTutorEntityFromPatchForm(tutorForm, old);
        return mapper.toTutorDto(repository.save(updated));
    }


    public TutorDto deleteTutor(UUID id) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;
        repository.deleteById(id);
        return mapper.toTutorDto(optionalTutorEntity.get());
    }

    private void checkEmail(String email) {
        if (repository.existsByEmail(email)) throw new EmailAlreadyExistsException();
    }
}
