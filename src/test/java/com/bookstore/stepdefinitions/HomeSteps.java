package com.bookstore.stepdefinitions;

import com.bookstore.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class HomeSteps {

    private final HomePage homePage = new HomePage();

    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        homePage.openHomePage();
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String expected) {
        String actual = homePage.getPageTitle();
        Assert.assertTrue(actual.contains(expected),
                "Page title '" + actual + "' does not contain '" + expected + "'");
    }

    @Then("the page heading should be {string}")
    public void thePageHeadingShouldBe(String expected) {
        Assert.assertEquals(homePage.getHeading(), expected, "Unexpected page heading");
    }

    @Then("{int} books should be displayed on the page")
    public void booksShouldBeDisplayed(int expected) {
        Assert.assertEquals(homePage.getBookCount(), expected, "Unexpected number of books on the page");
    }

    @Then("the category list should contain {string}")
    public void theCategoryListShouldContain(String category) {
        Assert.assertTrue(homePage.getCategoryNames().contains(category),
                "Category '" + category + "' not found in the sidebar");
    }

    @When("the user clicks the next page button")
    public void theUserClicksTheNextPageButton() {
        homePage.clickNextPage();
    }

    @Then("the page indicator should show {string}")
    public void thePageIndicatorShouldShow(String expected) {
        Assert.assertEquals(homePage.getPagerText(), expected, "Unexpected pagination text");
    }

    @Then("the URL should contain {string}")
    public void theUrlShouldContain(String fragment) {
        Assert.assertTrue(homePage.getCurrentUrl().contains(fragment),
                "URL '" + homePage.getCurrentUrl() + "' does not contain '" + fragment + "'");
    }
}
