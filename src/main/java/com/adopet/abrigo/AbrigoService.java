package com.adopet.abrigo;

import com.adopet.MapStructMapper;
import com.adopet.abrigo.dto.AbrigoDto;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AbrigoService {

    private final AbrigoRepository repository;

    private final MapStructMapper mapper;

    public PagedModel<AbrigoDto> getAllAbrigos(Integer page) {
        Page<AbrigoEntity> abrigoEntityPage = repository.findAll(
                Pageable.ofSize(10).withPage(page));
        return new PagedModel<>(abrigoEntityPage.map(mapper::toAbrigoDto));
    }

    public AbrigoDto getAbrigoById(UUID id) {
        Optional<AbrigoEntity> abrigoEntityOptional = repository.findById(id);
        return abrigoEntityOptional.map(mapper::toAbrigoDto).orElse(null);
    }

    public AbrigoDto insertNewAbrigo(AbrigoForm AbrigoForm) {
        AbrigoEntity abrigoEntity = mapper.toAbrigoEntity(AbrigoForm);
        AbrigoEntity saved = repository.save(abrigoEntity);
        return mapper.toAbrigoDto(saved);
    }

    public AbrigoDto updateAbrigo(UUID id, AbrigoForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity old = optionalAbrigoEntity.get();
        AbrigoEntity updated = mapper.updateAbrigoEntityFromForm(AbrigoForm, old);
        return mapper.toAbrigoDto(repository.save(updated));
    }

    public AbrigoDto patchAbrigo(UUID id, AbrigoPatchForm AbrigoForm) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;

        AbrigoEntity old = optionalAbrigoEntity.get();
        AbrigoEntity updated = mapper.updateAbrigoEntityFromPatchForm(AbrigoForm, old);
        return mapper.toAbrigoDto(repository.save(updated));
    }

    public AbrigoDto deleteAbrigo(UUID id) {
        Optional<AbrigoEntity> optionalAbrigoEntity = repository.findById(id);
        if (optionalAbrigoEntity.isEmpty())
            return null;
        repository.deleteById(id);
        return mapper.toAbrigoDto(optionalAbrigoEntity.get());
    }
}
