package com.adopet;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public abstract class EncodeDecorator implements MapStructMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("delegate")
    private MapStructMapper delegate;

    @Override
    public TutorEntity toTutorEntity(TutorForm tutorForm) {
        TutorEntity tutorEntity = delegate.toTutorEntity(tutorForm);
        tutorEntity.setPassword(passwordEncoder.encode(tutorEntity.getPassword()));
        return tutorEntity;
    }

    @Override
    public TutorEntity updateTutorEntityFromForm(TutorForm tutorForm, TutorEntity tutorEntity) {
        TutorEntity newTutorEntity = delegate.updateTutorEntityFromForm(tutorForm, tutorEntity);
        newTutorEntity.setPassword(passwordEncoder.encode(newTutorEntity.getPassword()));
        return newTutorEntity;
    }

    @Override
    public TutorEntity updateTutorEntityFromPatchForm(
            TutorPatchForm tutorPatchForm, TutorEntity tutorEntity) {
        TutorEntity newTutorEntity = delegate.updateTutorEntityFromPatchForm(tutorPatchForm, tutorEntity);
        newTutorEntity.setPassword(passwordEncoder.encode(newTutorEntity.getPassword()));
        return newTutorEntity;
    }

    @Override
    public AbrigoEntity toAbrigoEntity(AbrigoForm abrigoForm) {
        AbrigoEntity abrigoEntity = delegate.toAbrigoEntity(abrigoForm);
        abrigoEntity.setPassword(passwordEncoder.encode(abrigoEntity.getPassword()));
        return abrigoEntity;
    }

    @Override
    public AbrigoEntity updateAbrigoEntityFromForm(
            AbrigoForm abrigoForm, AbrigoEntity abrigoEntity) {
        AbrigoEntity newAbrigoEntity = delegate.updateAbrigoEntityFromForm(abrigoForm, abrigoEntity);
        newAbrigoEntity.setPassword(passwordEncoder.encode(newAbrigoEntity.getPassword()));
        return newAbrigoEntity;
    }

    @Override
    public AbrigoEntity updateAbrigoEntityFromPatchForm(
            AbrigoPatchForm abrigoPatchForm, AbrigoEntity abrigoEntity) {
        AbrigoEntity newAbrigoEntity = delegate.updateAbrigoEntityFromPatchForm(abrigoPatchForm, abrigoEntity);
        newAbrigoEntity.setPassword(passwordEncoder.encode(newAbrigoEntity.getPassword()));
        return newAbrigoEntity;
    }
}
