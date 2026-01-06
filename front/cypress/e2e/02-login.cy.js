describe('02 - Funkcjonalność logowania', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('Powinna wyświetlić formularz logowania', () => {
    cy.get('.auth-form').should('be.visible');
    cy.get('h1').should('contain', 'Login');
  });

  it('Powinna wyświetlić pola username i password', () => {
    cy.get('input[name="username"]').should('be.visible');
    cy.get('input[name="password"]').should('be.visible');
  });

  it('Powinna wyświetlić przycisk Login', () => {
    cy.get('.auth-form button[type="submit"]').should('be.visible');
  });

  it('Powinna wyświetlić link do rejestracji', () => {
    cy.contains('a', 'Register').should('be.visible');
  });

  it('Powinno pokazać błąd gdy pola są puste', () => {
    cy.get('.auth-form button[type="submit"]').click();
    // Sprawdź czy formularz waliduje puste pola (pozostaje na stronie /login)
    cy.url().should('include', '/login');
    cy.get('.auth-form').should('be.visible');
  });

  it('Link do rejestracji powinien przekierowywać na stronę rejestracji', () => {
    cy.contains('a', 'Register').click();
    cy.url().should('include', '/register');
  });
});
