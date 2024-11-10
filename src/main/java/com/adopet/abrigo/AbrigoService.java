package com.adopet.abrigo;

import com.adopet.MapStructMapper;
import com.adopet.abrigo.dto.AbrigoDTO;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AbrigoService {

    private final AbrigoRepository repository;

    private final MapStructMapper mapper;

    public List<AbrigoDTO> getAllTutores() {
        List<AbrigoEntity> abrigoEntityList = repository.findAll();
        return abrigoEntityList.stream().map(mapper::abrigoEntityToAbrigoDto).toList();
    }

    public AbrigoDTO getTutorById(UUID id) {
        Optional<AbrigoEntity> abrigoEntityOptional = repository.findById(id);
        return abrigoEntityOptional.map(mapper::abrigoEntityToAbrigoDto).orElse(null);
    }

    public AbrigoDTO insertNewTutor(AbrigoForm AbrigoForm) {
        AbrigoEntity abrigoEntity = mapper.abrigoFormToAbrigoEntity(AbrigoForm);
        AbrigoEntity saved = repository.save(abrigoEntity);
        return mapper.abrigoEntityToAbrigoDto(saved);
    }

    public AbrigoDTO updateTutor(UUID id, AbrigoForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity old = optionalAbrigoEntity.get();
        AbrigoEntity updated = mapper.updateAbrigoEntityFromForm(AbrigoForm, old);
        return mapper.abrigoEntityToAbrigoDto(repository.save(updated));
    }

    public AbrigoDTO patchTutor(UUID id, AbrigoPatchForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity old = optionalAbrigoEntity.get();
        AbrigoEntity updated = mapper.updateAbrigoEntityFromPatchForm(AbrigoForm, old);
        return mapper.abrigoEntityToAbrigoDto(repository.save(updated));
    }

    public Optional<AbrigoDTO> deleteTutor(UUID id) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        AbrigoDTO abrigo = mapper.abrigoEntityToAbrigoDto(optionalAbrigoEntity.get());
        return Optional.of(abrigo);
    }
}
