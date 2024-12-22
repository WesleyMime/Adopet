package com.adopet.abrigo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AbrigoPatchForm(@Size(min = 3, max = 100) String name,
                              @Email String email,
                              @Size(min = 8) String password,
                              @Size(min = 8, max = 20) String phone,
                              @Size(min = 8, max = 50) String location) {
}
