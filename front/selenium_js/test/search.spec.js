// test/search.spec.js
// Odpowiednik: cypress/e2e/04-search.cy.js i 13-search-results.cy.js
// Kryterium c) wyszukiwanie elementów na stronie internetowej

const { By, Key, until } = require('selenium-webdriver');
const { expect } = require('chai');
const { createDriver } = require('../utils/driverFactory');

describe('Selenium: Search Functionality (Comparison with Cypress)', function () {
  let driver;
  const baseUrl = 'http://localhost:5173';

  this.timeout(30000); // Selenium często wymaga dłuższych timeoutów niż Cypress

  beforeEach(async function () {
    driver = await createDriver();
  });

  afterEach(async function () {
    if (driver) {
      await driver.quit();
    }
  });

  it('Should display search elements correctly (Visibility)', async function () {
    // Porównanie: cy.visit('/')
    await driver.get(baseUrl + '/');

    // Porównanie: cy.get('.search-bar').should('be.visible')
    const searchInput = await driver.findElement(By.css('.search-bar'));
    expect(await searchInput.isDisplayed()).to.be.true;

    // Porównanie: .and('have.attr', 'placeholder', 'Search')
    expect(await searchInput.getAttribute('placeholder')).to.equal('Search');

    // Porównanie: cy.get('.form-search-bar button[type="submit"]')
    // W Selenium wyszukiwanie po zagnieżdżonym CSS jest podobne
    const submitBtn = await driver.findElement(
      By.css('.form-search-bar button[type="submit"]')
    );
    expect(await submitBtn.isDisplayed()).to.be.true;
  });

  it('Should type and search using button click', async function () {
    await driver.get(baseUrl + '/');
    const searchTerm = 'Matrix';

    // Porównanie: cy.get('...').type('Matrix')
    const searchInput = await driver.findElement(By.css('.search-bar'));
    await searchInput.sendKeys(searchTerm);

    // Asercja wartości inputa
    const value = await searchInput.getAttribute('value');
    expect(value).to.equal(searchTerm);

    // Kliknięcie
    const submitBtn = await driver.findElement(
      By.css('.form-search-bar button[type="submit"]')
    );
    await submitBtn.click();

    // Oczekiwanie na zmianę URL - kryterium e)
    // W Selenium trzeba jawnie używać wait.until, w Cypress jest to automatyczne
    await driver.wait(until.urlContains('/search'), 5000);

    const currentUrl = await driver.getCurrentUrl();
    expect(currentUrl).to.include(`title=${searchTerm}`);
  });

  it('Should search using Enter key', async function () {
    await driver.get(baseUrl + '/');

    const searchInput = await driver.findElement(By.css('.search-bar'));
    await searchInput.sendKeys('Inception', Key.RETURN);

    await driver.wait(until.urlContains('/search'), 5000);
    const currentUrl = await driver.getCurrentUrl();
    expect(currentUrl).to.include('title=Inception');
  });

  it('Should persist search term in input on results page', async function () {
    const searchTerm = 'Avatar';
    await driver.get(baseUrl + '/');

    const searchInput = await driver.findElement(By.css('.search-bar'));
    await searchInput.sendKeys(searchTerm, Key.RETURN);

    await driver.wait(until.urlContains('/search'), 5000);

    // W Selenium musimy ponownie znaleźć element po przeładowaniu strony (StaleElementReferenceException risk)
    // To ważny punkt do wniosków o "wyszukiwaniu elementów"
    const resultsInput = await driver.findElement(By.css('.search-bar'));
    const inputValue = await resultsInput.getAttribute('value');
    expect(inputValue).to.equal(searchTerm);
  });

  it('Should handle special characters in search', async function () {
    await driver.get(baseUrl + '/');
    const searchInput = await driver.findElement(By.css('.search-bar'));

    await searchInput.sendKeys('Star Wars', Key.RETURN);
    await driver.wait(until.urlContains('/search'), 5000);

    const currentUrl = await driver.getCurrentUrl();
    // W Selenium sami musimy obsłużyć enkodowanie URL w asercji, jeśli driver zwraca encoded
    expect(currentUrl).to.satisfy(
      (url) => url.includes('Star%20Wars') || url.includes('Star Wars')
    );
  });
});
