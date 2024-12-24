package com.adopet.adocao;

import com.adopet.abrigo.AbrigoEntity;
import com.adopet.abrigo.AbrigoRepository;
import com.adopet.adocao.dto.AdocaoForm;
import com.adopet.pet.PetEntity;
import com.adopet.pet.PetRepository;
import com.adopet.tutor.TutorEntity;
import com.adopet.tutor.TutorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdocaoControllerTest {

    private static final String URL = "/adocao";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    private PetEntity pet;

    private TutorEntity tutor;

    @BeforeEach
    void beforeEach() {
        AbrigoEntity abrigo = new AbrigoEntity("defaultAbrigo@email.com", "defaultPass", "Default",
                "5511955556666", "São Paulo");
        abrigo = abrigoRepository.save(abrigo);

        pet = new PetEntity(abrigo, "Duck", "Small size", "Description", "1 Month",
                "Address", "https://cs50.ai/static/img/duck_6.jpg");
        pet = petRepository.save(pet);

        tutor = new TutorEntity("default", "defaultTutor@email.com", "defaultPass");
        tutor = tutorRepository.save(tutor);
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
        petRepository.deleteAll();
        abrigoRepository.deleteAll();
        tutorRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_ValidForm_Return201() throws Exception {
        AdocaoForm adocaoForm = new AdocaoForm(pet.getId(), tutor.getId());

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adocaoForm.toString()))
                .andExpectAll(
                        status().isCreated(),
                        header().exists("Location"),
                        jsonPath("$.pet", is(pet.getId().toString())),
                        jsonPath("$.tutor", is(tutor.getId().toString())),
                        jsonPath("$.date", is(LocalDate.now().toString())))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_AlreadyAdopted_Return422() throws Exception {
        AdocaoForm adocaoForm = new AdocaoForm(pet.getId(), tutor.getId());

        mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(adocaoForm.toString()));

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adocaoForm.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Pet already adopted")),
                        jsonPath("$.detail",  is("This pet was already adopted.")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_NoForm_Return422() throws Exception {
        mvc.perform(post(URL))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Invalid request content.")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_InvalidPet_Return422() throws Exception {
        AdocaoForm form = new AdocaoForm(null, tutor.getId());

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("pet")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_InvalidTutor_Return422() throws Exception {
        AdocaoForm form = new AdocaoForm(pet.getId(), null);

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Unprocessable Entity")),
                        jsonPath("$.detail",  is("Invalid request content.")),
                        jsonPath("$.errors[0].field",  is("tutor")),
                        jsonPath("$.errors[0].detail",  is("must not be null")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_WrongPet_Return422() throws Exception {
        AdocaoForm form = new AdocaoForm(UUID.randomUUID(), tutor.getId());

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Pet not found")),
                        jsonPath("$.detail",  is("There is no pet with this id.")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void postNewAdocao_WrongTutor_Return422() throws Exception {
        AdocaoForm form = new AdocaoForm(pet.getId(), UUID.randomUUID());

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(form.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.title",  is("Tutor not found")),
                        jsonPath("$.detail",  is("There is no tutor with this id.")))
                .andDo(print());
    }
    @Test
    @WithMockUser(roles = "ABRIGO")
    void deleteAdocao_ValidId_Return200() throws Exception {
        AdocaoEntity adocao = repository.save(new AdocaoEntity(pet, tutor));

        mvc.perform(delete(URL + "/" + adocao.getId()))
                .andExpect(
                        status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void deleteAdocao_WrongId_Return404() throws Exception {
        mvc.perform(delete(URL + "/" + UUID.randomUUID()))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ABRIGO")
    void deleteAdocao_InvalidId_Return400() throws Exception {
        mvc.perform(delete(URL + "/a"))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.title",  is("Bad Request")),
                        jsonPath("$.detail",  is("Method parameter 'id' is invalid.")))
                .andDo(print());
    }
}