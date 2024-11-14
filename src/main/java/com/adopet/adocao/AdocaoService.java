package com.adopet.adocao;

import com.adopet.MapStructMapper;
import com.adopet.adocao.dto.AdocaoDto;
import com.adopet.adocao.dto.AdocaoForm;
import com.adopet.exceptions.InvalidPetException;
import com.adopet.exceptions.InvalidTutorException;
import com.adopet.exceptions.PetAlreadyAdoptedException;
import com.adopet.pet.PetEntity;
import com.adopet.pet.PetRepository;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.TutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdocaoService {

    private AdocaoRepository repository;

    private PetRepository petRepository;

    private TutorRepository tutorRepository;

    private MapStructMapper mapper;

    @Transactional
    public AdocaoDto insertNew(AdocaoForm adocaoForm) {
        Optional<PetEntity> optionalPet = petRepository.findById(adocaoForm.pet());
        Optional<TutorEntity> optionalTutor = tutorRepository.findById(adocaoForm.tutor());
        if (optionalTutor.isEmpty()) throw new InvalidTutorException();
        if (optionalPet.isEmpty()) throw new InvalidPetException();

        PetEntity petEntity = optionalPet.get();
        TutorEntity tutorEntity = optionalTutor.get();

        if (petEntity.getAdopted()) throw new PetAlreadyAdoptedException();
        petEntity.setAdopted(true);
        petRepository.save(petEntity);
        AdocaoEntity adocaoEntity = mapper.toAdocaoEntity(tutorEntity, petEntity);
        adocaoEntity = repository.save(adocaoEntity);
        return mapper.toAdocaoDto(adocaoEntity);
    }

    public AdocaoDto deleteAdocao(UUID id) {
        Optional<AdocaoEntity> optionalAdocaoEntity = repository.findById(id);
        if (optionalAdocaoEntity.isEmpty())
            return null;
        repository.deleteById(id);
        return mapper.toAdocaoDto(optionalAdocaoEntity.get());
    }
}
