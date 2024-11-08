package com.adopet.abrigo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Size;

public record AbrigoPatchForm(@Size(min = 3, max = 100) String name,
                              @Size(min = 8, max = 20) String phone,
                              @Size(min = 8, max = 50) String location) {
    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}