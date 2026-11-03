@smoke @home
Feature: Books to Scrape - Home page
  As a visitor
  I want the home page to load with the product catalogue and navigation
  So that I can start browsing books

  Scenario: Home page loads with the book catalogue
    Given the user is on the home page
    Then the page title should contain "Books to Scrape"
    And the page heading should be "All products"
    And 20 books should be displayed on the page

  Scenario: Category sidebar lists the main categories
    Given the user is on the home page
    Then the category list should contain "Travel"
    And the category list should contain "Mystery"
    And the category list should contain "Poetry"

  @pagination
  Scenario: Navigating to the second page of products
    Given the user is on the home page
    When the user clicks the next page button
    Then the page indicator should show "Page 2 of 50"
    And the URL should contain "page-2.html"
