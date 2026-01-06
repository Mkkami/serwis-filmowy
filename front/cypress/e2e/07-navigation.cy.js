describe('07 - Nawigacja między stronami', () => {
  it('Powinien przejść z głównej strony do logowania', () => {
    cy.visit('/');
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');
  });

  it('Powinien przejść z logowania do rejestracji', () => {
    cy.visit('/login');
    cy.contains('a', 'Register').click();
    cy.url().should('include', '/register');
  });

  it('Powinien przejść z rejestracji do logowania', () => {
    cy.visit('/register');
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');
  });

  it('Powinien przejść do strony wyszukiwania', () => {
    cy.visit('/');
    cy.get('.search-bar').type('test');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });

  it('Logo powinno zawsze prowadzić do strony głównej', () => {
    cy.visit('/login');
    cy.get('.logo').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    cy.visit('/register');
    cy.get('.logo').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');
  });

  it('Przycisk wstecz w przeglądarce powinien działać', () => {
    cy.visit('/');
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');
    cy.go('back');
    cy.url().should('eq', Cypress.config().baseUrl + '/');
  });
});
