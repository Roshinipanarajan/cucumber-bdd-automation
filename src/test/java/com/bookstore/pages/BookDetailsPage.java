package com.bookstore.pages;

import com.bookstore.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for a single book's detail page. */
public class BookDetailsPage extends BasePage {

    private final By title = By.cssSelector("div.product_main h1");
    private final By price = By.cssSelector("div.product_main p.price_color");
    private final By availability = By.cssSelector("div.product_main p.availability");
    private final By rating = By.cssSelector("div.product_main p.star-rating");
    private final By description = By.cssSelector("#product_description ~ p");

    public String getTitle() {
        return getText(title);
    }

    public String getPrice() {
        return getText(price);
    }

    public String getAvailability() {
        return getText(availability);
    }

    public boolean isInStock() {
        return getAvailability().toLowerCase().contains("in stock");
    }

    /** Star rating is encoded as a CSS class, e.g. "star-rating Three" -> "Three". */
    public String getRating() {
        WebElement element = getElements(rating).get(0);
        String classes = getAttribute(element, "class");
        return classes.replace("star-rating", "").trim();
    }

    public boolean isDescriptionDisplayed() {
        return isDisplayed(description);
    }
}
