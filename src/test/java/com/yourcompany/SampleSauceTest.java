package com.yourcompany;

import static org.junit.Assert.*;

import com.saucelabs.common.SauceOnDemandAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import java.net.URL;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.*;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;


/**
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple emulators in parallel.
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher} which will invoke the Sauce REST API to mark
 * the test as passed or failed.
 *
 * @author Neil Manvar
 */
@RunWith(ConcurrentParameterized.class)
public class SampleSauceTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY"));

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
    
    @Rule public TestName name = new TestName() {
        public String getMethodName() {
        		return String.format("%s", super.getMethodName());
        };
    };

    /**
     * Represents the platform to be used as part of the test run (i.e. Android).
     */
    private String platformName;
    /**
     * Represents the device name to be used as part of the test run. (i.e. Android Emulator, Google Nexus 7 HD Emulator)
     */
    private String deviceName;
    /**
     * Represents the version of the platform to be used as part of the test run. (i.e. 4.3)
     */
    private String platformVersion;
    /**
     * Location of the app
     */
    private String app;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    private String sessionId;

    /**
     * The {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private AppiumDriver driver;
    
    private List<Integer> values;

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 10;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param platformName
     * @param deviceName
     * @param platformVersion
     * @param app
     */

    public SampleSauceTest(String platformName, String deviceName, String platformVersion, String app) {
        super();
        this.platformName = platformName;
        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
        this.app = app;
    }

    /**
     * @return a LinkedList containing String arrays representing the mobile device emulator combinations the test should be run against.
     * The values in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();
        
        browsers.add(new String[]{"Android", "Android Emulator", "4.3", "http://saucelabs.com/example_files/ContactManager.apk"});
        browsers.add(new String[]{"Android", "Google Nexus 7 HD Emulator", "4.4", "http://saucelabs.com/example_files/ContactManager.apk"});

        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("app", app);
        
        capabilities.setCapability("browserName", "");
        capabilities.setCapability("appiumVersion", "1.4.10");
        capabilities.setCapability("deviceOrientation", "portrait");

        capabilities.setCapability("name", name.getMethodName());	

        this.driver = new AndroidDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);

        this.sessionId = driver.getSessionId().toString();
    }


    @Test
    public void testUIComputation() {

    	WebElement addContactButton = driver.findElement(By.name("Add Contact"));
    	addContactButton.click();
    	
    	List<WebElement> textFieldsList = driver.findElementsByClassName("android.widget.EditText");
    	textFieldsList.get(0).sendKeys("Some Name");
    	textFieldsList.get(2).sendKeys("Some@example.com");
    	driver.findElementByName("Save").click();
    }


    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }
}
