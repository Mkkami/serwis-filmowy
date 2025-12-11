Feature: Rejestracja użytkownika
  Jako nowy użytkownik
  Chcę utworzyć konto w systemie
  Aby móc korzystać z serwisu filmowego

  Scenario: Pomyślna rejestracja użytkownika
    Given użytkownik nie istnieje w systemie
    When użytkownik próbuje zarejestrować się z nazwą "nowy_user" i hasłem "StrongPass123!"
    Then rejestracja powinna zakończyć się sukcesem
    And w systemie powinien istnieć użytkownik o nazwie "nowy_user"

  Scenario: Próba rejestracji z istniejącą nazwą użytkownika
    Given użytkownik o nazwie "istniejacy_user" już istnieje w systemie
    When użytkownik próbuje zarejestrować się z nazwą "istniejacy_user" i hasłem "AnyPass123!"
    Then rejestracja powinna zakończyć się błędem
    And komunikat błędu powinien informować o zajętej nazwie użytkownika

  Scenario: Rejestracja ze słabym hasłem
    Given użytkownik nie istnieje w systemie
    When użytkownik próbuje zarejestrować się z nazwą "slabe_haslo_user" i hasłem "123"
    Then rejestracja powinna zakończyć się błędem walidacji
    And komunikat błędu powinien informować o zbyt słabym haśle
