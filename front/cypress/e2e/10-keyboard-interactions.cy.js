describe('10 - Interakcje z klawiaturą', () => {
  context('Formularz logowania', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Pola formularza powinny być focusowalne', () => {
      cy.get('input[name="username"]').focus();
      cy.focused().should('have.attr', 'name', 'username');

      cy.get('input[name="password"]').focus();
      cy.focused().should('have.attr', 'name', 'password');
    });

    it('Enter w polu password powinien submitować formularz', () => {
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123{enter}');
      // Formularz powinien zostać wysłany
    });

    it('Pola powinny akceptować input z klawiatury', () => {
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="username"]').should('have.value', 'testuser');

      cy.get('input[name="password"]').type('password123');
      cy.get('input[name="password"]').should('have.value', 'password123');
    });
  });

  context('Pasek wyszukiwania', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Enter w pasku wyszukiwania powinien rozpocząć wyszukiwanie', () => {
      cy.get('.search-bar').type('Matrix{enter}');
      cy.url().should('include', '/search');
      cy.url().should('include', 'title=Matrix');
    });

    it('Pole wyszukiwania powinno być focusowalne i akceptować input', () => {
      cy.get('.search-bar').focus();
      cy.focused().should('have.class', 'search-bar');
      cy.get('.search-bar').type('test');
      cy.get('.search-bar').should('have.value', 'test');
    });
  });

  context('Nawigacja po linkach', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Linki powinny być focusowalne', () => {
      cy.get('a').first().focus();
      cy.focused().should('be.visible');
    });

    it('Kliknięcie linku powinno działać', () => {
      cy.contains('a', 'Login').click();
      cy.url().should('include', '/login');
    });
  });
});
