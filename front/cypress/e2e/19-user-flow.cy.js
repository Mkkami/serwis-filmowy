describe('19 - User Flow - Complete Journey', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('Kompletny flow: Wejście na stronę -> Wyszukiwanie -> Login', () => {
    // Krok 1: Wejście na stronę główną
    cy.visit('/');
    cy.get('.header').should('be.visible');
    cy.get('.logo').should('contain', 'FilmBase');

    // Krok 2: Próba wyszukiwania
    cy.get('.search-bar').should('be.visible');
    cy.get('.search-bar').type('Matrix');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
    cy.url().should('include', 'title=Matrix');

    // Krok 3: Powrót na stronę główną
    cy.get('.logo').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    // Krok 4: Przejście do logowania
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');

    // Krok 5: Wypełnienie formularza logowania
    cy.get('input[name="username"]').type('testuser');
    cy.get('input[name="password"]').type('password123');
    cy.get('button[type="submit"]').should('be.visible');
  });

  it('Kompletny flow: Rejestracja nowego użytkownika', () => {
    // Krok 1: Wejście na stronę
    cy.visit('/');

    // Krok 2: Przejście do logowania
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');

    // Krok 3: Przejście do rejestracji
    cy.contains('a', 'Register').click();
    cy.url().should('include', '/register');

    // Krok 4: Wypełnienie formularza rejestracji
    const timestamp = Date.now();
    cy.get('input[name="username"]').type(`user${timestamp}`);
    cy.get('input[name="password"]').type('password12345');

    // Krok 5: Submit formularza
    cy.get('button[type="submit"]').should('be.visible');

    // Krok 6: Powrót do logowania przez link
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');
  });

  it('Kompletny flow: Użytkownik niezalogowany -> różne sekcje', () => {
    // Krok 1: Strona główna
    cy.visit('/');
    cy.shouldBeLoggedOut();

    // Krok 2: Sprawdzenie że przycisk Add nie jest widoczny
    cy.get('button').contains('Add').should('not.exist');

    // Krok 3: Wyszukiwanie
    cy.get('.search-bar').type('Avatar');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');

    // Krok 4: Powrót i próba dostępu do formularzy
    cy.visit('/login');
    cy.get('.auth-form').should('be.visible');

    cy.visit('/register');
    cy.get('.auth-form').should('be.visible');
  });

  it('Nawigacja pomiędzy wszystkimi głównymi stronami', () => {
    const pages = ['/', '/login', '/register'];

    pages.forEach((page) => {
      cy.visit(page);
      cy.get('.header').should('be.visible');
      cy.get('.logo').should('be.visible');
    });
  });
});
