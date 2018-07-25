package de.test.automatedTests.managers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ToolsManager {


    private static final Logger logger = LoggerFactory.getLogger(ToolsManager.class);


    private WebDriver driver;

    public ToolsManager(WebDriver driver) {
        this.driver = driver;
    }

    public void openEngineeringUI() {

        List<WebElement> engineeringUI = driver.findElements(By.cssSelector("b-app-menu b-menu b-item"));
        engineeringUI.get(10).click();

    }

}
