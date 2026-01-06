describe('05 - Komponent Header - responsywność i interakcje', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Header powinien być widoczny', () => {
    cy.get('.header').should('be.visible');
  });

  it('Header powinien zawierać logo, nawigację i profil', () => {
    cy.get('.header .logo').should('be.visible');
    cy.get('.header nav').should('be.visible');
    cy.get('.header .profile').should('be.visible');
  });

  it('Header powinien być stały podczas przewijania', () => {
    // Dodaj wysokość do body żeby móc scrollować
    cy.document().then((doc) => {
      const div = doc.createElement('div');
      div.style.height = '2000px';
      doc.body.appendChild(div);
    });

    cy.scrollTo(0, 500);
    cy.get('.header').should('be.visible');
  });

  it('Wszystkie elementy nawigacji powinny być klikalne', () => {
    cy.get('.logo').should('not.be.disabled');
    cy.get('.profile a').should('not.be.disabled');
  });

  it('Header powinien wyświetlać się poprawnie na mniejszych ekranach', () => {
    cy.viewport(768, 1024); // tablet
    cy.get('.header').should('be.visible');
    cy.get('.logo').should('be.visible');
  });

  it('Header powinien wyświetlać się poprawnie na mobile', () => {
    cy.viewport(375, 667); // iPhone
    cy.get('.header').should('be.visible');
    cy.get('.logo').should('be.visible');
  });
});
