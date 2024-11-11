package com.adopet.tutor;

import com.adopet.CrudController;
import com.adopet.tutor.dto.TutorDto;
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
public class TutorController implements CrudController<TutorDto, TutorForm, TutorPatchForm> {

    private final TutorService service;

    public ResponseEntity<List<TutorDto>> getAll() {
        List<TutorDto> all = service.getAllTutores();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<TutorDto> getById(UUID id) {
        TutorDto tutorById = service.getTutorById(id);
        return ResponseEntity.ofNullable(tutorById);
    }

    public ResponseEntity<TutorDto> insertNew(TutorForm tutorForm) {
        TutorDto tutor = service.insertNewTutor(tutorForm);
        return ResponseEntity.created(URI.create("/tutores/" + tutor.id().toString())).body(tutor);
    }

    public ResponseEntity<TutorDto> update(UUID id, TutorForm tutorForm) {
        TutorDto updatedTutor= service.updateTutor(id, tutorForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    public ResponseEntity<TutorDto> patch(UUID id, TutorPatchForm tutorForm) {
        TutorDto patchedTutor= service.patchTutor(id, tutorForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    public ResponseEntity<TutorDto> delete(UUID id) {
        TutorDto tutor = service.deleteTutor(id);
        return ResponseEntity.ofNullable(tutor);
    }
}
