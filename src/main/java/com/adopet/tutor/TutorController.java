package com.adopet.tutor;

import com.adopet.tutor.dto.PatchTutorForm;
import com.adopet.tutor.dto.Tutor;
import com.adopet.tutor.dto.TutorForm;
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
    public ResponseEntity<List<Tutor>> getAllTutores() {
        List<Tutor> all = service.getAllTutores();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutor> getTutorById(@PathVariable(name = "id") UUID id) {
        Tutor tutorById = service.getTutorById(id);
        return ResponseEntity.ofNullable(tutorById);
    }

    @PostMapping
    public ResponseEntity<?> insertNewTutor(@RequestBody @Valid TutorForm tutorForm) {
        Tutor tutor = service.insertNewTutor(tutorForm);
        return ResponseEntity.created(URI.create("/tutores/" + tutor.id().toString())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable(name = "id") UUID id,
                                              @RequestBody @Valid TutorForm tutorForm) {
        Tutor updatedTutor= service.updateTutor(id, tutorForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tutor> patchTutor(@PathVariable(name = "id") UUID id,
                                             @RequestBody @Valid PatchTutorForm tutorForm) {
        Tutor patchedTutor= service.patchTutor(id, tutorForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TutorEntity> deleteTutor(@PathVariable(name = "id") UUID id) {
        Optional<TutorEntity> tutorEntity = service.deleteTutor(id);
        return ResponseEntity.of(tutorEntity);
    }
}
