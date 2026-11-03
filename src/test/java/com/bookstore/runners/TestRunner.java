package com.bookstore.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber TestRunner executed through testng.xml (and the Maven Surefire plugin).
 *
 * Override tags from the command line:  mvn test -Dcucumber.filter.tags="@smoke"
 * The @demo-failure scenario is excluded by default; it exists to demonstrate
 * screenshot-on-failure in the Allure report.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.bookstore.stepdefinitions", "com.bookstore.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "not @demo-failure"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /** parallel = true makes each scenario run on its own thread (see testng.xml for the thread count). */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
