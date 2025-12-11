Feature: Przeglądanie i wyszukiwanie filmów
  Jako użytkownik (zalogowany lub nie)
  Chcę przeglądać listę filmów
  Aby znaleźć interesujące mnie pozycje

  Background:
    Given w systemie istnieją następujące filmy:
      | tytuł            | gatunek | rok  | ocena | czas_trwania |
      | Matrix           | Sci-Fi  | 1999 | 4.8   | 136          |
      | Matrix Reloaded  | Sci-Fi  | 2003 | 4.2   | 138          |
      | Incepcja         | Sci-Fi  | 2010 | 4.9   | 148          |
      | Ojciec Chrzestny | Dramat  | 1972 | 5.0   | 175          |
      | Kung Fu Panda    | Komedia | 2008 | 4.5   | 92           |

  Scenario: Pobranie listy wszystkich filmów
    When użytkownik pobiera listę wszystkich filmów
    Then system zwraca listę zawierającą 5 filmów
    And każdy film na liście ma tytuł i rok produkcji

  Scenario: Wyszukiwanie filmu po tytule
    When użytkownik wyszukuje filmy wpisując frazę "Matrix"
    Then system zwraca listę zawierającą 2 filmów
    And wszystkie zwrócone filmy zawierają w tytule "Matrix"

  Scenario: Filtrowanie filmów po gatunku
    When użytkownik filtruje filmy po gatunku "Komedia"
    Then system zwraca listę zawierającą 1 film
    And na liście znajduje się film "Kung Fu Panda"

  Scenario: Wyświetlanie szczegółów filmu
    When użytkownik pobiera szczegóły filmu "Incepcja"
    Then system zwraca poprawne szczegóły filmu
    And zwrócony tytuł to "Incepcja"
    And zwrócony rok produkcji to 2010

  Scenario: Sortowanie filmów po ocenie
    When użytkownik sortuje filmy według oceny malejąco
    Then system zwraca listę zawierającą 5 filmów posortowanych według oceny
    And pierwszy film na liście to "Ojciec Chrzestny"
    And ostatni film na liście to "Matrix Reloaded"

  Scenario: Sortowanie filmów po roku produkcji
    When użytkownik sortuje filmy według roku produkcji rosnąco
    Then system zwraca listę zawierającą 5 filmów posortowanych według roku produkcji
    And pierwszy film na liście to "Ojciec Chrzestny"
    And ostatni film na liście to "Incepcja"

  Scenario: Wyszukiwanie filmu, który nie istnieje
    When użytkownik wyszukuje filmy wpisując frazę "Nieistniejący film"
    Then system zwraca pustą listę filmów
    And komunikat informuje, że nie znaleziono żadnych filmów