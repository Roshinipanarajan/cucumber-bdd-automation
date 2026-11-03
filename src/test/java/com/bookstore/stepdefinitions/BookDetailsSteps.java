package com.bookstore.stepdefinitions;

import com.bookstore.pages.BookDetailsPage;
import com.bookstore.pages.HomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class BookDetailsSteps {

    private final HomePage homePage = new HomePage();
    private final BookDetailsPage bookPage = new BookDetailsPage();

    @When("the user opens the book {string}")
    public void theUserOpensTheBook(String title) {
        homePage.openBook(title);
    }

    @Then("the book title should be {string}")
    public void theBookTitleShouldBe(String expected) {
        Assert.assertEquals(bookPage.getTitle(), expected, "Unexpected book title");
    }

    @Then("the book price should be {string}")
    public void theBookPriceShouldBe(String expected) {
        Assert.assertEquals(bookPage.getPrice(), expected, "Unexpected book price");
    }

    @Then("the book should be in stock")
    public void theBookShouldBeInStock() {
        Assert.assertTrue(bookPage.isInStock(),
                "Book is not in stock. Availability text: " + bookPage.getAvailability());
    }

    @Then("the book rating should be {string}")
    public void theBookRatingShouldBe(String expected) {
        Assert.assertEquals(bookPage.getRating(), expected, "Unexpected star rating");
    }

    @Then("the book description should be displayed")
    public void theBookDescriptionShouldBeDisplayed() {
        Assert.assertTrue(bookPage.isDescriptionDisplayed(), "Book description is not displayed");
    }
}
