package com.adopet.tutor;

import com.adopet.tutor.dto.PatchTutorForm;
import com.adopet.tutor.dto.Tutor;
import com.adopet.tutor.dto.TutorForm;
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

    public List<Tutor> getAllTutores() {
        List<TutorEntity> tutorEntityList = repository.findAll();
        return tutorEntityList.stream().map(entityToTutorMapper::map).toList();
    }

    public Tutor getTutorById(UUID id) {
        Optional<TutorEntity> tutorEntityOptional = repository.findById(id);
        return tutorEntityOptional.map(entityToTutorMapper::map).orElse(null);
    }

    public Tutor insertNewTutor(TutorForm tutorForm) {
        TutorEntity tutorEntity = formToTutorEntityMapper.map(tutorForm);
        TutorEntity saved = repository.save(tutorEntity);
        return entityToTutorMapper.map(saved);
    }

    public Tutor updateTutor(UUID id, TutorForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity old = optionalTutorEntity.get();
        TutorEntity updated = old.update(tutorForm.name(), tutorForm.email(), tutorForm.password());
        return entityToTutorMapper.map(repository.save(updated));
    }

    public Tutor patchTutor(UUID id, PatchTutorForm tutorForm) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return null;

        TutorEntity tutorEntity = optionalTutorEntity.get();
        TutorEntity updated = tutorEntity.update(tutorForm.name(), tutorForm.email(), tutorForm.password());

        return entityToTutorMapper.map(repository.save(updated));
    }


    public Optional<TutorEntity> deleteTutor(UUID id) {
        Optional<TutorEntity> optionalTutorEntity = repository.findById(id);
        if (optionalTutorEntity.isEmpty())
            return optionalTutorEntity;
        repository.deleteById(id);
        return optionalTutorEntity;
    }
}
