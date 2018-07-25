package de.test.automatedTests.loginTests;

import de.test.automatedTests.config.AbstractAcceptanceTest;
import de.test.automatedTests.managers.ApplicationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static de.test.automatedTests.managers.loginDates.*;
import static de.test.automatedTests.utils.LoginUtils.loginOnPage;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    @DataProvider(name = "testDates")
    public Object[][] testDates() {

        return new Object[][]{
                {USER_BED_PASS_GOOD.getUsername(), USER_BED_PASS_GOOD.getPassword()},
                {USER_BED_PASS_BED.getUsername(), USER_BED_PASS_BED.getPassword()},
                {USER_GOOD_PASS_BED.getUsername(), USER_GOOD_PASS_BED.getPassword()},
                {USER2_BED_PASS_GOOD.getUsername(), USER2_BED_PASS_GOOD.getPassword()},
                {USER3_BED_PASS_GOOD.getUsername(), USER3_BED_PASS_GOOD.getPassword()}
        };
    }

    @Test(dataProvider = "testDates")
    public void loginTest(String user, String password) {
        //try login to the page
        loginOnPage(user, password, getWebDriver());

        //wait for the page to load
        WebElement alertMessage = new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#lblResult")));

        //verify fail login
        Assert.assertEquals(alertMessage.getText(), "Your username or password is incorrect. Please try again.");
    }
}
