package com.adopet.abrigo.mapper;

import com.adopet.Mapper;
import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.dto.AbrigoDTO;
import org.springframework.stereotype.Component;

@Component
public class AbrigoEntityToAbrigoMapper implements Mapper<AbrigoEntity, AbrigoDTO> {

    @Override
    public AbrigoDTO map(AbrigoEntity source) {
        return new AbrigoDTO(source.getId(), source.getName(), source.getPhone(), source.getLocation());
    }
}
