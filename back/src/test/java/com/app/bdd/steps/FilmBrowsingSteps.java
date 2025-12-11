package com.app.bdd.steps;

import com.app.entity.Category;
import com.app.entity.Film;
import com.app.entity.User;
import com.app.repository.CategoryRepository;
import com.app.repository.FilmRepository;
import com.app.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmBrowsingSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;
    private ResultActions resultActions;

    @Given("w systemie istnieją następujące filmy:")
    public void w_systemie_istnieja_filmy(DataTable dataTable) {
        filmRepository.deleteAll();
        categoryRepository.deleteAll();

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            String title = row.get("tytuł");
            String genreName = row.get("gatunek");
            int year = Integer.parseInt(row.get("rok"));
            double rating = Double.parseDouble(row.get("ocena"));
            int duration = Integer.parseInt(row.get("czas_trwania"));

            Category category = categoryRepository.findByName(genreName);
            if (category == null) {
                category = new Category();
                category.setName(genreName);
                category = categoryRepository.save(category);
            }

            Film film = new Film();
            film.setTitle(title);
            film.setReleaseYear(year);
            film.setDuration(duration);
            film.setCategories(List.of(category));
            film.setAverageRating(rating);
            film.setReviewCount(1);

            filmRepository.save(film);
        }
    }

    @When("użytkownik pobiera listę wszystkich filmów")
    public void uzytkownik_pobiera_liste_wszystkich_filmow() throws Exception {
        // Using /film endpoint which returns List<Film>
        resultActions = mockMvc.perform(get("/film"));
    }

    @Then("system zwraca listę zawierającą {int} filmów")
    public void system_zwraca_liste_zawierajaca_n_filmow(int count) throws Exception {
        resultActions.andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();

        // Check if it's a Page (from search) or List (from getAll)
        if (content.contains("\"content\":")) {
            JsonNode root = objectMapper.readTree(content);
            assertEquals(count, root.path("content").size());
        } else {
            List<Film> films = objectMapper.readValue(content, new TypeReference<List<Film>>() {
            });
            assertEquals(count, films.size());
        }
    }

    @Then("system zwraca listę zawierającą {int} film")
    public void system_zwraca_liste_zawierajaca_1_film(int count) throws Exception {
        system_zwraca_liste_zawierajaca_n_filmow(count);
    }

    @Then("każdy film na liście ma tytuł i rok produkcji")
    public void kazdy_film_ma_tytul_i_rok() throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        List<Film> films = objectMapper.readValue(content, new TypeReference<List<Film>>() {
        });

        for (Film film : films) {
            assertNotNull(film.getTitle());
            assertTrue(film.getReleaseYear() > 0);
        }
    }

    @When("użytkownik wyszukuje filmy wpisując frazę {string}")
    public void uzytkownik_wyszukuje_filmy(String phrase) throws Exception {
        // Using /film/search endpoint which returns Page<FilmRequest>
        resultActions = mockMvc.perform(get("/film/search")
                .param("title", phrase));
    }

    @Then("wszystkie zwrócone filmy zawierają w tytule {string}")
    public void wszystkie_filmy_zawieraja_w_tytule(String phrase) throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(content);
        JsonNode contentNode = root.path("content");

        assertTrue(contentNode.isArray());
        for (JsonNode node : contentNode) {
            String title = node.path("title").asText();
            assertTrue(title.contains(phrase));
        }
    }

    @When("użytkownik filtruje filmy po gatunku {string}")
    public void uzytkownik_filtruje_filmy_po_gatunku(String genre) throws Exception {
        Category category = categoryRepository.findByName(genre);
        assertNotNull(category, "Category not found: " + genre);

        resultActions = mockMvc.perform(get("/film/search")
                .param("categories", category.getId().toString()));
    }

    @Then("na liście znajduje się film {string}")
    public void na_liscie_znajduje_sie_film(String title) throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(content);
        JsonNode contentNode = root.path("content");

        boolean found = false;
        for (JsonNode node : contentNode) {
            if (node.path("title").asText().equals(title)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Film not found: " + title);
    }

    @When("użytkownik pobiera szczegóły filmu {string}")
    public void uzytkownik_pobiera_szczegoly_filmu(String title) throws Exception {
        // Find ID first
        // We can use the search endpoint to find the ID, or repo
        // But repo is easier in test
        // We need to find the film in the DB to get its ID
        // Since we don't have findByTitle in repo interface (only getAll and search),
        // we can use getAll and filter.
        List<Film> films = filmRepository.getAll();
        Film film = films.stream()
                .filter(f -> f.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Film not found in DB: " + title));

        resultActions = mockMvc.perform(get("/film/" + film.getId()));
    }

    @Then("system zwraca poprawne szczegóły filmu")
    public void system_zwraca_poprawne_szczegoly() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @Then("zwrócony tytuł to {string}")
    public void zwrocony_tytul_to(String title) throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertEquals(title, node.path("title").asText());
    }

    @Then("zwrócony rok produkcji to {int}")
    public void zwrocony_rok_to(int year) throws Exception {
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertEquals(year, node.path("releaseYear").asInt());
    }
}
