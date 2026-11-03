package com.bookstore.stepdefinitions;

import com.bookstore.pages.CategoryPage;
import com.bookstore.pages.HomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CategorySteps {

    private final HomePage homePage = new HomePage();
    private final CategoryPage categoryPage = new CategoryPage();

    @When("the user selects the {string} category")
    public void theUserSelectsTheCategory(String category) {
        homePage.selectCategory(category);
    }

    @Then("the category heading should be {string}")
    public void theCategoryHeadingShouldBe(String expected) {
        Assert.assertEquals(categoryPage.getCategoryHeading(), expected, "Unexpected category heading");
    }

    @Then("the result count should be {int}")
    public void theResultCountShouldBe(int expected) {
        Assert.assertEquals(categoryPage.getResultCount(), expected, "Unexpected result count");
    }

    @Then("the breadcrumb should be displayed")
    public void theBreadcrumbShouldBeDisplayed() {
        Assert.assertTrue(categoryPage.isBreadcrumbDisplayed(), "Breadcrumb is not displayed");
    }
}
