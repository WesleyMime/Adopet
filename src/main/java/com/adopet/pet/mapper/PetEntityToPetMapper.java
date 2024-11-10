package com.adopet.pet.mapper;

import com.adopet.Mapper;
import com.adopet.pet.PetEntity;
import com.adopet.pet.dto.PetDTO;
import org.springframework.stereotype.Component;

@Component
public class PetEntityToPetMapper implements Mapper<PetEntity, PetDTO> {

    @Override
    public PetDTO map(PetEntity source) {
        return new PetDTO(source.getId(), source.getAbrigo().getId(), source.getName(), source.getDescription(),
                source.getAdopted(), source.getAge(), source.getAddress(), source.getImage());
    }
}
