# Porównanie narzędzi do testów automatycznych: Selenium vs Cypress

## 1. Kryteria porównania

### a) Instalacja i dodatkowa konfiguracja narzędzi

#### **Selenium**
- **Proces instalacji**: 
  - Biblioteka Selenium WebDriver przez npm
  - WebDriver dla wybranej przeglądarki (ChromeDriver, GeckoDriver, EdgeDriver)
  - Framework testowy (Mocha, Jest)
  
- **Konfiguracja**:
  - Wymaga zarządzania wersjami driverów
  - Dodatkowa konfiguracja frameworka testowego
  
- **Złożoność**: Średnia - więcej początkowego wysiłku niż Cypress

**Przykład instalacji**:
```bash
npm install selenium-webdriver --save-dev
npm install chromedriver --save-dev
```

#### **Cypress**
- **Proces instalacji**: 
  - Pojedyncza komenda npm instaluje wszystko
  - Nie wymaga instalacji dodatkowych driverów
  
- **Konfiguracja**:
  - Automatyczna konfiguracja po instalacji
  - Gotowa struktura katalogów
  
- **Złożoność**: Niska - gotowe w kilka minut

- Cypress działa wyłącznie w JavaScript/TypeScript - wymaga Node.js

**Przykład instalacji**:
```bash
npm install cypress --save-dev
npx cypress open
```

**Wnioski**:
- **Cypress** jest prostszy w instalacji, ale ograniczony do ekosystemu JavaScript
- **Selenium** wymaga więcej konfiguracji, ale wspiera wiele języków programowania


---

### b) Dokumentacja

#### **Selenium**
- Obszerna dokumentacja, ale rozproszona między różnymi językami
- Brak jednolitego przewodnika dla początkujących

#### **Cypress**
- Wyjątkowo dobra, dobrze zorganizowana dokumentacja
- Interaktywne przykłady i Real World App - pełna aplikacja demonstracyjna

**Wnioski**: **Cypress** ma lepszą dokumentację dla nowych użytkowników



---

### c) Wyszukiwanie elementów na stronie internetowej

#### **Selenium**
- ID, Name, Class Name, CSS Selectors, XPath (pełna obsługa)
- Wymaga jawnego oczekiwania na elementy

**Przykład (JavaScript)**:
```javascript
const { Builder, By, until } = require('selenium-webdriver');

const driver = await new Builder().forBrowser('chrome').build();
// Jawne oczekiwanie na element
const element = await driver.wait(
  until.elementLocated(By.css('.submit-button')), 
  10000
);
await element.click();
```

#### **Cypress**
- CSS Selectors, `cy.contains()` - wyszukiwanie po tekście
- Automatyczne retry i oczekiwanie
- Zalecane data attributes: `data-cy`, `data-test`

**Przykład**:
```javascript
// Automatyczne oczekiwanie
cy.get('[data-cy=submit-button]')
  .should('be.visible')
  .click()

cy.contains('Submit').click()
```

**Wnioski**:
- **Selenium** oferuje większą elastyczność (XPath)
- **Cypress** jest prostszy dzięki automatycznemu oczekiwaniu



---

### d) Zrównoleglenie testów

#### **Selenium**
- Selenium Grid - wymaga dodatkowej konfiguracji
- Integracja z chmurami testowymi (BrowserStack, Sauce Labs)
- Pełna kontrola nad infrastrukturą
- Darmowe przy własnej infrastrukturze

#### **Cypress**
- Natywne wsparcie dla równoległego wykonywania
- Cypress Cloud - płatna usługa (darmowa dla małych projektów)
- Bardzo prosta konfiguracja

**Przykład (Cypress)**:
```bash
cypress run --record --parallel --key=your-key
```

**Wnioski**:
- **Selenium** zapewnia większą kontrolę i możliwości
- **Cypress** oferuje prostszą konfigurację



---

### e) Oczekiwanie na wczytanie aplikacji webowej lub pojawienie się nowego elementu

#### **Selenium**
- Implicit Wait, Explicit Wait, Fluent Wait
- Wymaga ręcznej implementacji
- Ryzyko "flaky tests" przy niewłaściwej konfiguracji

**Przykład (JavaScript)**:
```javascript
const { Builder, By, until } = require('selenium-webdriver');

// Explicit Wait
const driver = await new Builder().forBrowser('chrome').build();
await driver.wait(until.elementLocated(By.id('submit')), 10000);
```

#### **Cypress**
- Automatyczne retry - każda komenda automatycznie czeka
- Automatycznie czeka na zakończenie XHR/fetch, animacje CSS, gotowość DOM
- Domyślny timeout: 4 sekundy (konfigurowalny)

**Przykład**:
```javascript
//Automatyczne oczekiwanie
cy.get('.loading').should('not.exist')
cy.get('.content').should('be.visible')

// Oczekiwanie na request
cy.intercept('GET', '/api/data').as('getData')
cy.wait('@getData')
```

**Wnioski**:
- **Cypress** znacząco redukuje "flaky tests" dzięki automatycznemu oczekiwaniu
- **Selenium** wymaga większej uwagi i doświadczenia



---

### f) Szybkość wykonywania testów

#### **Selenium**
- Komunikacja przez WebDriver (dodatkowy overhead)
- Headless mode znacznie przyspiesza testy
- Typowy czas: 5-15 sekund (headless) dla testu z 10 interakcjami

#### **Cypress**
- Działa w tym samym "run loop" co aplikacja - bezpośredni dostęp do DOM
- Szybsze dzięki architekturze
- Typowy czas: 2-8 sekund (headless) dla testu z 10 interakcjami

