package com.adopet.pet.dto;

import java.util.UUID;

public record PetDto(UUID id, UUID abrigo, String name, String size, String description, Boolean adopted, String age,
                     String address, String image) {
}
