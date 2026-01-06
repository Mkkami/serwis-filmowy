describe('06 - Walidacja formularzy', () => {
  context('Formularz logowania', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien walidować puste pole username', () => {
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();
      cy.url().should('include', '/login');
    });

    it('Powinien walidować puste pole password', () => {
      cy.get('input[name="username"]').type('testuser');
      cy.get('.auth-form button[type="submit"]').click();
      cy.url().should('include', '/login');
    });

    it('Powinien walidować oba puste pola', () => {
      cy.get('.auth-form button[type="submit"]').click();
      cy.url().should('include', '/login');
    });
  });

  context('Formularz rejestracji', () => {
    beforeEach(() => {
      cy.visit('/register');
    });

    it('Powinien walidować puste pole username', () => {
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();
      cy.url().should('include', '/register');
    });

    it('Powinien walidować krótkie hasło (mniej niż 8 znaków)', () => {
      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('pass');
      cy.get('.auth-form button[type="submit"]').click();
      cy.url().should('include', '/register');
    });
  });
});
