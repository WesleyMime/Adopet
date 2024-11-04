package com.adopet.tutor;

import com.adopet.tutor.dto.TutorForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TutorControllerTest {

    private static final String URL = "/tutores";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TutorRepository repository;

    private TutorEntity tutor;

    @BeforeEach
    void beforeEach() {
        tutor = repository.save(new TutorEntity("default", "default@email.com", "defaultPass"));
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void getAllTutors_Exists_Return200() throws Exception {
        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .is2xxSuccessful())
                .andExpect(
                        content()
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content()
                                .json("[{id:" + tutor.getId() + ", name:default, email:default@email.com}]"));
    }

    @Test
    void getAllTutors_Empty_Return200() throws Exception {
        repository.deleteAll();

        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .isNotFound());
    }

    @Test
    void getTutorById_ValidId_Return200() throws Exception {
        mvc.perform(get(URL + "/" + tutor.getId()))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{id:" + tutor.getId() + ", name:default, email:default@email.com}"));
    }

    @Test
    void getTutorById_InvalidId_Return404() throws Exception {
        mvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status()
                                .isNotFound());
    }

    @Test
    void postNewTutor_ValidForm_Return201() throws Exception {
        TutorForm form = new TutorForm("testName", "test@email.com", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isCreated())
                .andExpect(
                        header()
                                .exists("Location"))
                .andExpect(
                        content()
                                .json("{name:testName, email:test@email.com}"));
    }

    @Test
    void postNewTutor_InvalidEmail_Return400() throws Exception {
        TutorForm form = new TutorForm("testName", "testemail", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest())
                .andExpect(
                        header()
                                .doesNotExist("Location"));
    }

    @Test
    void postNewTutor_InvalidPassword_Return400() throws Exception {
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"testName\", \"email\":\"test@email.com\"}"))
                .andExpect(
                        status()
                                .isBadRequest())
                .andExpect(
                        header()
                                .doesNotExist("Location"));
    }

    @Test
    void updateTutor_ValidForm_Return200() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut@email.com", "testPutPass");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{name:testPutName, email:testPut@email.com}"));
    }

    @Test
    void updateTutor_InvalidEmail_Return400() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut", "testPutPass");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void updateTutor_InvalidPassword_Return400() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut", "test");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void patchTutor_ValidForm_Return200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testPut@email.com\"}"))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{name:default, email:testPut@email.com}"));
    }

    @Test
    void patchTutor_InvalidEmail_Return400() throws Exception {
        TutorForm form = new TutorForm("testPatchName", "testPatchEmail", null);

        mvc.perform(MockMvcRequestBuilders.patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void patchTutor_InvalidName_Return400() throws Exception {
        TutorForm form = new TutorForm("t", null, null);

        mvc.perform(MockMvcRequestBuilders.patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void deleteTutor_ValidId_Return200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL + "/" + tutor.getId()))
                .andExpect(
                        status().isOk());
    }

    @Test
    void deleteTutor_WrongId_Return404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound());
    }

    @Test
    void deleteTutor_InvalidId_Return404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(URL + "/a"))
                .andExpect(
                        status().isBadRequest());
    }
}