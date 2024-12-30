package com.adopet.pet.dto;

import java.util.UUID;

public record PetWithoutAbrigoDto(UUID id, String name, String size, String description, Boolean adopted, String age,
                                  String address, String image) {
}
