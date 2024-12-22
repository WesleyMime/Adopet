package com.adopet.tutor;

import com.adopet.user.Roles;
import com.adopet.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "Tutor")
@Getter
@Setter
public class TutorEntity extends UserEntity {

    @Column(nullable = false, length = 100)
    private String name;

    public TutorEntity() {
        this.setAuthorities(List.of(Roles.TUTOR));
    }

    public TutorEntity(String name, String email, String password) {
        super(email, password, List.of(Roles.TUTOR));
        this.name = name;
    }
}
