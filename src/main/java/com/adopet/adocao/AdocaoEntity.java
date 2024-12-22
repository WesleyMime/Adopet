package com.adopet.adocao;

import com.adopet.pet.PetEntity;
import com.adopet.tutor.TutorEntity;
import com.adopet.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "adocao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdocaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    private UserEntity tutor;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    public AdocaoEntity(PetEntity pet, TutorEntity tutor) {
        this.pet = pet;
        this.tutor = tutor;
    }
}
