package com.bookstore.utils;

import com.bookstore.base.Driver;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Captures a screenshot of the current thread's browser, attaches it to the
 * Cucumber scenario (which the Allure plugin turns into a report attachment)
 * and also saves a copy under target/screenshots.
 */
public final class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    private ScreenshotUtil() {
    }

    public static void capture(Scenario scenario, String label) {
        try {
            byte[] bytes = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(bytes, "image/png", label);

            Files.createDirectories(SCREENSHOT_DIR);
            String fileName = label.replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + System.currentTimeMillis() + ".png";
            Files.write(SCREENSHOT_DIR.resolve(fileName), bytes);
            log.info("Screenshot captured: {}", fileName);
        } catch (WebDriverException | IOException | IllegalStateException e) {
            // A failed screenshot must never hide the real test result.
            log.error("Could not capture screenshot '{}': {}", label, e.getMessage());
        }
    }
}
