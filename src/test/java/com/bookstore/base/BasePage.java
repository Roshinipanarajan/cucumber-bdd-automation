package com.bookstore.base;

import com.bookstore.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Parent of every page object (inherits Driver). Wraps Selenium calls with
 * explicit waits, logging and exception handling so page classes stay small.
 */
public class BasePage extends Driver {

    protected final Logger log = LogManager.getLogger(getClass());

    private WebDriverWait waitFor() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    protected void open(String url) {
        log.info("Opening {}", url);
        getDriver().get(url);
    }

    protected void click(By locator) {
        try {
            waitFor().until(ExpectedConditions.elementToBeClickable(locator)).click();
            log.info("Clicked {}", locator);
        } catch (TimeoutException e) {
            log.error("Element not clickable within the timeout: {}", locator);
            throw e;
        }
    }

    protected void click(WebElement element) {
        try {
            waitFor().until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (TimeoutException e) {
            log.error("Element not clickable within the timeout");
            throw e;
        }
    }

    protected String getText(By locator) {
        try {
            String text = waitFor().until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
            log.info("Read text '{}' from {}", text, locator);
            return text;
        } catch (TimeoutException e) {
            log.error("Element not visible within the timeout: {}", locator);
            throw e;
        }
    }

    protected String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    protected List<WebElement> getElements(By locator) {
        try {
            return waitFor().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("No elements became visible for {}", locator);
            throw e;
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitFor().until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            log.warn("Element is not displayed: {}", locator);
            return false;
        }
    }

    protected void waitForUrlContains(String fragment) {
        waitFor().until(ExpectedConditions.urlContains(fragment));
    }

    protected void waitForTitleContains(String fragment) {
        waitFor().until(ExpectedConditions.titleContains(fragment));
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
}
