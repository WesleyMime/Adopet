package com.adopet.abrigo;

import com.adopet.abrigo.dto.AbrigoDTO;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import com.adopet.abrigo.mapper.AbrigoEntityToAbrigoMapper;
import com.adopet.abrigo.mapper.AbrigoFormToAbrigoEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AbrigoService {

    private final AbrigoRepository repository;

    private final AbrigoEntityToAbrigoMapper entityToAbrigoMapper;

    private final AbrigoFormToAbrigoEntityMapper formToAbrigoEntityMapper;

    public List<AbrigoDTO> getAllTutores() {
        List<AbrigoEntity> abrigoEntityList = repository.findAll();
        return abrigoEntityList.stream().map(entityToAbrigoMapper::map).toList();
    }

    public AbrigoDTO getTutorById(UUID id) {
        Optional<AbrigoEntity> abrigoEntityOptional = repository.findById(id);
        return abrigoEntityOptional.map(entityToAbrigoMapper::map).orElse(null);
    }

    public AbrigoDTO insertNewTutor(AbrigoForm AbrigoForm) {
        AbrigoEntity abrigoEntity = formToAbrigoEntityMapper.map(AbrigoForm);
        AbrigoEntity saved = repository.save(abrigoEntity);
        return entityToAbrigoMapper.map(saved);
    }

    public AbrigoDTO updateTutor(UUID id, AbrigoForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity old = optionalAbrigoEntity.get();
        AbrigoEntity updated = old.update(AbrigoForm.name(), AbrigoForm.phone(), AbrigoForm.location());
        return entityToAbrigoMapper.map(repository.save(updated));
    }

    public AbrigoDTO patchTutor(UUID id, AbrigoPatchForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity abrigoEntity = optionalAbrigoEntity.get();
        AbrigoEntity updated = abrigoEntity.update(AbrigoForm.name(), AbrigoForm.phone(), AbrigoForm.location());

        return entityToAbrigoMapper.map(repository.save(updated));
    }

    public Optional<AbrigoDTO> deleteTutor(UUID id) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return Optional.empty();
        repository.deleteById(id);
        AbrigoDTO abrigo = entityToAbrigoMapper.map(optionalAbrigoEntity.get());
        return Optional.of(abrigo);
    }
}
