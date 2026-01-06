describe('12 - Wydajność ładowania strony', () => {
  it('Strona główna powinna załadować się w rozsądnym czasie', () => {
    const startTime = Date.now();

    cy.visit('/', {
      onBeforeLoad: (win) => {
        win.performance.mark('page-start');
      },
      onLoad: (win) => {
        win.performance.mark('page-end');
        win.performance.measure('page-load', 'page-start', 'page-end');
      },
    });

    cy.window().then((win) => {
      const loadTime = Date.now() - startTime;
      expect(loadTime).to.be.lessThan(5000); // Powinno załadować się w mniej niż 5 sekund
    });

    cy.get('.header').should('be.visible');
  });

  it('Strona logowania powinna załadować się szybko', () => {
    const startTime = Date.now();

    cy.visit('/login');

    cy.window().then((win) => {
      const loadTime = Date.now() - startTime;
      expect(loadTime).to.be.lessThan(3000);
    });
  });

  it('Zasoby statyczne powinny się załadować', () => {
    cy.visit('/');

    cy.window().then((win) => {
      const resources = win.performance.getEntriesByType('resource');
      expect(resources.length).to.be.greaterThan(0);
    });
  });

  it('DOM powinien być gotowy szybko', () => {
    cy.visit('/');

    cy.window().then((win) => {
      const domContentLoaded =
        win.performance.timing.domContentLoadedEventEnd -
        win.performance.timing.navigationStart;
      expect(domContentLoaded).to.be.lessThan(3000);
    });
  });
});
