describe('01 - Strona główna i nawigacja', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Powinna wyświetlić stronę główną', () => {
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.get('.header').should('be.visible');
  });

  it('Powinna wyświetlić logo FilmBase', () => {
    cy.get('.logo').should('be.visible').and('contain', 'FilmBase');
  });

  it('Powinien zawierać link do logowania gdy użytkownik jest wylogowany', () => {
    cy.get('.profile').should('contain', 'Login');
  });

  it('Powinien zawierać pasek wyszukiwania', () => {
    cy.get('.search-bar').should('be.visible');
  });

  it('Logo powinno przekierowywać na stronę główną', () => {
    cy.visit('/login');
    cy.get('.logo').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');
  });
});
