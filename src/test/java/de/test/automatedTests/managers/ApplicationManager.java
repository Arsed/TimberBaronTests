package de.test.automatedTests.managers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationManager {


    private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    public static final int WAIT_TIME_OUT_IN_10_SECONDS = 10;
    public static final int WAIT_TIME_OUT_IN_20_SECONDS = 20;
    private WebDriver driver;

    public ApplicationManager(WebDriver driver) {
        this.driver = driver;
    }

    public static void loginOnPageWithWrongData(String username, String password, WebDriver driver) {

        //send username in username field
        WebElement userInput = driver.findElement(By.cssSelector("#txtUsername"));
        userInput.clear();
        userInput.sendKeys(username);

        //send password in password field
        WebElement passwordInput = driver.findElement(By.cssSelector("#txtPassword"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        //click login button
        WebElement loginButton = driver.findElement(By.cssSelector("#btnLogin"));
        loginButton.click();

    }

    public static void loginOnPage(String username, String password, WebDriver driver) {

        //send username in username field
        WebElement userInput = driver.findElement(By.cssSelector("#txtUsername"));
        userInput.clear();
        userInput.sendKeys(username);

        //send password in password field
        WebElement passwordInput = driver.findElement(By.cssSelector("#txtPassword"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        //click login button
        WebElement loginButton = driver.findElement(By.cssSelector("#btnLogin"));
        loginButton.click();
        //wait loading page
        new WebDriverWait(driver, ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-hamburger")));

    }



}
