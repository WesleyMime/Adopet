package com.adopet.pet;

import com.adopet.MapStructMapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.pet.dto.PetDTO;
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

    public List<PetDTO> getAllPets() {
        List<PetEntity> petEntityList = repository.findAll();
        return petEntityList.stream().map(mapper::petEntityToPetDto).toList();
    }

    public PetDTO getPetById(UUID id) {
        Optional<PetEntity> PetEntityOptional = repository.findById(id);
        return PetEntityOptional.map(mapper::petEntityToPetDto).orElse(null);
    }

    public PetDTO insertNewPet(PetForm petForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            throw new NoSuchElementException();

        PetEntity petEntity = mapper.petFormToPetEntity(petForm, optionalAbrigoEntity.get());
        return mapper.petEntityToPetDto(repository.save(petEntity));
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
        PetEntity updated = mapper.updatePetEntityFromForm(petForm, abrigoEntity,petEntity);
        return mapper.petEntityToPetDto(repository.save(updated));
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
        PetEntity updated = mapper.updatePetEntityFromPatchForm(petForm, abrigoEntity, petEntity);
        return mapper.petEntityToPetDto(repository.save(updated));
    }

    public Optional<PetDTO> deletePet(UUID id) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        PetDTO Pet = mapper.petEntityToPetDto(optionalPetEntity.get());
        return Optional.of(Pet);
    }
}
