package com.bookstore.base;

import com.bookstore.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

/**
 * Reusable base class that owns the WebDriver.
 *
 * INHERITANCE: BasePage extends Driver, and every page object extends BasePage,
 * so all pages share one driver-management implementation.
 *
 * The driver is held in a static ThreadLocal. It is "static" so pages and hooks can reach it
 * without passing it around, and "ThreadLocal" so every parallel scenario thread gets its
 * own isolated browser (required for parallel execution / Selenium Grid).
 */
public class Driver {

    private static final Logger log = LogManager.getLogger(Driver.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    /** Returns the WebDriver bound to the current thread. */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialised for thread "
                    + Thread.currentThread().getName() + ". Call Driver.initDriver() first.");
        }
        return driver;
    }

    /** Creates a local or remote (Selenium Grid) browser session for the current thread. */
    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        String execution = ConfigReader.get("execution").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");

        log.info("Starting browser={}, execution={}, headless={}", browser, execution, headless);

        try {
            WebDriver driver = execution.equals("grid")
                    ? createRemoteDriver(browser, headless)
                    : createLocalDriver(browser, headless);

            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
            driver.manage().window().maximize();
            DRIVER.set(driver);
        } catch (MalformedURLException e) {
            log.error("Invalid Selenium Grid URL: {}", ConfigReader.get("grid.url"), e);
            throw new IllegalStateException("Invalid grid.url", e);
        } catch (RuntimeException e) {
            log.error("Could not start the {} browser", browser, e);
            throw e;
        }
    }

    /** Quits the browser and removes it from the current thread. Safe to call more than once. */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Browser closed");
            } catch (RuntimeException e) {
                log.warn("Problem while closing the browser: {}", e.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }

    // ------------------------------------------------------------------ helpers

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser) {
            case "firefox":
                return new FirefoxDriver((FirefoxOptions) buildOptions(browser, headless));
            case "edge":
                return new EdgeDriver((EdgeOptions) buildOptions(browser, headless));
            case "chrome":
            default:
                return new ChromeDriver((ChromeOptions) buildOptions("chrome", headless));
        }
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless) throws MalformedURLException {
        MutableCapabilities options = buildOptions(browser, headless);
        return new RemoteWebDriver(URI.create(ConfigReader.get("grid.url")).toURL(), options);
    }

    private static MutableCapabilities buildOptions(String browser, boolean headless) {
        switch (browser) {
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                options.addArguments("--width=1920", "--height=1080");
                return options;
            }
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--disable-gpu");
                return options;
            }
            case "chrome":
            default: {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--no-sandbox",
                        "--disable-dev-shm-usage", "--disable-gpu", "--remote-allow-origins=*");
                return options;
            }
        }
    }
}
