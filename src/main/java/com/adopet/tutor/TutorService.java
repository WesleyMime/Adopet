package com.adopet.tutor;

import com.adopet.tutor.dto.TutorDTO;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import com.adopet.tutor.mapper.TutorEntityToTutorMapper;
import com.adopet.tutor.mapper.TutorFormToTutorEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TutorService {

    private final TutorRepository repository;

    private final TutorEntityToTutorMapper entityToTutorMapper;

    private final TutorFormToTutorEntityMapper formToTutorEntityMapper;

    public List<TutorDTO> getAllTutores() {
        List<TutorEntity> tutorEntityList = repository.findAll();
        return tutorEntityList.stream().map(entityToTutorMapper::map).toList();
    }

    public TutorDTO getTutorById(UUID id) {
        Optional<TutorEntity> tutorEntityOptional = repository.findById(id);
        return tutorEntityOptional.map(entityToTutorMapper::map).orElse(null);
    }

    public TutorDTO insertNewTutor(TutorForm tutorForm) {
        TutorEntity tutorEntity = formToTutorEntityMapper.map(tutorForm);
        TutorEntity saved = repository.save(tutorEntity);
        return entityToTutorMapper.map(saved);
    }

    public TutorDTO updateTutor(UUID id, TutorForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = old.update(tutorForm.name(), tutorForm.email(), tutorForm.password());
        return entityToTutorMapper.map(repository.save(updated));
    }

    public TutorDTO patchTutor(UUID id, TutorPatchForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity tutorEntity = optionalTutorEntity.get();
        TutorEntity updated = tutorEntity.update(tutorForm.name(), tutorForm.email(), tutorForm.password());

        return entityToTutorMapper.map(repository.save(updated));
    }


    public Optional<TutorDTO> deleteTutor(UUID id) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        TutorDTO tutor = entityToTutorMapper.map(optionalTutorEntity.get());
        return Optional.of(tutor);
    }
}
