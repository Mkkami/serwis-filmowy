Feature: Recenzowanie filmów
  Jako zalogowany użytkownik
  Chcę pisać recenzje filmów
  Aby podzielić się opinią

  Background:
    Given użytkownik "agnieszka" jest zalogowany
    And w systemie istnieje film "Parasite"

  Scenario: Dodawanie recenzji do filmu
    When użytkownik dodaje recenzję do filmu "Parasite" z oceną 5 i komentarzem "Rewelacyjne kino"
    Then recenzja zostaje zapisana w systemie
    And średnia ocena filmu "Parasite" wynosi 5.0

  Scenario: Próba dodania recenzji bez oceny
    When użytkownik próbuje dodać recenzję do filmu "Parasite" bez oceny
    Then system odrzuca recenzję

  Scenario: Przeglądanie recenzji filmu
    Given w systemie istnieje film "Tenet"
    And film "Tenet" posiada recenzję użytkownika "user1" z oceną 4
    And film "Tenet" posiada recenzję użytkownika "user2" z oceną 2
    When użytkownik wyświetla szczegóły filmu "Tenet"
    Then lista recenzji zawiera 2 pozycje
    And średnia ocena filmu "Tenet" wynosi 3.0