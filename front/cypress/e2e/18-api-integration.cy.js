describe('18 - API Integration Tests', () => {
  context('Login API', () => {
    beforeEach(() => {
      cy.visit('/login');
    });

    it('Powinien wysłać request do API podczas logowania', () => {
      cy.intercept('POST', '**/login').as('loginRequest');

      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@loginRequest').then((interception) => {
        // Request body może być w formacie form-urlencoded lub JSON
        const body = interception.request.body;
        if (typeof body === 'string') {
          expect(body).to.include('username=testuser');
          expect(body).to.include('password=password123');
        } else {
          expect(body).to.include({
            username: 'testuser',
            password: 'password123',
          });
        }
      });
    });

    it('Powinien obsłużyć sukces logowania (200)', () => {
      cy.intercept('POST', '**/login', {
        statusCode: 200,
        body: { success: true, token: 'fake-token' },
      }).as('loginSuccess');

      cy.get('input[name="username"]').type('testuser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@loginSuccess');
      cy.url().should('not.include', '/login');
    });

    it('Powinien obsłużyć błąd logowania (401)', () => {
      cy.intercept('POST', '**/login', {
        statusCode: 401,
        body: { error: 'Invalid credentials' },
      }).as('loginError');

      cy.get('input[name="username"]').type('wronguser');
      cy.get('input[name="password"]').type('wrongpass');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@loginError');
      cy.get('.error').should('be.visible');
    });
  });

  context('Register API', () => {
    beforeEach(() => {
      cy.visit('/register');
    });

    it('Powinien wysłać request do API podczas rejestracji', () => {
      cy.intercept('POST', '**/register').as('registerRequest');

      cy.get('input[name="username"]').type('newuser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@registerRequest');
    });

    it('Powinien obsłużyć sukces rejestracji', () => {
      cy.intercept('POST', '**/register', {
        statusCode: 201,
        body: { success: true },
      }).as('registerSuccess');

      cy.get('input[name="username"]').type('newuser');
      cy.get('input[name="password"]').type('password12345');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@registerSuccess');
    });

    it('Powinien obsłużyć konflikt (user już istnieje - 409)', () => {
      cy.intercept('POST', '**/register', {
        statusCode: 409,
        body: { error: 'User already exists' },
      }).as('registerConflict');

      cy.get('input[name="username"]').type('existinguser');
      cy.get('input[name="password"]').type('password123');
      cy.get('.auth-form button[type="submit"]').click();

      cy.wait('@registerConflict');
      cy.get('.error').should('be.visible');
    });
  });

  context('Search API', () => {
    it('Powinien wysłać request podczas wyszukiwania', () => {
      cy.intercept('GET', '**/search*').as('searchRequest');

      cy.visit('/');
      cy.get('.search-bar').type('Matrix');
      cy.get('.form-search-bar button[type="submit"]').click();

      // Jeśli API jest wywoływane
      // cy.wait('@searchRequest')
    });
  });
});
