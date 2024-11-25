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

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content[0].id", is(tutor.getId().toString())),
                        jsonPath("$.content[0].name", is("default")),
                        jsonPath("$.content[0].email", is("default@email.com")),
                        jsonPath("$.page.size", is(10)),
                        jsonPath("$.page.number", is(0)),
                        jsonPath("$.page.totalElements", is(1)),
                        jsonPath("$.page.totalPages", is(1)))
                .andDo(print());
    }

    @Test
    void getAllTutorsWrongPage_Empty_Return404() throws Exception {
        mvc.perform(get(URL).param("page", "1"))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void getAllTutors_Empty_Return200() throws Exception {
        repository.deleteAll();

        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void getTutorById_ValidId_Return200() throws Exception {
        mvc.perform(get(URL + "/" + tutor.getId()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(tutor.getId().toString())),
                        jsonPath("$.name", is("default")),
                        jsonPath("$.email", is("default@email.com")))
                .andDo(print());
    }

    @Test
    void getTutorById_WrongId_Return404() throws Exception {
        mvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void postNewTutor_ValidForm_Return201() throws Exception {
        TutorForm form = new TutorForm("testName", "test@email.com", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location"),
                        jsonPath("$.name", is("testName")),
                        jsonPath("$.email", is("test@email.com")))
                .andDo(print());
    }

    @Test
    void postNewTutor_NoForm_Return422() throws Exception {
        mvc.perform(post(URL))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Invalid request content.")))
                .andDo(print());
    }

    @Test
    void postNewTutor_InvalidEmail_Return422() throws Exception {
        TutorForm form = new TutorForm("testName", "testemail", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("email")),
                        jsonPath("$.errors[0].detail",  is("must be a well-formed email address")))
                .andDo(print());
    }

    @Test
    void postNewTutor_InvalidName_Return422() throws Exception {
        TutorForm form = new TutorForm("t", "test@email.com", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 30")))
                .andDo(print());
    }

    @Test
    void postNewTutor_InvalidPassword_Return422() throws Exception {
        TutorForm form = new TutorForm("testName", "test@email.com", "t");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("password")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 200")))
                .andDo(print());
    }

    @Test
    void postNewTutor_DuplicateEmail_Return422() throws Exception {
        TutorForm form = new TutorForm("testName", "default@email.com", "testPass");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Email already registered")),
                        jsonPath("$.detail",  is("You tried to use an email that already is in use.")))
                .andDo(print());
    }

    @Test
    void postNewTutor_InvalidForm_Return422() throws Exception {
        TutorForm form = new TutorForm("testName", "test@email.com", null);

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("password")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void updateTutor_ValidForm_Return200() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut@email.com", "testPutPass");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(tutor.getId().toString())),
                        jsonPath("$.name", is("testPutName")),
                        jsonPath("$.email", is("testPut@email.com")))
                .andDo(print());
    }

    @Test
    void updateTutor_WrongId_Return404() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut@email.com", "testPutPass");

        mvc.perform(put(URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateTutor_InvalidEmail_Return422() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut", "testPutPass");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("email")),
                        jsonPath("$.errors[0].detail",  is("must be a well-formed email address")))
                .andDo(print());
    }

    @Test
    void updateTutor_DuplicateEmail_Return422() throws Exception {
        repository.save(new TutorEntity("testPutName", "testPut@email.com", "testPutPass"));

        TutorForm form = new TutorForm("testPutName", "testPut@email.com", "testPutPass");
        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Email already registered")),
                        jsonPath("$.detail",  is("You tried to use an email that already is in use.")))
                .andDo(print());
    }

    @Test
    void updateTutor_InvalidName_Return422() throws Exception {
        TutorForm form = new TutorForm("t", "testPut@email.com", "testPutPass");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 30")))
                .andDo(print());
    }

    @Test
    void updateTutor_InvalidPassword_Return422() throws Exception {
        TutorForm form = new TutorForm("testPutName", "testPut@email.com", "test");

        mvc.perform(put(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("password")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 200")))
                .andDo(print());
    }

    @Test
    void patchTutor_ValidForm_Return200() throws Exception {
        TutorForm form = new TutorForm(null, "testPatch@email.com", null);

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(tutor.getId().toString())),
                        jsonPath("$.name", is("default")),
                        jsonPath("$.email", is("testPatch@email.com")))
                .andDo(print());
    }

    @Test
    void patchTutor_WrongId_Return404() throws Exception {
        TutorForm form = new TutorForm(null, "testPatch@email.com", null);

        mvc.perform(patch(URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void patchTutor_InvalidEmail_Return422() throws Exception {
        TutorForm form = new TutorForm("testPatchName", "testPatchEmail", null);

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("email")),
                        jsonPath("$.errors[0].detail",  is("must be a well-formed email address")))
                .andDo(print());
    }

    @Test
    void patchTutor_DuplicateEmail_Return422() throws Exception {
        repository.save(new TutorEntity("testPatchName", "testPatch@email.com", "testPatchPass"));

        TutorForm form = new TutorForm(null, "testPatch@email.com", null);

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Email already registered")),
                        jsonPath("$.detail",  is("You tried to use an email that already is in use.")))
                .andDo(print());
    }

    @Test
    void patchTutor_InvalidName_Return422() throws Exception {
        TutorForm form = new TutorForm("p", null, null);

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 30")))
                .andDo(print());
    }

    @Test
    void patchTutor_ValidName_Return200() throws Exception {
        TutorForm form = new TutorForm("testPatchName", null, null);

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(tutor.getId().toString())),
                        jsonPath("$.name", is("testPatchName")),
                        jsonPath("$.email", is("default@email.com")))
                .andDo(print());
    }

    @Test
    void patchTutor_InvalidPassword_Return422() throws Exception {
        TutorForm form = new TutorForm(null, null, "pass");

        mvc.perform(patch(URL + "/" + tutor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("password")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 200")))
                .andDo(print());
    }

    @Test
    void deleteTutor_ValidId_Return200() throws Exception {
        mvc.perform(delete(URL + "/" + tutor.getId()))
                .andExpect(
                        status().isOk())
                .andDo(print());
    }

    @Test
    void deleteTutor_WrongId_Return404() throws Exception {
        mvc.perform(delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteTutor_InvalidId_Return400() throws Exception {
        mvc.perform(delete(URL + "/a"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Method parameter 'id' is invalid.")))
                .andDo(print());
    }
}