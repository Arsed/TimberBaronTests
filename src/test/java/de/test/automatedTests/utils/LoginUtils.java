package de.test.automatedTests.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginUtils {

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

    }
}
