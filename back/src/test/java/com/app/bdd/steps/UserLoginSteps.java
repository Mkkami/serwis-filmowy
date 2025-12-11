package com.app.bdd.steps;

import com.app.entity.User;
import com.app.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserLoginSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ResultActions resultActions;
    private MockHttpSession session;

    @Given("użytkownik {string} z hasłem {string} istnieje w systemie")
    public void uzytkownik_istnieje_w_systemie(String username, String password) {
        if (userRepository.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @When("użytkownik loguje się używając nazwy {string} i hasła {string}")
    public void uzytkownik_loguje_sie(String username, String password) throws Exception {
        // Using Spring Security's formLogin() builder which sends
        // application/x-www-form-urlencoded
        // to the configured login processing URL (default /login)
        resultActions = mockMvc.perform(formLogin().user(username).password(password));
    }

    @Then("logowanie powinno zakończyć się sukcesem")
    public void logowanie_powinno_zakonczyc_sie_sukcesem() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @Then("logowanie powinno zakończyć się błędem autoryzacji")
    public void logowanie_powinno_zakonczyc_sie_bledem() throws Exception {
        resultActions.andExpect(status().isUnauthorized());
    }

    @Given("użytkownik {string} z hasłem {string} jest zalogowany do systemu")
    public void uzytkownik_jest_zalogowany(String username, String password) throws Exception {
        // 1. Ensure user exists
        uzytkownik_istnieje_w_systemie(username, password);

        // 2. Perform login and capture session
        // We use a new session for this scenario
        session = new MockHttpSession();
        mockMvc.perform(post("/login")
                .param("username", username)
                .param("password", password)
                .session(session))
                .andExpect(status().isOk());
    }

    @When("użytkownik wylogowuje się")
    public void uzytkownik_wylogowuje_sie() throws Exception {
        // Use the captured session to logout
        if (session != null) {
            resultActions = mockMvc.perform(post("/logout").session(session));
        } else {
            resultActions = mockMvc.perform(post("/logout"));
        }
    }

    @Then("wylogowanie powinno zakończyć się sukcesem")
    public void wylogowanie_powinno_zakonczyc_sie_sukcesem() throws Exception {
        // Default logout success usually redirects to /login?logout or returns 204/200
        // depending on config
        // In SecurityConfig:
        // .logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
        // It doesn't specify successHandler, so it defaults to
        // SimpleUrlLogoutSuccessHandler which redirects to /login?logout
        // BUT wait, if it's a REST API, we might expect 200 OK or 204 No Content.
        // Let's check SecurityConfig again.
        // It just says .logout(...).
        // Default behavior is redirect to /login?logout.
        // Let's see what happens. If it redirects, status is 302.

        // Update: Looking at SecurityConfig, there is no .logoutSuccessHandler()
        // configured.
        // So it will likely redirect.
        // However, for an API, we usually want 200 OK.
        // Let's assume 302 or 200. I'll check the result.
        // Actually, let's check if I can adjust the expectation or the config.
        // For now, I'll expect 302 (Found) or 204 (No Content) or 200 (OK).
        // Let's try expecting 302 first as it is default.
        // Or better, I can check if the session is invalidated.

        // Let's try to be generic or check what happens.
        // I'll assume 302 for now.
        // resultActions.andExpect(status().is3xxRedirection());

        // Wait, if I want to be sure, I should check SecurityConfig again.
        // It has .logoutUrl("/logout").
        // No success handler.

        // Let's try to run it and see.
        // I'll put a placeholder expectation.
        resultActions.andExpect(status().is3xxRedirection());
    }
}
