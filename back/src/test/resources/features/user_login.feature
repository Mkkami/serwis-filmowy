Feature: Logowanie i wylogowanie użytkownika
  Jako zarejestrowany użytkownik
  Chcę mieć możliwość zalogowania i wylogowania się z systemu
  Aby uzyskać dostęp do chronionych zasobów

  Scenario: Pomyślne logowanie użytkownika
    Given użytkownik "jan_kowalski" z hasłem "Haslo123!" istnieje w systemie
    When użytkownik loguje się używając nazwy "jan_kowalski" i hasła "Haslo123!"
    Then logowanie powinno zakończyć się sukcesem

  Scenario: Nieudane logowanie z błędnym hasłem
    Given użytkownik "jan_kowalski" z hasłem "Haslo123!" istnieje w systemie
    When użytkownik loguje się używając nazwy "jan_kowalski" i hasła "ZleHaslo123!"
    Then logowanie powinno zakończyć się błędem autoryzacji

  Scenario: Wylogowanie użytkownika
    Given użytkownik "jan_kowalski" z hasłem "Haslo123!" jest zalogowany do systemu
    When użytkownik wylogowuje się
    Then wylogowanie powinno zakończyć się sukcesem

  Scenario: Próba logowania z nieistniejącym użytkownikiem
    Given użytkownik "nieistniejacy_user" nie istnieje w systemie
    When użytkownik loguje się używając nazwy "nieistniejacy_user" i hasła "JakiesHaslo123!"
    Then logowanie powinno zakończyć się błędem autoryzacji