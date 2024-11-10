package com.adopet.pet.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PetForm(@NotNull @Size(min = 3, max = 30) String name,
                      @NotNull @Size(min = 4, max = 20) String age,
                      @NotNull @Size(min = 8, max = 50) String address,
                      @NotNull UUID abrigo,
                      String description,
                      String image) {
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