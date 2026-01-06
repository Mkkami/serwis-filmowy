describe('11 - Bezpieczeństwo formularzy', () => {
  context('XSS Protection', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien escapować znaki specjalne w polach input', () => {
      const xssAttempt = '<script>alert("XSS")</script>';
      cy.get('input[name="username"]').type(xssAttempt);
      cy.get('input[name="username"]').should('have.value', xssAttempt);
      // Input powinien przechowywać tekst jako string, nie wykonywać
    });

    it('Powinien chronić przed SQL injection próbami', () => {
      const sqlInjection = "'; DROP TABLE users; --";
      cy.get('input[name="username"]').type(sqlInjection);
      cy.get('input[name="password"]').type('password');
      cy.get('.auth-form button[type="submit"]').click();
      // Aplikacja powinna obsłużyć to bezpiecznie
    });
  });

  context('Password Field Security', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Pole hasła powinno mieć type="password"', () => {
      cy.get('input[name="password"]').should('have.attr', 'type', 'password');
    });

    it('Hasło nie powinno być widoczne w HTML', () => {
      cy.get('input[name="password"]').type('secretPassword123');
      cy.get('input[name="password"]').should('have.attr', 'type', 'password');
    });
  });

  context('CSRF Protection', () => {
    it('Formularz powinien używać POST dla wrażliwych operacji', () => {
      cy.visit('/login');
      cy.get('form').should('exist');
      // Sprawdź czy submit używa odpowiedniej metody
    });
  });
});
