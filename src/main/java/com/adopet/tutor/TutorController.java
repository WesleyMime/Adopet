package com.adopet.tutor;

import com.adopet.tutor.dto.TutorDTO;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tutores")
@AllArgsConstructor
public class TutorController {

    private final TutorService service;

    @GetMapping
    public ResponseEntity<List<TutorDTO>> getAllTutores() {
        List<TutorDTO> all = service.getAllTutores();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorDTO> getTutorById(@PathVariable(name = "id") UUID id) {
        TutorDTO tutorById = service.getTutorById(id);
        return ResponseEntity.ofNullable(tutorById);
    }

    @PostMapping
    public ResponseEntity<?> insertNewTutor(@RequestBody @Valid TutorForm tutorForm) {
        TutorDTO tutor = service.insertNewTutor(tutorForm);
        return ResponseEntity.created(URI.create("/tutores/" + tutor.id().toString())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorDTO> updateTutor(@PathVariable(name = "id") UUID id,
                                              @RequestBody @Valid TutorForm tutorForm) {
        TutorDTO updatedTutor= service.updateTutor(id, tutorForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TutorDTO> patchTutor(@PathVariable(name = "id") UUID id,
                                             @RequestBody @Valid TutorPatchForm tutorForm) {
        TutorDTO patchedTutor= service.patchTutor(id, tutorForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TutorDTO> deleteTutor(@PathVariable(name = "id") UUID id) {
        Optional<TutorDTO> tutor = service.deleteTutor(id);
        return ResponseEntity.of(tutor);
    }
}
