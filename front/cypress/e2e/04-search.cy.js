describe('04 - Funkcjonalność wyszukiwania', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Pasek wyszukiwania powinien być widoczny', () => {
    cy.get('.search-bar').should('be.visible');
  });

  it('Powinien mieć placeholder "Search"', () => {
    cy.get('.search-bar').should('have.attr', 'placeholder', 'Search');
  });

  it('Powinien zawierać przycisk submit z ikoną', () => {
    cy.get('.form-search-bar button[type="submit"]').should('be.visible');
    cy.get('.search-icon').should('be.visible');
  });

  it('Powinien umożliwić wpisanie tekstu', () => {
    const searchQuery = 'Inception';
    cy.get('.search-bar').type(searchQuery);
    cy.get('.search-bar').should('have.value', searchQuery);
  });

  it('Powinien przekierować na stronę wyników po wyszukaniu', () => {
    cy.get('.search-bar').type('Matrix');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
    cy.url().should('include', 'title=Matrix');
  });

  it('Powinien zakodować specjalne znaki w URL', () => {
    cy.get('.search-bar').type('Star Wars');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', 'title=Star%20Wars');
  });

  it('Pusty submit powinien przekierować na /search bez parametrów', () => {
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });
});
