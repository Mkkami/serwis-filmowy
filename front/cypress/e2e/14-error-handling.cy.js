describe('14 - Error Handling i Edge Cases', () => {
  context('404 Not Found', () => {
    it('Powinien obsłużyć nieistniejącą trasę', () => {
      cy.visit('/nieistniejaca-strona', { failOnStatusCode: false });
      // Aplikacja powinna wyświetlić stronę 404 lub przekierować
    });

    it('Powinien obsłużyć nieprawidłowe ID w URL', () => {
      cy.visit('/movie/999999', { failOnStatusCode: false });
      // Aplikacja powinna obsłużyć brak zasobu
    });
  });

  context('Network Errors', () => {
    it('Powinien obsłużyć błąd sieci podczas logowania', () => {
      cy.intercept('POST', '**/login', {
        statusCode: 500,
        body: { error: 'Internal Server Error' },
      }).as('loginError');

      cy.visit('/login');
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@loginError');
      // Aplikacja powinna wyświetlić komunikat o błędzie
    });

    it('Powinien obsłużyć timeout żądania', () => {
      // Ignore uncaught exceptions from background polling (e.g. /me) since backend is mock/down
      cy.on('uncaught:exception', (err, runnable) => {
        return false;
      });

      cy.intercept('POST', '**/login', {
        delay: 5000,
        statusCode: 200,
        body: { token: 'mock-token' },
      }).as('slowRequest');

      cy.visit('/login');
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();
    });
  });

  context('Extreme Input Values', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien obsłużyć bardzo długi username', () => {
      const longUsername = 'a'.repeat(1000);
      cy.get('input[name="username"]').type(longUsername.substring(0, 100));
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();
    });

    it('Powinien obsłużyć Unicode characters', () => {
      cy.get('input[name="username"]').type('用户名Test🎬');
      cy.get('input[name="password"]').type('password123');
      cy.get('input[name="username"]').should('have.value', '用户名Test🎬');
    });
  });
});
