package com.adopet.pet;

import com.adopet.MapStructMapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.exceptions.InvalidAbrigoException;
import com.adopet.pet.dto.PetDto;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import com.adopet.pet.dto.PetWithoutAbrigoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PetService {

    private final PetRepository repository;

    private final AbrigoRepository abrigoRepository;

    private final MapStructMapper mapper;

    public PagedModel<PetDto> getAllPets(Integer page) {
        Page<PetEntity> petEntityPage = repository.findAll(
                Pageable.ofSize(10).withPage(page));
        return new PagedModel<>(petEntityPage.map(mapper::toPetDto));
    }

    public PagedModel<PetWithoutAbrigoDto> getAllPetsByAbrigoId(UUID id, Integer page) {
        Page<PetWithoutAbrigoDto> petEntityPage = repository.findByAbrigoId(id,
                Pageable.ofSize(10).withPage(page));
        return new PagedModel<>(petEntityPage);
    }

    public PagedModel<PetDto> getNotAdopted(Integer page) {
        Page<PetEntity> petEntityPage = repository.findByAdoptedFalse(
                Pageable.ofSize(10).withPage(page));
        return new PagedModel<>(petEntityPage.map(mapper::toPetDto));
    }

    public PetDto getPetById(UUID id) {
        Optional<PetEntity> PetEntityOptional = repository.findById(id);
        return PetEntityOptional.map(mapper::toPetDto).orElse(null);
    }

    public PetDto insertNewPet(PetForm petForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            throw new InvalidAbrigoException();

        PetEntity petEntity = mapper.toPetEntity(petForm, optionalAbrigoEntity.get());
        return mapper.toPetDto(repository.save(petEntity));
    }

    public PetDto updatePet(UUID id, PetForm petForm) {
        Optional<PetEntity> optionalPetEntity = repository.findById(id);
        if (optionalPetEntity.isEmpty())
            return null;

        Optional<AbrigoEntity> optionalAbrigoEntity = abrigoRepository.findById(petForm.abrigo());
        if (optionalAbrigoEntity.isEmpty())
            throw new InvalidAbrigoException();

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
                throw new InvalidAbrigoException();
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
