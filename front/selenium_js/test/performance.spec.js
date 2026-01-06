// test/performance.spec.js
// Odpowiednik: cypress/e2e/12-performance.cy.js i 20-stress-testing.cy.js
// Kryteria: e) oczekiwanie na wczytanie, f) szybkość, g) obciążenie

const { By, until, Key } = require('selenium-webdriver');
const { expect } = require('chai');
const { createDriver } = require('../utils/driverFactory');

describe('Selenium: Performance & Stress Testing', function () {
  let driver;
  const baseUrl = 'http://localhost:5173';

  this.timeout(120000); // Stress testy trwają dłużej

  beforeEach(async function () {
    driver = await createDriver();
  });

  afterEach(async function () {
    if (driver) {
      await driver.quit();
    }
  });

  // --- Sekcja PERFORMANCE ---

  it('Should load homepage within acceptable time (Page Load Speed)', async function () {
    const start = Date.now();
    await driver.get(baseUrl + '/');

    // Wait for specific element ensures interaction readiness
    await driver.wait(until.elementLocated(By.css('.header')), 5000);
    const end = Date.now();

    const duration = end - start;
    console.log(`Selenium Homepage Load Time: ${duration}ms`);
    expect(duration).to.be.below(5000);
  });

  it('Should measure DOM Interactive time via Navigation Timing API', async function () {
    await driver.get(baseUrl + '/');

    // Wykonanie JavaScript w przeglądarce - analogia do cy.window()
    const domInteractive = await driver.executeScript(
      'return performance.timing.domInteractive - performance.timing.navigationStart;'
    );

    console.log(`DOM Interactive: ${domInteractive}ms`);
    expect(domInteractive).to.be.below(3000);
  });

  // --- Sekcja STRESS TESTING ---

  it('Should handle rapid navigation (Stress Test)', async function () {
    // Symulacja szybkiego klikania w nawigację
    await driver.get(baseUrl + '/');

    // W Cypress to może być szybsze ze względu na brak narzutu HTTP WebDrivera dla każdej komendy
    for (let i = 0; i < 5; i++) {
      const loginLink = await driver.findElement(By.linkText('Login'));
      await loginLink.click();
      await driver.wait(until.urlContains('/login'), 2000);

      // Back simulation
      await driver.navigate().back();
      // Po back() elementy DOM są nowe - trzeba czekać
      await driver.wait(until.elementLocated(By.linkText('Login')), 2000);
    }
  });

  it('Should handle large input stress test', async function () {
    await driver.get(baseUrl + '/login');

    const hugeString = 'a'.repeat(5000); // 5000 znaków
    const usernameInput = await driver.findElement(By.name('username'));

    // Selenium wysyła znaki często jeden po drugim, co może trwać dłużej niż w Cypress (dla dużych stringów)
    // W tym przypadku sendKeys dla dużego stringa jest dobrym testem wydajności drivera
    const start = Date.now();
    await usernameInput.sendKeys(hugeString);
    const end = Date.now();

    console.log(`Time to type 5000 chars: ${end - start}ms`);

    const value = await usernameInput.getAttribute('value');
    expect(value.length).to.equal(5000);
  });

  it('Should handle rapid searches in sequence', async function () {
    const queries = ['Matrix', 'Avatar', 'Inception', 'Titanic'];

    for (const query of queries) {
      await driver.get(baseUrl + '/');
      const searchInput = await driver.findElement(By.css('.search-bar'));

      await searchInput.sendKeys(query, Key.RETURN);
      await driver.wait(until.urlContains(`/search`), 3000);

      // Weryfikacja
      const url = await driver.getCurrentUrl();
      expect(url).to.include('title=' + query);
    }
  });
});
