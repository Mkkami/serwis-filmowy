// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

// -- Custom commands dla aplikacji FilmBase --

// Komenda do logowania
Cypress.Commands.add(
  'login',
  (username = 'testuser', password = 'testpassword123') => {
    cy.visit('/login');
    cy.get('input[name="username"]').type(username);
    cy.get('input[name="password"]').type(password);
    cy.get('button[type="submit"]').click();
    cy.url().should('not.include', '/login');
  }
);

// Komenda do rejestracji
Cypress.Commands.add('register', (username, password) => {
  cy.visit('/register');
  cy.get('input[name="username"]').type(username);
  cy.get('input[name="password"]').type(password);
  cy.get('button[type="submit"]').click();
});

// Komenda do wylogowania
Cypress.Commands.add('logout', () => {
  cy.contains('button', 'Logout').click();
});

// Komenda do wyszukiwania
Cypress.Commands.add('searchMovie', (title) => {
  cy.get('.search-bar').type(title);
  cy.get('.form-search-bar button[type="submit"]').click();
});

// Sprawdzenie czy użytkownik jest zalogowany
Cypress.Commands.add('shouldBeLoggedIn', () => {
  cy.get('.profile').should('not.contain', 'Login');
});

// Sprawdzenie czy użytkownik jest wylogowany
Cypress.Commands.add('shouldBeLoggedOut', () => {
  cy.get('.profile').should('contain', 'Login');
});
