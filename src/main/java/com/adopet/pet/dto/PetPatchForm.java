package com.adopet.pet.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PetPatchForm(@Size(min = 3, max = 30) String name,
                           @Size(min = 4, max = 20) String age,
                           @Size(min = 8, max = 50) String address,
                           UUID abrigo,
                           String description,
                           String image) {
}
