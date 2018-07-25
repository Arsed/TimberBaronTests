package de.test.automatedTests.config;

import com.sdl.selenium.utils.config.WebDriverConfig;
import de.test.automatedTests.managers.ApplicationManager;
import de.test.automatedTests.managers.ToolsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * abstract class for the main application
 */

@ContextConfiguration(locations = {"classpath:spring/applicationContext-acceptance-test.xml"})
public class AbstractAcceptanceTest extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAcceptanceTest.class);
    private static WebDriver driver;

    protected ApplicationManager applicationManager;
    protected ToolsManager toolsManager;

    @Value("${chrome.driver.location}")
    private String chromeDriverLocation;
    @Value("${app.url}")
    private String applicationURL;

    protected static WebDriver getWebDriver() {
        return driver;
    }

    @BeforeClass
    public void setup() {
        setupChromeDriver();
    }

    @BeforeMethod
    public void openApplication() {
        logger.info("------------------------ Test start ------------------------");
        createWebDriver();

        //init testy
        WebDriverConfig.init(driver);

        applicationManager = new ApplicationManager(driver);
        toolsManager = new ToolsManager(driver);

        logger.info("Opening application " + applicationURL);
        driver.get(applicationURL);

        new WebDriverWait(driver, ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#txtUsername")));
    }

    @AfterMethod
    public void destroy() {
        quitDriver();
        logger.info("------------------------ Test finish ------------------------");
    }

    /**
     * Set chrome driver location as system property.
     */
    private void setupChromeDriver() {
        logger.debug("Retrieving chrome driver from location: " + chromeDriverLocation);

        String chromeDriverPath = getChromeDriverPath();

        if (chromeDriverPath != null) {
            setChromeDriverPath(chromeDriverPath);
        } else {
            String errorMessage = "Chrome driver not found";
            logger.error(errorMessage);

            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Check if the configured chrome driver path is relative or absolute and return the absolute path.
     *
     * @return absolute path of chrome driver
     */
    private String getChromeDriverPath() {
        String chromeDriverPath = null;

        Path path = Paths.get(chromeDriverLocation);
        if (Files.exists(path)) {
            chromeDriverPath = chromeDriverLocation;
        } else {
            URL chromeDriver = AbstractAcceptanceTest.class.getResource(chromeDriverLocation);
            if (chromeDriver != null) {
                chromeDriverPath = chromeDriver.getPath();
            }
        }

        return chromeDriverPath;
    }

    /**
     * Sets Chrome driver path system property.
     *
     * @param chromeDriverPath chrome driver executable path
     */
    private void setChromeDriverPath(String chromeDriverPath) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    private void createWebDriver() {
        logger.debug("Initalizing chrome getWebDriver()..");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        driver = new ChromeDriver(chromeOptions);
    }

    private void quitDriver() {
        if (driver != null) {
            getWebDriver().quit();

            logger.debug("Chrome driver has quit.");
        }
    }

    public String getApplicationErrorPage() {
        return applicationURL + "/error";
    }

    public String getApplicationURL() {
        return applicationURL;
    }

}
