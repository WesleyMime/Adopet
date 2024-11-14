package com.adopet.adocao.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AdocaoDto(UUID id, UUID pet, UUID tutor, LocalDate date) {
}
