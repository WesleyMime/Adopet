package com.adopet.tutor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TutorForm(@NotNull @Size(min = 3, max = 30) String name,
                        @NotNull @Email String email,
                        @NotNull @Size(min = 8, max = 200) String password) {
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"").append(name).append("\"");
        sb.append(", \"email\":\"").append(email).append("\"");
        sb.append(", \"password\":\"").append(password).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
