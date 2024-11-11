package com.adopet.tutor;

import com.adopet.CrudController;
import com.adopet.tutor.dto.TutorDTO;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.tutor.dto.TutorPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tutores")
@AllArgsConstructor
public class TutorController implements CrudController<TutorDTO, TutorForm, TutorPatchForm> {

    private final TutorService service;

    public ResponseEntity<List<TutorDTO>> getAll() {
        List<TutorDTO> all = service.getAllTutores();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<TutorDTO> getById(UUID id) {
        TutorDTO tutorById = service.getTutorById(id);
        return ResponseEntity.ofNullable(tutorById);
    }

    public ResponseEntity<TutorDTO> insertNew(TutorForm tutorForm) {
        TutorDTO tutor = service.insertNewTutor(tutorForm);
        return ResponseEntity.created(URI.create("/tutores/" + tutor.id().toString())).body(tutor);
    }

    public ResponseEntity<TutorDTO> update(UUID id, TutorForm tutorForm) {
        TutorDTO updatedTutor= service.updateTutor(id, tutorForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    public ResponseEntity<TutorDTO> patch(UUID id, TutorPatchForm tutorForm) {
        TutorDTO patchedTutor= service.patchTutor(id, tutorForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    public ResponseEntity<TutorDTO> delete(UUID id) {
        TutorDTO tutor = service.deleteTutor(id);
        return ResponseEntity.ofNullable(tutor);
    }
}
