package com.adopet;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.dto.AbrigoDto;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import com.adopet.adocao.AdocaoEntity;
import com.adopet.adocao.dto.AdocaoDto;
import com.adopet.pet.PetEntity;
import com.adopet.pet.dto.PetDto;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.dto.TutorDto;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@DecoratedWith(EncodeDecorator.class)
@Primary
public interface MapStructMapper {

    // Tutor
    TutorDto toTutorDto(TutorEntity tutorEntity);

    TutorEntity toTutorEntity(TutorForm tutorForm);

    TutorEntity updateTutorEntityFromForm(
            TutorForm tutorForm, @MappingTarget TutorEntity tutorEntity);

    TutorEntity updateTutorEntityFromPatchForm(
            TutorPatchForm tutorPatchForm, @MappingTarget TutorEntity tutorEntity);

    // Abrigo
    AbrigoDto toAbrigoDto(AbrigoEntity abrigoEntity);

    AbrigoEntity toAbrigoEntity(AbrigoForm abrigoForm);

    AbrigoEntity updateAbrigoEntityFromForm(
            AbrigoForm abrigoForm, @MappingTarget AbrigoEntity abrigoEntity);

    AbrigoEntity updateAbrigoEntityFromPatchForm(
            AbrigoPatchForm abrigoPatchForm, @MappingTarget AbrigoEntity abrigoEntity);

    default UUID map(AbrigoEntity abrigoEntity) {
        return abrigoEntity.getId();
    }

    // Pet
    PetDto toPetDto(PetEntity petEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity toPetEntity(PetForm petForm, AbrigoEntity abrigoEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity updatePetEntityFromForm(PetForm petForm, AbrigoEntity abrigoEntity, @MappingTarget PetEntity petEntity);

    @Mapping(target = "name", source = "petForm.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abrigo", source = "abrigoEntity")
    PetEntity updatePetEntityFromPatchForm(PetPatchForm petForm, AbrigoEntity abrigoEntity, @MappingTarget PetEntity petEntity);

    // Adocao
    @Mapping(target = "pet", source = "pet.id")
    @Mapping(target = "tutor", source = "tutor.id")
    AdocaoDto toAdocaoDto(AdocaoEntity adocaoEntity);

    @Mapping(target = "pet", source = "petEntity")
    @Mapping(target = "tutor", source = "tutorEntity")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "id", ignore = true)
    AdocaoEntity toAdocaoEntity(TutorEntity tutorEntity, PetEntity petEntity);
}
