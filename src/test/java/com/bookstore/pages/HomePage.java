package com.bookstore.pages;

import com.bookstore.base.BasePage;
import com.bookstore.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for https://books.toscrape.com/
 *
 * ENCAPSULATION: locators are private; tests can only use the public behaviour methods.
 */
public class HomePage extends BasePage {

    // ---- private locators (hidden from the step definitions)
    private final By heading = By.cssSelector("div.page-header h1");
    private final By bookCards = By.cssSelector("article.product_pod");
    private final By categoryLinks = By.cssSelector("div.side_categories ul.nav-list ul li a");
    private final By nextButton = By.cssSelector("li.next a");
    private final By pagerText = By.cssSelector("li.current");

    // ---- public behaviour
    public void openHomePage() {
        open(ConfigReader.get("base.url"));
        waitForTitleContains("Books to Scrape");
    }

    public String getHeading() {
        return getText(heading);
    }

    public int getBookCount() {
        return getElements(bookCards).size();
    }

    public List<String> getCategoryNames() {
        return getElements(categoryLinks).stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public void selectCategory(String categoryName) {
        By category = By.xpath("//div[contains(@class,'side_categories')]//ul[contains(@class,'nav-list')]"
                + "//ul//a[normalize-space()='" + categoryName + "']");
        log.info("Selecting category '{}'", categoryName);
        click(category);
    }

    public void openBook(String bookTitle) {
        By book = By.xpath("//article[contains(@class,'product_pod')]//h3/a[@title=\"" + bookTitle + "\"]");
        log.info("Opening book '{}'", bookTitle);
        click(book);
    }

    public void clickNextPage() {
        click(nextButton);
    }

    /** Returns text such as "Page 2 of 50" with whitespace normalised. */
    public String getPagerText() {
        return getText(pagerText).replaceAll("\\s+", " ");
    }

    public String getFirstBookTitle() {
        WebElement first = getElements(bookCards).get(0);
        return getAttribute(first.findElement(By.cssSelector("h3 a")), "title");
    }
}
