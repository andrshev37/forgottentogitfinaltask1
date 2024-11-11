package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final WebDriver driver = WebDriverSingleton.getDriver();

    public static void main(String[] args) {
        try {
            performUC1();
            performUC2();
            performUC3();
        } finally {
            driver.quit();
        }
    }


    private static void performUC1() {
        logger.info("Starting UC1");
        navigateToLoginPage();
        enterCredentials("", "");

        clickLoginButton();
        String errorMessage = getErrorMessage();
        try{
        MatcherAssert.assertThat("UC1: Error message should match 'Username is required'",
                errorMessage, Matchers.containsString("Username is required"));
                //errorMessage, Matchers.equalTo("Username is required"));
        logger.info("UC1 completed.");
        } catch (AssertionError e) {
            logger.error("UC1 failed: " + e.getMessage());
        }
    }

    private static void performUC2() {
        logger.info("Starting UC2");
        clearFields();
        enterCredentials("1", "");

        clickLoginButton();
        String errorMessage = getErrorMessage();
        try {


            MatcherAssert.assertThat("UC2: Error message should match 'Password is required'",
                    errorMessage, Matchers.containsString("Password is required"));
                    //errorMessage, Matchers.equalTo("Password is required"));
            logger.info("UC2 completed.");
        }catch (AssertionError e) {
        logger.error("UC1 failed: " + e.getMessage());
    }
    }

    private static void performUC3() {
        logger.info("Starting UC3");
        clearFields();
        enterCredentials("performance_glitch_user", "secret_sauce");

        clickLoginButton();
        String dashboardText = getDashboardText();
        try {


        MatcherAssert.assertThat("UC3: Dashboard title should match 'Swag Labs'",
                dashboardText, Matchers.containsString("Swag Labs"));

        logger.info("UC3 completed.");
        } catch (AssertionError e) {
            logger.error("UC1 failed: " + e.getMessage());
        }
    }

    private static void navigateToLoginPage() {
        driver.get("https://www.saucedemo.com/");
    }

    private static void enterCredentials(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
    }

    private static void clearFields() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.clear();
        passwordField.clear();
    }

    private static void clickLoginButton() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
    }

    private static String getErrorMessage() {
        WebElement errorMessage = driver.findElement(By.xpath("//*[@id='login_button_container']/div/form/div[3]"));
        return errorMessage.getText();
    }

    private static String getDashboardText() {
        WebElement dashboard = driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[1]/div[2]/div"));
        return dashboard.getText();
    }
}

// Singleton for WebDriver
class WebDriverSingleton {
    private static WebDriver driver;

    private WebDriverSingleton() {}

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = System.getProperty("browser", "firefox");
            switch (browser.toLowerCase()) {
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                default:
                    driver = new EdgeDriver();
                    break;
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return driver;
    }
}
