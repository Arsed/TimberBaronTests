package de.test.automatedTests.managers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public void loginPage(String usernameText, String passwordText) {

        WebElement username = driver.findElement(By.cssSelector("#txtUsername"));
        username.sendKeys(usernameText);

        WebElement password = driver.findElement(By.cssSelector("#txtPassword"));
        password.sendKeys(passwordText);

        WebElement btnLogin = driver.findElement(By.cssSelector("#btnLogin"));
        btnLogin.click();

    }

    public void openLeftMenu() {
        WebElement leftMenu = driver.findElement(By.cssSelector("b-app-header b-icon-button.right-button"));
        leftMenu.click();
    }
}
