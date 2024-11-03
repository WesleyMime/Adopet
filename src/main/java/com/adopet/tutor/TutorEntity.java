package com.adopet.tutor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "Tutor")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TutorEntity {

    public TutorEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public TutorEntity update(String name, String email, String password) {
        if (name != null)
            this.name = name;
        if (email != null)
            this.email = email;
        if (password != null)
            this.password = password;
        return this;
    }
}
