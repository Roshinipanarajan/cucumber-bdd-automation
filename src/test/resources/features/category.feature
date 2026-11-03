@regression @category
Feature: Books to Scrape - Browse by category
  As a visitor
  I want to filter books by category
  So that I only see books I am interested in

  Scenario Outline: Selecting a category shows its books
    Given the user is on the home page
    When the user selects the "<category>" category
    Then the category heading should be "<category>"
    And the result count should be <count>
    And the breadcrumb should be displayed

    Examples:
      | category | count |
      | Travel   | 11    |
      | Mystery  | 32    |
      | Poetry   | 19    |
      | Humor    | 10    |
