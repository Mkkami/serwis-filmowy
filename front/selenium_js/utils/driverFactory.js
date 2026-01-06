// utils/driverFactory.js
const { Builder } = require('selenium-webdriver');
const { Options } = require('selenium-webdriver/chrome');

// Konfiguracja drivera - odpowiednik beforeEach/before w Cypress
async function createDriver() {
  const options = new Options();

  // Włączamy tryb headless dla większej stabilności i szybkości (fix timeout issues)
  options.addArguments('--headless');
  options.addArguments('--window-size=1280,720');
  options.addArguments('--no-sandbox');
  options.addArguments('--disable-dev-shm-usage');

  const driver = await new Builder()
    .forBrowser('chrome')
    .setChromeOptions(options)
    .build();

  // Ustawienie implicit wait (Selenium way) vs Cypress built-in waiting
  // To jest kluczowe dla punktu e) kryteriów porównania
  await driver.manage().setTimeouts({ implicit: 10000 });

  return driver;
}

module.exports = { createDriver };
