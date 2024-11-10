package com.adopet.pet.mapper;

import com.adopet.Mapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.pet.PetEntity;
import com.adopet.pet.dto.PetForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetFormToPetEntityMapper implements Mapper<PetForm, PetEntity> {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public PetEntity map(PetForm source) {
        AbrigoEntity abrigoEntity = abrigoRepository.findById(source.abrigo()).get();
        return new PetEntity(abrigoEntity, source.name(), source.description(), source.age(), source.address(), source.image());
    }
}
