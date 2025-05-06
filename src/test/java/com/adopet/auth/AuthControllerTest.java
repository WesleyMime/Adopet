package com.adopet.auth;

import com.adopet.LoginForm;
import com.adopet.tutor.TutorRepository;
import com.adopet.tutor.TutorService;
import com.adopet.tutor.dto.TutorDto;
import com.adopet.tutor.dto.TutorForm;
import com.adopet.user.Roles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthControllerTest {

    private static final String URL = "/auth/login";
    private static final String URL_TUTORES = "/tutores";
    @Autowired
    private TutorService service;
    @Autowired
    private TutorRepository repository;
    private TutorDto tutor;
    private TutorForm tutorForm;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void beforeEach() {
        this.tutorForm = new TutorForm("default", "default@email.com", "defaultPass");
        this.tutor = service.insertNewTutor(tutorForm);
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }


    @Test
    public void authenticate_Exists_Return200() throws Exception {
        LoginForm form = new LoginForm(tutorForm.email(), tutorForm.password());

        mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(form.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.token", startsWith("eyJhbGciOiJIUzUxMiJ9.")),
                        jsonPath("$.expiresAt", anything()),
                        jsonPath("$.role", is(List.of(Roles.TUTOR).toString())),
                        jsonPath("$.id", is(tutor.id().toString())))
                .andDo(print());
    }
    @Test
    public void authenticate_InvalidPassword_Return403() throws Exception {
        LoginForm form = new LoginForm(tutorForm.email(), "wrongPass");

        mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(form.toString()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.type", is("about:blank")),
                        jsonPath("$.title", is("Forbidden")),
                        jsonPath("$.detail", is("Invalid e-mail and password.")))
                .andDo(print());
    }

    @Test
    public void authenticate_InvalidEmail_Return403() throws Exception {
        LoginForm form = new LoginForm("wrong@email.com", tutorForm.password());

        mvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(form.toString()))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.type", is("about:blank")),
                        jsonPath("$.title", is("Forbidden")),
                        jsonPath("$.detail", is("Invalid e-mail and password.")))
                .andDo(print());
    }

    @Test
    void getAllTutors_NotLoggedIn_Return403() throws Exception {
        mvc.perform(get(URL_TUTORES))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getAllTutors_ExpiredToken_Return403() throws Exception {
        mvc.perform(get(URL_TUTORES)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJBZG9wZXQiLCJzdWIiOiJkZWZhdWx0QGVtYWlsLmNvbSIsImlhdCI6MTc0NjQ5NzE4MywiZXhwIjoxNzQ2NTAwNzgzfQ.J3crLcQSiyvkd-4kN-CEjHXZTEss0e183-fhcP_OhZNRi8OuxS7_UwARt219DrdyJWYLcm-y5iw33qDHK_ihqg"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getAllTutors_InvalidBearerToken_Return403() throws Exception {
        mvc.perform(get(URL_TUTORES)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ndsoifgnaeoinbueatea"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getAllTutors_InvalidToken_Return403() throws Exception {
        mvc.perform(get(URL_TUTORES)
                        .header(HttpHeaders.AUTHORIZATION, "ndsoifgnaeoinbueatea"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}