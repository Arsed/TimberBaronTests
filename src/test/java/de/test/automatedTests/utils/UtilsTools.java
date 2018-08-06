package de.test.automatedTests.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class UtilsTools {
    public static void scrollWitJavaScrip(int x,int y,WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollBy("+x+","+y+")");
    }


    public static void switchToLastHandle(WebDriver driver) {
        for (String windowName : driver.getWindowHandles()) {
            driver.switchTo().window(windowName);
        }
    }
}
