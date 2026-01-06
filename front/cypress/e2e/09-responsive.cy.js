describe('09 - Responsywność aplikacji', () => {
  const viewports = [
    { device: 'iPhone SE', width: 375, height: 667 },
    { device: 'iPhone 12 Pro', width: 390, height: 844 },
    { device: 'iPad', width: 768, height: 1024 },
    { device: 'iPad Pro', width: 1024, height: 1366 },
    { device: 'Desktop HD', width: 1280, height: 720 },
    { device: 'Desktop Full HD', width: 1920, height: 1080 },
  ];

  viewports.forEach((viewport) => {
    context(`${viewport.device} (${viewport.width}x${viewport.height})`, () => {
      beforeEach(() => {
        cy.viewport(viewport.width, viewport.height);
      });

      it('Strona główna powinna być widoczna i responsywna', () => {
        cy.visit('/');
        cy.get('.header').should('be.visible');
        cy.get('.logo').should('be.visible');
      });

      it('Formularz logowania powinien być responsywny', () => {
        cy.visit('/login');
        cy.get('.auth-form').should('be.visible');
        cy.get('input[name="username"]').should('be.visible');
        cy.get('input[name="password"]').should('be.visible');
        cy.get('button[type="submit"]').should('be.visible');
      });

      it('Pasek wyszukiwania powinien być widoczny', () => {
        cy.visit('/');
        cy.get('.search-bar').should('be.visible');
      });
    });
  });
});
