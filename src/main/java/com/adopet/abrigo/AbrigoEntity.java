package com.adopet.abrigo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "Abrigo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbrigoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String location;

    public AbrigoEntity(String name, String phone, String location) {
        this.name = name;
        this.phone = phone;
        this.location = location;
    }
}
