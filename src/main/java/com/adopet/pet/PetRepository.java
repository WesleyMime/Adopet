package com.adopet.pet;

import com.adopet.pet.dto.PetWithoutAbrigoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, UUID> {
    Page<PetEntity> findByAdoptedFalse(Pageable pageable);

    Page<PetWithoutAbrigoDto> findByAbrigoId(UUID id, Pageable pageable);
}
