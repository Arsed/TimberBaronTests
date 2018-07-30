package de.test.automatedTests.utils;

import de.test.automatedTests.managers.ApplicationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginUtils {

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