**Porównanie (suite 50 testów)**:
```
Selenium (headless, sekwencyjnie):     8-12 minut
Cypress (headless, sekwencyjnie):      4-7 minut
```

**Wnioski**: **Cypress** jest zazwyczaj szybszy, ale różnica maleje przy równoległym wykonywaniu



---

### g) Obciążenie procesora i pamięci RAM

#### **Selenium**
- Każda instancja przeglądarki: 200-500 MB RAM
- Procesor: średnie obciążenie (10-30% na rdzeń)
- 4 równoległe testy: ~1.5-2.5 GB RAM

#### **Cypress**
- Wyższe bazowe zużycie: Cypress App + przeglądarka + Node.js
- Procesor: wyższe obciążenie (20-40% na rdzeń)
- 4 równoległe testy: ~2-3 GB RAM
- Video recording dodatkowo zwiększa zużycie

**Wnioski**:
- **Selenium** jest bardziej efektywny pamięciowo
- **Różnice nie są krytyczne dla większości środowisk CI/CD**



---

### h) Generowanie raportów

#### **Selenium**
- Brak wbudowanego systemu raportowania
- Wymaga dodatkowych narzędzi (Allure, ExtentReports, Mochawesome)
- Screenshots i video trzeba implementować samodzielnie

#### **Cypress**
- Automatyczne screenshots przy niepowodzeniu testu
- Automatyczne nagrywanie video (w `cypress run`)
- Test Replay w Cypress Cloud

**Przykład konfiguracji (Cypress)**:
```javascript
// cypress.config.js
module.exports = {
  reporter: 'mochawesome',
  video: true,
  screenshotOnRunFailure: true
}
```

**Porównanie**:

Cypress ma:
- automatyczne screenshoty
- automatyczne nagrywanie wideo
- test replay

**Wnioski**: **Cypress** ma znacznie lepsze wbudowane wsparcie dla raportowania



---

## 3. Podsumowanie porównawcze

### Tabela porównawcza

| Kryterium | Selenium | Cypress | Zwycięzca |
|-----------|----------|---------|-----------|
| **Instalacja i konfiguracja** | Średnio złożona, wymaga driverów | Prosta, all-in-one | **Cypress** |
| **Dokumentacja** | Obszerna, ale rozproszona | Doskonała, spójna | **Cypress** |
| **Wyszukiwanie elementów** | Bardzo elastyczne (XPath) | Proste, automatyczne retry | **Remis** |
| **Zrównoleglenie testów** | Grid, pełna kontrola | Proste, ale płatne dla pełni możliwości | **Selenium** |
| **Oczekiwanie na elementy** | Ręczne, wymaga doświadczenia | Automatyczne, inteligentne | **Cypress** |
| **Szybkość testów** | Dobra (z optymalizacją) | Lepsza (out-of-the-box) | **Cypress** |
| **Obciążenie zasobów** | Niższe zużycie RAM | Wyższe zużycie RAM | **Selenium** |
| **Generowanie raportów** | Wymaga konfiguracji | Wbudowane, automatyczne | **Cypress** |

---

## 4. Wskazówki dotyczące wyboru narzędzia

### Kiedy wybrać **Selenium**:

1. **Testy cross-browser** - Safari, IE, Edge (starsze wersje), wiele przeglądarek jednocześnie
2. **Różne systemy operacyjne** - Windows, macOS, Linux z specyficznym środowiskiem
3. **Istniejąca infrastruktura** - zespół już używa Selenium lub Selenium Grid
4. **Preferowany język inny niż JavaScript** - zespół w Java, Python, C#, Ruby
5. **Integracja z narzędziami desktop/mobile** - Appium, automaty systemowe

### Kiedy wybrać **Cypress**:

1. **Nowoczesne aplikacje JavaScript** (React, Vue, Angular) - aplikacje SPA, dostęp do state
2. **Szybkie wdrożenie testów E2E** - nowy projekt, krótki czas na setup
3. **Developerzy piszący testy** - zespół JavaScript/TypeScript, testy w development workflow
4. **Testy API + E2E** - Cypress testuje API i mockuje responses
5. **Priorytet: stabilność** - automatyczne retry, mniej flaky tests
6. **Dobra dokumentacja** - szybki onboarding nowych osób

### Strategia hybrydowa:

Można używać obu narzędzi:
- **80% testów w Cypress** (Chrome, Firefox) - szybkie, stabilne
- **20% smoke testów w Selenium** (Safari, Edge, multi-OS) - cross-browser
- Cypress w CI/CD dla każdego PR
- Selenium nightly dla full regression

---

## 5. Podsumowanie

### Według doświadczenia zespołu:
- **Zespół JavaScript/TypeScript** → **Cypress**
- **Zespół Java/.NET/Python** → **Selenium**
- **Mieszany zespół** → **Cypress** (łatwiejsza nauka)

### Według architektury aplikacji:
- **SPA (React/Vue/Angular)** → **Cypress**
- **Multi-page tradycyjna** → **Oba mogą być dobre**
- **Wymagane wiele przeglądarek** → **Selenium**

### Według zasobów:
- **Małe/średnie zespoły** → **Cypress**
- **Duże organizacje** → **Selenium Grid**

### Ostateczna rekomendacja:

**Dla większości nowoczesnych projektów webowych** → **Cypress**
- Łatwiejszy start, szybsze wyniki, mniejsze utrzymanie

**Dla projektów wymagających szerokiego wsparcia przeglądarek** → **Selenium**
-Większa elastyczność, sprawdzone rozwiązanie

**Kluczowa różnica**: Cypress działa tylko z JavaScript/TypeScript, Selenium wspiera wiele języków
