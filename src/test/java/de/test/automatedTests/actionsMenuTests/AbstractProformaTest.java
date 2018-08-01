package de.test.automatedTests.actionsMenuTests;

import de.test.automatedTests.config.AbstractAcceptanceTest;
import de.test.automatedTests.managers.HomePageMananger;
import de.test.automatedTests.managers.ProformaManager;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractProformaTest extends AbstractAcceptanceTest {

    protected HomePageMananger homePageMananger;
    protected ProformaManager proformaManager;

    @BeforeMethod
    public void init() {
        homePageMananger = new HomePageMananger(getWebDriver());
        proformaManager = new ProformaManager(getWebDriver());
    }

}
