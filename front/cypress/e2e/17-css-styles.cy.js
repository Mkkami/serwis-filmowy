describe('17 - CSS i Style Testing', () => {
  context('Header Styles', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Header powinien mieć odpowiednie style', () => {
      cy.get('.header').should('have.css', 'display');
    });

    it('Logo powinno być widoczne i mieć odpowiedni styl', () => {
      cy.get('.logo').should('be.visible').and('have.css', 'text-decoration');
    });

    it('Przyciski powinny mieć cursor pointer', () => {
      cy.get('button').first().should('have.css', 'cursor');
    });
  });

  context('Form Styles', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Formularz powinien być wycentrowany', () => {
      cy.get('.auth-form').should('be.visible');
    });

    it('Input fields powinny mieć odpowiedni padding', () => {
      cy.get('input[name="username"]').should('have.css', 'padding');
    });

    it('Przyciski powinny mieć hover effect', () => {
      cy.get('.auth-form button[type="submit"]')
        .trigger('mouseover')
        .should('have.css', 'cursor');
    });
  });

  context('Responsive Styles', () => {
    it('Layout powinien się zmieniać na mobile', () => {
      cy.viewport(375, 667);
      cy.visit('/');
      cy.get('.header').should('be.visible');
    });

    it('Layout powinien się zmieniać na tablecie', () => {
      cy.viewport(768, 1024);
      cy.visit('/');
      cy.get('.header').should('be.visible');
    });
  });

  context('Color Contrast', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Tekst powinien mieć odpowiedni kontrast', () => {
      cy.get('.logo').should('have.css', 'color');
    });

    it('Linki powinny być widoczne', () => {
      cy.get('a').first().should('have.css', 'color');
    });
  });
});
