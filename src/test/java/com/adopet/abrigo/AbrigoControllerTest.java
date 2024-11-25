package com.adopet.abrigo;

import com.adopet.abrigo.dto.AbrigoForm;
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
class AbrigoControllerTest {

    private static final String URL = "/abrigos";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AbrigoRepository repository;

    private AbrigoEntity abrigo;

    @BeforeEach
    void beforeEach() {
        abrigo = repository.save(new AbrigoEntity(
                "default", "5511955556666", "São Paulo"));
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void getAllAbrigos_Exists_Return200() throws Exception {
        mvc.perform(get(URL))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content[0].id", is(abrigo.getId().toString())),
                        jsonPath("$.content[0].name", is("default")),
                        jsonPath("$.content[0].phone", is("5511955556666")),
                        jsonPath("$.content[0].location", is("São Paulo")),
                        jsonPath("$.page.size", is(10)),
                        jsonPath("$.page.number", is(0)),
                        jsonPath("$.page.totalElements", is(1)),
                        jsonPath("$.page.totalPages", is(1)))
                .andDo(print());
    }

    @Test
    void getAllAbrigosWrongPage_Empty_Return404() throws Exception {
        mvc.perform(get(URL).param("page", "1"))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void getAllAbrigos_Empty_Return404() throws Exception {
        repository.deleteAll();

        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void getAbrigoById_ValidId_Return200() throws Exception {
        mvc.perform(get(URL + "/" + abrigo.getId()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(abrigo.getId().toString())),
                        jsonPath("$.name", is("default")),
                        jsonPath("$.phone", is("5511955556666")),
                        jsonPath("$.location", is("São Paulo")))
                .andDo(print());
    }

    @Test
    void getAbrigoById_WrongId_Return404() throws Exception {
        mvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void postNewAbrigo_ValidForm_Return201() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", "123456789", "TestLand");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location"),
                        jsonPath("$.name", is("testName")),
                        jsonPath("$.phone", is("123456789")),
                        jsonPath("$.location", is("TestLand")))
                .andDo(print());
    }

    @Test
    void postNewAbrigo_NoForm_Return422() throws Exception {
        mvc.perform(post(URL))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Invalid request content.")))
                .andDo(print());
    }

    @Test
    void postNewAbrigo_InvalidPhone_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", "1234567", "TestLand");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("phone")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 20")))
                .andDo(print());
    }

    @Test
    void postNewAbrigo_InvalidName_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("t", "123456789", "TestLand");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 100")))
                .andDo(print());
    }

    @Test
    void postNewAbrigo_InvalidLocation_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", "123456789", "t");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("location")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void postNewAbrigo_InvalidForm_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", null, "TestLand");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Location"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("phone")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void updateAbrigo_ValidForm_Return200() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "0123456789", "TestLand");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(abrigo.getId().toString())),
                        jsonPath("$.name", is("testPutName")),
                        jsonPath("$.phone", is("0123456789")),
                        jsonPath("$.location", is("TestLand")))
                .andDo(print());
    }

    @Test
    void updateAbrigo_WrongId_Return404() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "0123456789", "TestLand");

        mvc.perform(put(URL + "/" + UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateAbrigo_InvalidPhone_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "1234567", "TestLand");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("phone")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 20")))
                .andDo(print());
    }

    @Test
    void updateAbrigo_InvalidName_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("t", "123456789", "TestLand");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 100")))
                .andDo(print());
    }

    @Test
    void updateAbrigo_InvalidLocation_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "123456789", "t");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("location")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void updateAbrigo_InvalidForm_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "0123456789", null);

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("location")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void patchAbrigo_ValidForm_Return200() throws Exception {
        AbrigoForm form = new AbrigoForm("PatchName", null, null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(abrigo.getId().toString())),
                        jsonPath("$.name", is("PatchName")),
                        jsonPath("$.phone", is("5511955556666")),
                        jsonPath("$.location", is("São Paulo")))
                .andDo(print());
    }

    @Test
    void patchAbrigo_WrongId_Return404() throws Exception {
        AbrigoForm form = new AbrigoForm("PatchName", null, null);

        mvc.perform(patch(URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void patchAbrigo_InvalidPhone_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm(null, "1234567", null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("phone")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 20")))
                .andDo(print());
    }

    @Test
    void patchAbrigo_InvalidLocation_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm(null, null, "local");

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("location")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void patchAbrigo_InvalidName_Return422() throws Exception {
        AbrigoForm form = new AbrigoForm("t", null, null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("name")),
                        jsonPath("$.errors[0].detail",  is("size must be between 3 and 100")))
                .andDo(print());
    }

    @Test
    void deleteAbrigo_ValidId_Return200() throws Exception {
        mvc.perform(delete(URL + "/" + abrigo.getId()))
                .andExpect(
                        status().isOk())
                .andDo(print());
    }

    @Test
    void deleteAbrigo_WrongId_Return404() throws Exception {
        mvc.perform(delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteAbrigo_InvalidId_Return400() throws Exception {
        mvc.perform(delete(URL + "/a"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Method parameter 'id' is invalid.")))
                .andDo(print());
    }
}