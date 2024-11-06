package com.adopet.abrigo;

import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .andExpect(
                        status()
                                .is2xxSuccessful())
                .andExpect(
                        content()
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content()
                                .json("[{id:" + abrigo.getId() +
                                        ", name:default, phone:\"5511955556666\", \"location\":\"São Paulo\"}]"));
    }

    @Test
    void getAllAbrigos_Empty_Return200() throws Exception {
        repository.deleteAll();

        mvc.perform(get(URL))
                .andExpect(
                        status()
                                .isNotFound());
    }

    @Test
    void getAbrigoById_ValidId_Return200() throws Exception {
        mvc.perform(get(URL + "/" + abrigo.getId()))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{id:" + abrigo.getId() +
                                        ", name:default, phone:\"5511955556666\", \"location\":\"São Paulo\"}"));
    }

    @Test
    void getAbrigoById_InvalidId_Return404() throws Exception {
        mvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status()
                                .isNotFound());
    }

    @Test
    void postNewAbrigo_ValidForm_Return201() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", "123456789", "TestLand");

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
                                .json("{name:testName, phone:\"123456789\", \"location\":\"TestLand\"}"));
    }

    @Test
    void postNewAbrigo_InvalidPhone_Return400() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", "1234567", "TestLand");

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
    void postNewAbrigo_InvalidForm_Return400() throws Exception {
        AbrigoForm form = new AbrigoForm("testName", null, "TestLand");

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
    void updateAbrigo_ValidForm_Return200() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "0123456789", "TestLand");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{name:testPutName, phone:\"0123456789\"}"));
    }

    @Test
    void updateAbrigo_InvalidPhone_Return400() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "1234567", "TestLand");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void updateAbrigo_InvalidForm_Return400() throws Exception {
        AbrigoForm form = new AbrigoForm("testPutName", "0123456789", "");

        mvc.perform(put(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void patchAbrigo_ValidForm_Return200() throws Exception {
        AbrigoPatchForm form = new AbrigoPatchForm("PatchName", null, null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isOk())
                .andExpect(
                        content()
                                .json("{name:PatchName, phone:\"5511955556666\"}"));
    }

    @Test
    void patchAbrigo_InvalidPhone_Return400() throws Exception {
        AbrigoPatchForm form = new AbrigoPatchForm(null, "1234567", null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void patchAbrigo_InvalidName_Return400() throws Exception {
        AbrigoForm form = new AbrigoForm("t", null, null);

        mvc.perform(patch(URL + "/" + abrigo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpect(
                        status()
                                .isBadRequest());
    }

    @Test
    void deleteAbrigo_ValidId_Return200() throws Exception {
        mvc.perform(delete(URL + "/" + abrigo.getId()))
                .andExpect(
                        status().isOk());
    }

    @Test
    void deleteAbrigo_WrongId_Return404() throws Exception {
        mvc.perform(delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound());
    }

    @Test
    void deleteAbrigo_InvalidId_Return404() throws Exception {
        mvc.perform(delete(URL + "/a"))
                .andExpect(
                        status().isBadRequest());
    }
}