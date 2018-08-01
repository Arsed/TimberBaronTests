package de.test.automatedTests.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class UtilsTools {
    public static void scrollWitJavaScrip(int x,int y,WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollBy("+x+","+y+")");
    }

    public static float converFloat(float number,int decimalPart) {

        int zero=10^decimalPart;
        return (float) (Math.floor(number *zero) /zero);
    }
}
