describe('16 - Multiple Browser Support', () => {
  const pages = ['/', '/login', '/register'];

  pages.forEach((page) => {
    context(`Page: ${page}`, () => {
      it('Powinien działać poprawnie w Chrome', () => {
        cy.visit(page);
        cy.get('.header').should('be.visible');
      });

      it('Powinien działać poprawnie w Firefox', () => {
        cy.visit(page);
        cy.get('.header').should('be.visible');
      });

      it('Powinien działać poprawnie w Edge', () => {
        cy.visit(page);
        cy.get('.header').should('be.visible');
      });
    });
  });

  it('Formularz powinien działać we wszystkich przeglądarkach', () => {
    cy.visit('/login');
    cy.get('input[name="username"]').type('testuser');
    cy.get('input[name="password"]').type('password123');
    cy.get('button[type="submit"]').should('be.visible');
  });

  it('Wyszukiwanie powinno działać we wszystkich przeglądarkach', () => {
    cy.visit('/');
    cy.get('.search-bar').type('Test');
    cy.get('.form-search-bar button[type="submit"]').click();
    cy.url().should('include', '/search');
  });
});
