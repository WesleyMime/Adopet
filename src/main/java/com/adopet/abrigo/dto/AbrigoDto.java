package com.adopet.abrigo.dto;

import java.util.UUID;

public record AbrigoDto(UUID id, String name, String email, String phone, String location) {
}
