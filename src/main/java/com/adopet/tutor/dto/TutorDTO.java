package com.adopet.tutor.dto;

import java.util.UUID;

public record TutorDTO(UUID id, String name, String email) {

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
