package com.adopet.pet;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.pet.dto.PetForm;
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
class PetControllerTest {

    private static final String URL = "/pets";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PetRepository repository;
    @Autowired
    private AbrigoRepository abrigoRepository;

    private PetEntity pet;

    private AbrigoEntity abrigo;

    @BeforeEach
    void beforeEach() {
        abrigo = abrigoRepository.save(new AbrigoEntity("default", "5511955556666", "São Paulo"));

        pet = repository.save(new PetEntity(abrigo, "Duck", "Description", "1 Month",
                "Address", "https://cs50.ai/static/img/duck_6.jpg"));
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
        abrigoRepository.deleteAll();
    }

    @Test
    void getAllPets_Exists_Return200() throws Exception {
        mvc.perform(get(URL))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].name", is("Duck")),
                        jsonPath("$[0].age", is("1 Month")),
                        jsonPath("$[0].address", is("Address")),
                        jsonPath("$[0].abrigo", is(abrigo.getId().toString())),
                        jsonPath("$[0].description", is("Description")),
                        jsonPath("$[0].image", is("https://cs50.ai/static/img/duck_6.jpg")))
                .andDo(print());
    }

    @Test
    void getAllPets_Empty_Return200() throws Exception {
        repository.deleteAll();

        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void getPetById_ValidId_Return200() throws Exception {
        mvc.perform(get(URL + "/" + pet.getId()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("Duck")),
                        jsonPath("$.age", is("1 Month")),
                        jsonPath("$.address", is("Address")),
                        jsonPath("$.adopted", is(false)),
                        jsonPath("$.abrigo", is(abrigo.getId().toString())),
                        jsonPath("$.description", is("Description")),
                        jsonPath("$.image", is("https://cs50.ai/static/img/duck_6.jpg")))
                .andDo(print());
    }

    @Test
    void getPetById_WrongId_Return404() throws Exception {
        mvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status()
                                .isNotFound())
                .andDo(print());
    }

    @Test
    void postNewPet_ValidForm_Return201() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location"),
                        jsonPath("$.name", is("DDB50")),
                        jsonPath("$.age", is("2 Months")),
                        jsonPath("$.address", is("New Address")),
                        jsonPath("$.adopted", is(false)),
                        jsonPath("$.abrigo", is(abrigo.getId().toString())),
                        jsonPath("$.description", is("Description 2")),
                        jsonPath("$.image", is("https://cs50.ai/static/img/duck_7.jpg")))
                .andDo(print());
    }

    @Test
    void postNewPet_NoForm_Return422() throws Exception {
        mvc.perform(post(URL))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Invalid request content.")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidAbrigo_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", UUID.randomUUID(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Abrigo not found")),
                        jsonPath("$.detail",  is("There is no abrigo with this id.")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidAge_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("age")),
                        jsonPath("$.errors[0].detail",  is("size must be between 4 and 20")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidName_Return422() throws Exception {
        PetForm form = new PetForm("t", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
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
    void postNewPet_InvalidAddress_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "t", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("address")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidDescription_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                null, "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("description")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidImage_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "duck.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("image")),
                        jsonPath("$.errors[0].detail",  is("must be a valid URL")))
                .andDo(print());
    }

    @Test
    void postNewPet_InvalidForm_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", null,
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().doesNotExist("Address"),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("abrigo")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void updatePet_ValidForm_Return200() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("DDB50")),
                        jsonPath("$.age", is("2 Months")),
                        jsonPath("$.address", is("New Address")),
                        jsonPath("$.abrigo", is(abrigo.getId().toString())),
                        jsonPath("$.description", is("Description 2")),
                        jsonPath("$.image", is("https://cs50.ai/static/img/duck_7.jpg")))
                .andDo(print());
    }

    @Test
    void updatePet_WrongId_Return404() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void updatePet_InvalidAbrigo_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", UUID.randomUUID(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Abrigo not found")),
                        jsonPath("$.detail",  is("There is no abrigo with this id.")))
                .andDo(print());
    }

    @Test
    void updatePet_InvalidAge_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("age")),
                        jsonPath("$.errors[0].detail",  is("size must be between 4 and 20")))
                .andDo(print());
    }

    @Test
    void updatePet_InvalidName_Return422() throws Exception {
        PetForm form = new PetForm("t", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
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
    void updatePet_InvalidAddress_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "t", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("address")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void updateNewPet_InvalidDescription_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                null, "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("description")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void updateNewPet_InvalidImage_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("image")),
                        jsonPath("$.errors[0].detail",  is("must be a valid URL")))
                .andDo(print());
    }

    @Test
    void updatePet_InvalidForm_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", null,
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(put(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("abrigo")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    void patchPet_ValidForm_Return200() throws Exception {
        PetForm form = new PetForm(null, null, null, abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name", is("Duck")),
                        jsonPath("$.age", is("1 Month")),
                        jsonPath("$.address", is("Address")),
                        jsonPath("$.abrigo", is(abrigo.getId().toString())),
                        jsonPath("$.description", is("Description 2")),
                        jsonPath("$.image", is("https://cs50.ai/static/img/duck_7.jpg")))
                .andDo(print());
    }

    @Test
    void patchPet_WrongId_Return404() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void patchPet_InvalidAbrigo_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "New Address", UUID.randomUUID(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Abrigo not found")),
                        jsonPath("$.detail",  is("There is no abrigo with this id.")))
                .andDo(print());
    }

    @Test
    void patchPet_InvalidAge_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("age")),
                        jsonPath("$.errors[0].detail",  is("size must be between 4 and 20")))
                .andDo(print());
    }

    @Test
    void patchPet_InvalidAddress_Return422() throws Exception {
        PetForm form = new PetForm("DDB50", "2 Months", "t", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("address")),
                        jsonPath("$.errors[0].detail",  is("size must be between 8 and 50")))
                .andDo(print());
    }

    @Test
    void patchPet_InvalidName_Return422() throws Exception {
        PetForm form = new PetForm("t", "2 Months", "New Address", abrigo.getId(),
                "Description 2", "https://cs50.ai/static/img/duck_7.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
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
    void patchNewPet_InvalidDescription_Return422() throws Exception {
        PetForm form = new PetForm(null, null, null, null,
                "t", null);

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("description")),
                        jsonPath("$.errors[0].detail",  is("size must be between 10 and 255")))
                .andDo(print());
    }

    @Test
    void patchNewPet_InvalidImage_Return422() throws Exception {
        PetForm form = new PetForm(null, null, null, null,
                null, "duck.jpg");

        mvc.perform(patch(URL + "/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("image")),
                        jsonPath("$.errors[0].detail",  is("must be a valid URL")))
                .andDo(print());
    }

    @Test
    void deletePet_ValidId_Return200() throws Exception {
        mvc.perform(delete(URL + "/" + pet.getId()))
                .andExpect(
                        status().isOk())
                .andDo(print());
    }

    @Test
    void deletePet_WrongId_Return404() throws Exception {
        mvc.perform(delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    void deletePet_InvalidId_Return400() throws Exception {
        mvc.perform(delete(URL + "/a"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Method parameter 'id' is invalid.")))
                .andDo(print());
    }
}