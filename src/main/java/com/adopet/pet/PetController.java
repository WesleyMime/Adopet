package com.adopet.pet;

import com.adopet.CrudController;
import com.adopet.pet.dto.PetDTO;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetController implements CrudController<PetDTO, PetForm, PetPatchForm> {

    private final PetService service;

    public ResponseEntity<List<PetDTO>> getAll() {
        List<PetDTO> all = service.getAllPets();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<PetDTO> getById(UUID id) {
        PetDTO petById = service.getPetById(id);
        return ResponseEntity.ofNullable(petById);
    }

    public ResponseEntity<PetDTO> insertNew(PetForm PetForm) {
        PetDTO pet = service.insertNewPet(PetForm);
        return ResponseEntity.created(URI.create("/pets/" + pet.id().toString())).body(pet);
    }

    public ResponseEntity<PetDTO> update(UUID id, PetForm PetForm) {
        PetDTO updatedPet= service.updatePet(id, PetForm);
        return ResponseEntity.ofNullable(updatedPet);
    }

    public ResponseEntity<PetDTO> patch(UUID id, PetPatchForm AbrigoForm) {
        PetDTO patchedPet= service.patchPet(id, AbrigoForm);
        return ResponseEntity.ofNullable(patchedPet);
    }

    public ResponseEntity<PetDTO> delete(UUID id) {
        Optional<PetDTO> pet = service.deletePet(id);
        return ResponseEntity.of(pet);
    }
}
