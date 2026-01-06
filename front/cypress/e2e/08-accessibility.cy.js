describe('08 - Test dostępności (Accessibility)', () => {
  context('Strona główna', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Powinna zawierać główne landmark elementy', () => {
      cy.get('header').should('exist');
    });

    it('Wszystkie przyciski powinny być dostępne przez klawiaturę', () => {
      cy.get('button').each(($button) => {
        cy.wrap($button).should('not.have.attr', 'tabindex', '-1');
      });
    });

    it('Linki powinny mieć odpowiedni tekst', () => {
      cy.get('a').each(($link) => {
        cy.wrap($link).invoke('text').should('not.be.empty');
      });
    });
  });

  context('Formularz logowania', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Pola input powinny mieć atrybuty name', () => {
      cy.get('input[name="username"]').should('exist');
      cy.get('input[name="password"]').should('exist');
    });

    it('Hasło powinno być typu password', () => {
      cy.get('input[name="password"]').should('have.attr', 'type', 'password');
    });

    it('Formularz powinien mieć przycisk submit', () => {
      cy.get('button[type="submit"]').should('exist');
    });
  });

  context('Obrazy i ikony', () => {
    beforeEach(() => {
      cy.visit('/');
    });

    it('Ikony powinny mieć atrybut alt', () => {
      cy.get('img').each(($img) => {
        cy.wrap($img).should('have.attr', 'alt');
      });
    });
  });
});
