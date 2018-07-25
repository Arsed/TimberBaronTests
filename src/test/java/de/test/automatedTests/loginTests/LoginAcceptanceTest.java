package de.test.automatedTests.loginTests;

import de.test.automatedTests.config.AbstractAcceptanceTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static de.test.automatedTests.managers.loginDate.USER_ADMIN;
import static de.test.automatedTests.utils.LoginUtils.loginOnPage;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    @DataProvider(name = "testDates")
    public Object[][] testDates() {

        return new Object[][]{
                {USER_ADMIN.getUsername(), USER_ADMIN.getPassword()}
        };
    }

    @Test(dataProvider = "testDates")
    public void loginTest(String user, String password) {

        loginOnPage(getWebDriver(), user, password);
    }
}
