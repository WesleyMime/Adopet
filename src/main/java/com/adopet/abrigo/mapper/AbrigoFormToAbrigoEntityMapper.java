package com.adopet.abrigo.mapper;

import com.adopet.Mapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.dto.AbrigoForm;
import org.springframework.stereotype.Component;

@Component
public class AbrigoFormToAbrigoEntityMapper implements Mapper<AbrigoForm, AbrigoEntity> {

    @Override
    public AbrigoEntity map(AbrigoForm source) {
        return new AbrigoEntity(source.name(), source.phone(), source.location());
    }
}
