package com.app.bdd.steps;

import com.app.entity.Film;
import com.app.entity.User;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.repository.FilmRepository;
import com.app.repository.UserRepository;
import com.app.service.FilmService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FilmService filmService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private ResultActions resultActions;
    private String currentUsername;

    @Given("użytkownik {string} jest zalogowany")
    public void uzytkownik_jest_zalogowany(String username) throws Exception {
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("password"));
            userRepository.save(user);
        }
        this.currentUsername = username;
    }

    @Given("w systemie istnieje film {string}")
    public void w_systemie_istnieje_film(String title) {
        Film film = new Film();
        film.setTitle(title);
        film.setReleaseYear(2020);
        film.setDuration(120);
        filmRepository.save(film);
    }

    @When("użytkownik dodaje recenzję do filmu {string} z oceną {int} i komentarzem {string}")
    public void uzytkownik_dodaje_recenzje(String title, int rating, String comment) throws Exception {
        Film film = filmRepository.getAll().stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElseThrow();

        NewReviewRequest request = new NewReviewRequest(rating, comment);

        resultActions = mockMvc.perform(post("/film/" + film.getId() + "/review")
                .with(user(currentUsername))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("recenzja zostaje zapisana w systemie")
    public void recenzja_zostaje_zapisana() throws Exception {
        resultActions.andExpect(status().isCreated());
    }

    @Then("średnia ocena filmu {string} wynosi {double}")
    public void srednia_ocena_filmu_wynosi(String title, double expectedRating) throws Exception {
        Film film = filmRepository.getAll().stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElseThrow();

        // Refresh from DB or check via API
        resultActions = mockMvc.perform(get("/film/" + film.getId()));
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);

        assertEquals(expectedRating, node.path("averageRating").asDouble(), 0.1);
    }

    @When("użytkownik próbuje dodać recenzję do filmu {string} bez oceny")
    public void uzytkownik_probuje_dodac_recenzje_bez_oceny(String title) throws Exception {
        Film film = filmRepository.getAll().stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElseThrow();

        // Sending null rating
        NewReviewRequest request = new NewReviewRequest(null, "Komentarz bez oceny");

        resultActions = mockMvc.perform(post("/film/" + film.getId() + "/review")
                .with(user(currentUsername))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("system odrzuca recenzję")
    public void system_odrzuca_recenzje() throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    @Given("film {string} posiada recenzję użytkownika {string} z oceną {int}")
    public void film_posiada_recenzje(String title, String username, int rating) {
        transactionTemplate.execute(status -> {
            Film film = filmRepository.getAll().stream()
                    .filter(f -> f.getTitle().equals(title))
                    .findFirst()
                    .orElseThrow();

            // Ensure user exists
            if (userRepository.findByUsername(username) == null) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("pass"));
                userRepository.save(user);
            }

            NewReviewRequest request = new NewReviewRequest(rating, "Auto comment");
            filmService.addReview(film.getId(), request, username);
            return null;
        });
    }

    @When("użytkownik wyświetla szczegóły filmu {string}")
    public void uzytkownik_wyswietla_szczegoly_filmu(String title) throws Exception {
        Film film = filmRepository.getAll().stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElseThrow();

        resultActions = mockMvc.perform(get("/film/" + film.getId()));
    }

    @Then("lista recenzji zawiera {int} pozycje")
    public void lista_recenzji_zawiera_pozycje(int count) throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertEquals(count, node.path("reviews").size());
    }
}
