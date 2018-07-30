package de.test.automatedTests.managers;

import com.sdl.selenium.web.WebLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePageMananger {
    private WebDriver driver;

    public HomePageMananger(WebDriver driver) {

        this.driver = driver;
    }

    public static String ACTIONS_MENU_SELECTOR = ".fa.fa-tasks";
    public static String PROFORMA_BUTTON_SELECTOR = ".fa.fa-file-text-o";

    public void goToPraformaPage(String menu, String submenu) {

        WebLocator menuWl = new WebLocator().setClasses("title").setText(menu);
        menuWl.click();

       WebElement subMenu=driver.findElement(By.cssSelector(PROFORMA_BUTTON_SELECTOR));
       subMenu.click();

    }
}
