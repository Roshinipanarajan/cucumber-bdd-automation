@regression @book
Feature: Books to Scrape - Book details
  As a visitor
  I want to open a book from the catalogue
  So that I can see its price, availability and rating

  Scenario: Viewing the details of a book
    Given the user is on the home page
    When the user opens the book "A Light in the Attic"
    Then the book title should be "A Light in the Attic"
    And the book price should be "£51.77"
    And the book should be in stock
    And the book rating should be "Three"
    And the book description should be displayed

  # Intentionally wrong expected price. Excluded by default (see TestRunner tags).
  # Run it to see a screenshot-on-failure in the Allure report:
  #   mvn test -Dcucumber.filter.tags="@demo-failure"
  @demo-failure
  Scenario: Demo - failing scenario to show the failure screenshot
    Given the user is on the home page
    When the user opens the book "A Light in the Attic"
    Then the book price should be "£0.00"
