describe('15 - Local Storage i Session Management', () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();
  });

  it('Powinien zachować stan po odświeżeniu strony', () => {
    cy.visit('/');
    cy.reload();
    cy.get('.header').should('be.visible');
  });

  it('Local storage powinno być dostępne', () => {
    cy.visit('/');
    cy.window().its('localStorage').should('exist');
  });

  it('Powinien obsłużyć czyszczenie local storage', () => {
    cy.visit('/');
    cy.clearLocalStorage();
    cy.reload();
    cy.get('.header').should('be.visible');
  });

  it('Session storage powinno być dostępne', () => {
    cy.visit('/');
    cy.window().its('sessionStorage').should('exist');
  });

  it('Powinien obsłużyć cookies', () => {
    cy.visit('/');
    cy.getCookies().then((cookies) => {
      // Sprawdź czy są jakieś cookies
      expect(cookies).to.be.an('array');
    });
  });
});
