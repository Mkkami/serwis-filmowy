package com.app.bdd.steps;

import com.app.entity.User;
import com.app.entity.dto.UserLoginRequest;
import com.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions resultActions;

    @Given("użytkownik nie istnieje w systemie")
    public void uzytkownik_nie_istnieje_w_systemie() {
        // Dzięki @Transactional zmiany są cofane automatycznie po każdym scenariuszu
        // Nie musimy ręcznie czyścić bazy
    }

    @When("użytkownik próbuje zarejestrować się z nazwą {string} i hasłem {string}")
    public void uzytkownik_probuje_zarejestrowac_sie(String username, String password) throws Exception {
        UserLoginRequest request = new UserLoginRequest(username, password);
        resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("rejestracja powinna zakończyć się sukcesem")
    public void rejestracja_powinna_zakonczyc_sie_sukcesem() throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(content().string("User registered"));
    }

    @And("w systemie powinien istnieć użytkownik o nazwie {string}")
    public void w_systemie_powinien_istniec_uzytkownik(String username) {
        User user = userRepository.findByUsername(username);
        assertNotNull(user, "User should exist in the database");
    }

    @Given("użytkownik o nazwie {string} już istnieje w systemie")
    public void uzytkownik_juz_istnieje(String username) {
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("SomePass123!"));
            userRepository.save(user);
        }
    }

    @Then("rejestracja powinna zakończyć się błędem")
    public void rejestracja_powinna_zakonczyc_sie_bledem() throws Exception {
        resultActions.andExpect(status().isConflict());
    }

    @And("komunikat błędu powinien informować o zajętej nazwie użytkownika")
    public void komunikat_bledu_zajeta_nazwa() throws Exception {
        resultActions.andExpect(content().string("Username already exists"));
    }

    @Then("rejestracja powinna zakończyć się błędem walidacji")
    public void rejestracja_powinna_zakonczyc_sie_bledem_walidacji() throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    @And("komunikat błędu powinien informować o zbyt słabym haśle")
    public void komunikat_bledu_slabe_haslo() throws Exception {
        // Note: This message depends on the implementation we haven't added yet.
        // We expect the implementation to throw IllegalArgumentException with this
        // message.
        resultActions.andExpect(content().string("Password is too weak"));
    }
}
