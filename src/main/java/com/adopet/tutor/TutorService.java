package com.adopet.tutor;

import com.adopet.MapStructMapper;
import com.adopet.tutor.dto.TutorDTO;
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

    public List<TutorDTO> getAllTutores() {
        List<TutorEntity> tutorEntityList = repository.findAll();
        return tutorEntityList.stream().map(mapper::tutorEntityToTutorDto).toList();
    }

    public TutorDTO getTutorById(UUID id) {
        Optional<TutorEntity> tutorEntityOptional = repository.findById(id);
        return tutorEntityOptional.map(mapper::tutorEntityToTutorDto).orElse(null);
    }

    public TutorDTO insertNewTutor(TutorForm tutorForm) {
        TutorEntity tutorEntity = mapper.tutorFormToTutorEntity(tutorForm);
        TutorEntity saved = repository.save(tutorEntity);
        return mapper.tutorEntityToTutorDto(saved);
    }

    public TutorDTO updateTutor(UUID id, TutorForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = mapper.updateTutorEntityFromForm(tutorForm, old);
        return mapper.tutorEntityToTutorDto(repository.save(updated));
    }

    public TutorDTO patchTutor(UUID id, TutorPatchForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = mapper.updateTutorEntityFromPatchForm(tutorForm, old);
        return mapper.tutorEntityToTutorDto(repository.save(updated));
    }


    public Optional<TutorDTO> deleteTutor(UUID id) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        TutorDTO tutor = mapper.tutorEntityToTutorDto(optionalTutorEntity.get());
        return Optional.of(tutor);
    }
}
