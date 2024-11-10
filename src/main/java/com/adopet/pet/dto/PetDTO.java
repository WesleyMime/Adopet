package com.adopet.pet.dto;

import java.util.UUID;

public record PetDTO(UUID id, UUID abrigo, String name, String description, Boolean adopted, String age,
                     String address, String image) {
}
