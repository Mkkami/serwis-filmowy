describe('03 - Funkcjonalność rejestracji', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('Powinna wyświetlić formularz rejestracji', () => {
    cy.get('.auth-form').should('be.visible');
    cy.get('h1').should('contain', 'Register');
  });

  it('Powinna wyświetlić pola username i password', () => {
    cy.get('input[name="username"]').should('be.visible');
    cy.get('input[name="password"]').should('be.visible');
  });

  it('Powinna wyświetlić przycisk Register', () => {
    cy.get('.auth-form button[type="submit"]').should('be.visible');
  });

  it('Powinna wyświetlić link do logowania', () => {
    cy.contains('a', 'Login').should('be.visible');
  });

  it('Link do logowania powinien przekierowywać na stronę logowania', () => {
    cy.contains('a', 'Login').click();
    cy.url().should('include', '/login');
  });

  it('Walidacja - hasło powinno mieć minimum 8 znaków', () => {
    cy.get('input[name="username"]').type('newuser');
    cy.get('input[name="password"]').type('short');
    cy.get('.auth-form button[type="submit"]').click();
    // Sprawdź czy jest wyświetlony błąd walidacji
    cy.url().should('include', '/register');
  });
});
