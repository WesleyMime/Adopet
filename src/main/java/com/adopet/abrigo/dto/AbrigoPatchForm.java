package com.adopet.abrigo.dto;

import jakarta.validation.constraints.Size;

public record AbrigoPatchForm(@Size(min = 3, max = 100) String name,
                              @Size(min = 8, max = 20) String phone,
                              @Size(min = 8, max = 50) String location) {
}
