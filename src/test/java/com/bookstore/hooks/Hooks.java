package com.bookstore.hooks;

import com.bookstore.base.Driver;
import com.bookstore.utils.ConfigReader;
import com.bookstore.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Cucumber hooks: browser start-up before, and screenshot + clean-up after each scenario. */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("=== START: {} (thread {}) ===", scenario.getName(), Thread.currentThread().getName());
        Driver.initDriver();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                log.error("=== FAILED: {} ===", scenario.getName());
                ScreenshotUtil.capture(scenario, "FAILED - " + scenario.getName());
            } else if (ConfigReader.getBoolean("screenshot.on.pass")) {
                ScreenshotUtil.capture(scenario, "PASSED - " + scenario.getName());
            }
        } finally {
            Driver.quitDriver();
            log.info("=== END: {} -> {} ===", scenario.getName(), scenario.getStatus());
        }
    }
}
