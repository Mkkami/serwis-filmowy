describe('13 - Testy Search Results', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Powinien wyświetlić stronę wyników wyszukiwania', () => {
    cy.get('.search-bar').type('Matrix');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });

  it('Parametr query powinien być zachowany w URL', () => {
    const searchTerm = 'Inception';
    cy.get('.search-bar').type(searchTerm);
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', `title=${searchTerm}`);
  });

  it('Powinien obsłużyć wyszukiwanie pustego stringa', () => {
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });

  it('Powinien zachować poprzednie wyszukiwanie w pasku', () => {
    cy.get('.search-bar').type('Avatar');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', 'title=Avatar');
    cy.get('.search-bar').should('have.value', 'Avatar');
  });

  it('Powinien umożliwić nowe wyszukiwanie ze strony wyników', () => {
    cy.get('.search-bar').type('Matrix');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', 'title=Matrix');

    cy.get('.search-bar').clear().type('Avatar');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', 'title=Avatar');
  });

  it('Powinien obsługiwać znaki specjalne w zapytaniu', () => {
    cy.get('.search-bar').type('Star & Wars');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });
});
