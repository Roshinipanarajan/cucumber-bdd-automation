package com.bookstore.pages;

import com.bookstore.base.BasePage;
import org.openqa.selenium.By;

/** Page Object for a category listing page (e.g. Travel, Mystery). */
public class CategoryPage extends BasePage {

    private final By heading = By.cssSelector("div.page-header h1");
    private final By resultCount = By.cssSelector("form.form-horizontal strong");
    private final By bookCards = By.cssSelector("article.product_pod");
    private final By breadcrumb = By.cssSelector("ul.breadcrumb");

    public String getCategoryHeading() {
        return getText(heading);
    }

    /** The first bold number in "11 results - showing 1 to 11". */
    public int getResultCount() {
        return Integer.parseInt(getText(resultCount));
    }

    public int getDisplayedBookCount() {
        return getElements(bookCards).size();
    }

    public boolean isBreadcrumbDisplayed() {
        return isDisplayed(breadcrumb);
    }
}
