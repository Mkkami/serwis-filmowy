describe('20 - Stress Testing i Edge Cases', () => {
  context('Rapid Clicking', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien obsłużyć wielokrotne kliknięcie przycisku submit', () => {
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123');

      // Szybkie wielokrotne kliknięcie
      cy.get('.auth-form button[type="submit"]').click().click().click();

      // Aplikacja powinna obsłużyć to gracefully
    });

    it('Powinien obsłużyć szybkie przełączanie między stronami', () => {
      cy.contains('a', 'Register').click();
      cy.contains('a', 'Login').click();
      cy.contains('a', 'Register').click();
      cy.url().should('include', '/register');
    });
  });

  context('Concurrent Actions', () => {
    it('Powinien obsłużyć równoczesne wpisywanie w wiele pól', () => {
      cy.visit('/login');
      cy.get('input[name="username"]').type('user');
      cy.get('input[name="password"]').type('pass');
      cy.get('input[name="username"]').clear().type('newuser');
      cy.get('input[name="password"]').clear().type('newpass');
    });

    it('Powinien obsłużyć szybkie wyszukiwanie po sobie', () => {
      cy.visit('/');

      const searches = ['Matrix', 'Avatar', 'Inception', 'Interstellar'];

      searches.forEach((term) => {
        cy.get('.search-bar').clear().type(term);
        cy.get('.form-search-bar button[type="submit"]').click();
        cy.url().should('include', `/search`);
        cy.go('back');
      });
    });
  });

  context('Memory Leaks Prevention', () => {
    it('Powinien obsłużyć wielokrotne odświeżanie strony', () => {
      cy.visit('/');
      for (let i = 0; i < 5; i++) {
        cy.reload();
        cy.get('.header').should('be.visible');
      }
    });

    it('Powinien obsłużyć wielokrotne nawigowanie tam i z powrotem', () => {
      cy.visit('/');

      for (let i = 0; i < 5; i++) {
        cy.contains('a', 'Login').click();
        cy.url().should('include', '/login');
        cy.go('back');
        cy.url().should('eq', Cypress.config().baseUrl + '/');
      }
    });
  });

  context('Large Data Input', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien obsłużyć wklejanie dużej ilości tekstu', () => {
      const largeText = 'a'.repeat(10000);
      cy.get('input[name="username"]').invoke('val', largeText);
    });

    it('Powinien obsłużyć wyszukiwanie z bardzo długim zapytaniem', () => {
      cy.visit('/');
      const longQuery = 'test '.repeat(100);
      cy.get('.search-bar').type(longQuery.substring(0, 500));
      cy.get('.form-search-bar button[type="submit"]').click();
      cy.url().should('include', '/search');
    });
  });

  context('Browser Back/Forward Navigation', () => {
    it('Powinien poprawnie obsługiwać historię przeglądarki', () => {
      cy.visit('/');
      cy.contains('a', 'Login').click();
      cy.url().should('include', '/login');

      cy.go('back');
      cy.url().should('eq', Cypress.config().baseUrl + '/');

      cy.go('forward');
      cy.url().should('include', '/login');

      cy.go('back');
      cy.url().should('eq', Cypress.config().baseUrl + '/');
    });

    it('Stan aplikacji powinien być zachowany po back/forward', () => {
      cy.visit('/');
      cy.get('.search-bar').type('Matrix');
      cy.get('.form-search-bar button[type="submit"]').click();
      cy.url().should('include', '/search');

      cy.go('back');
      cy.url().should('eq', Cypress.config().baseUrl + '/');
      // Pasek wyszukiwania zachowuje wartość Matrix
      cy.get('.search-bar').should('have.value', 'Matrix');

      cy.go('forward');
      cy.url().should('include', '/search');
      cy.get('.search-bar').should('have.value', 'Matrix');
    });
  });
});
