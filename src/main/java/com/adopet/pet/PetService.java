package com.adopet.pet;

import com.adopet.MapStructMapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.pet.dto.PetDto;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PetService {

    private final PetRepository repository;

    private final AbrigoRepository abrigoRepository;

    private final MapStructMapper mapper;

    public List<PetDto> getAllPets() {
        List<PetEntity> petEntityList = repository.findAll();
        return petEntityList.stream().map(mapper::toPetDto).toList();
    }

    public PetDto getPetById(UUID id) {
        Optional<PetEntity> PetEntityOptional = repository.findById(id);
        return PetEntityOptional.map(mapper::toPetDto).orElse(null);
    }

    public PetDto insertNewPet(PetForm petForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            throw new NoSuchElementException();

        PetEntity petEntity = mapper.toPetEntity(petForm, optionalAbrigoEntity.get());
        return mapper.toPetDto(repository.save(petEntity));
    }

    public PetDto updatePet(UUID id, PetForm petForm) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return null;

        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            return null;

        PetEntity petEntity = optionalPetEntity.get();
        AbrigoEntity abrigoEntity = optionalAbrigoEntity.get();
        PetEntity updated = mapper.updatePetEntityFromForm(petForm, abrigoEntity,petEntity);
        return mapper.toPetDto(repository.save(updated));
    }

    public PetDto patchPet(UUID id, PetPatchForm petForm) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return null;

        AbrigoEntity abrigoEntity = null;
        if (petForm.abrigo() != null) {
            Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
            if (optionalAbrigoEntity.isEmpty())
                return null;
            abrigoEntity = optionalAbrigoEntity.get();
        }
        PetEntity petEntity = optionalPetEntity.get();
        PetEntity updated = mapper.updatePetEntityFromPatchForm(petForm, abrigoEntity, petEntity);
        return mapper.toPetDto(repository.save(updated));
    }

    public PetDto deletePet(UUID id) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return null;
        repository.deleteById(id);
        return mapper.toPetDto(optionalPetEntity.get());
    }
}
