package com.adopet;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.dto.AbrigoDTO;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import com.adopet.pet.PetEntity;
import com.adopet.pet.dto.PetDTO;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.TutorDTO;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStructMapper {

    // Tutor
    TutorDTO tutorEntityToTutorDto(TutorEntity tutorEntity);

    TutorEntity tutorFormToTutorEntity(TutorForm tutorForm);

    TutorEntity updateTutorEntityFromForm(
            TutorForm tutorForm, @MappingTarget TutorEntity tutorEntity);

    TutorEntity updateTutorEntityFromPatchForm(
            TutorPatchForm tutorPatchForm, @MappingTarget TutorEntity tutorEntity);

    // Abrigo
    AbrigoDTO abrigoEntityToAbrigoDto(AbrigoEntity abrigoEntity);

    AbrigoEntity abrigoFormToAbrigoEntity(AbrigoForm abrigoForm);

    AbrigoEntity updateAbrigoEntityFromForm(
            AbrigoForm abrigoForm, @MappingTarget AbrigoEntity abrigoEntity);

    AbrigoEntity updateAbrigoEntityFromPatchForm(
            AbrigoPatchForm abrigoPatchForm, @MappingTarget AbrigoEntity abrigoEntity);

    default UUID map(AbrigoEntity abrigoEntity) {
        return abrigoEntity.getId();
    }

    // Pet
    PetDTO petEntityToPetDto(PetEntity petEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity petFormToPetEntity(PetForm petForm, AbrigoEntity abrigoEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity updatePetEntityFromForm(PetForm petForm, AbrigoEntity abrigoEntity, @MappingTarget PetEntity petEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity updatePetEntityFromPatchForm(PetPatchForm petForm, AbrigoEntity abrigoEntity, @MappingTarget PetEntity petEntity);
}
