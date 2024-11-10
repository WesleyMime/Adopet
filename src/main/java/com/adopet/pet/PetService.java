package com.adopet.pet;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.pet.dto.PetDTO;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import com.adopet.pet.mapper.PetEntityToPetMapper;
import com.adopet.pet.mapper.PetFormToPetEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PetService {

    private final PetRepository repository;

    private final AbrigoRepository abrigoRepository;


    private final PetEntityToPetMapper entityToPetMapper;

    private final PetFormToPetEntityMapper formToPetEntityMapper;

    public List<PetDTO> getAllPets() {
        List<PetEntity> petEntityList = repository.findAll();
        return petEntityList.stream().map(entityToPetMapper::map).toList();
    }

    public PetDTO getPetById(UUID id) {
        Optional<PetEntity> PetEntityOptional = repository.findById(id);
        return PetEntityOptional.map(entityToPetMapper::map).orElse(null);
    }

    public PetDTO insertNewPet(PetForm petForm) {
        PetEntity petEntity = formToPetEntityMapper.map(petForm);
        PetEntity saved = repository.save(petEntity);
        return entityToPetMapper.map(saved);
    }

    public PetDTO updatePet(UUID id, PetForm petForm) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return null;

        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            return null;

        PetEntity petEntity = optionalPetEntity.get();
        AbrigoEntity abrigoEntity = optionalAbrigoEntity.get();
        PetEntity updated = petEntity.update(abrigoEntity, petForm.name(), petForm.description(), petForm.age(), petForm.address(), petForm.image());
        return entityToPetMapper.map(repository.save(updated));
    }

    public PetDTO patchPet(UUID id, PetPatchForm petForm) {
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
        PetEntity updated = petEntity.update(abrigoEntity, petForm.name(), petForm.description(), petForm.age(), petForm.address(), petForm.image());

        return entityToPetMapper.map(repository.save(updated));
    }

    public Optional<PetDTO> deletePet(UUID id) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        PetDTO Pet = entityToPetMapper.map(optionalPetEntity.get());
        return Optional.of(Pet);
    }
}
