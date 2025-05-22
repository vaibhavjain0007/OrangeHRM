package org.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.OrangeHRM;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrangeHRMTest {

    SoftAssert softAssert = new SoftAssert();
    private RemoteWebDriver driver;
    private OrangeHRM orangeHRM;

    private static final String USERNAME = "Admin";
    private static final String PASSWORD = "admin123";

    @BeforeClass()
    public void setUp() {
        // Set path to chrome driver
//        System.setProperty("webdriver.chrome.driver",
//                "C:\\Users\\Vaibhav Jain\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
//        WebDriverManager.edgedriver().setup();
//        WebDriverManager.chromedriver().setup();
//        System.setProperty("webdriver.chrome.driver",
//                "C:\\Users\\Vaibhav Jain\\Downloads\\edgedriver_win64\\msedgedriver.exe");
    }

    @BeforeMethod()
    public void init() throws Exception {
        // Set up the WebDriver instance
//        driver = new ChromeDriver();
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless");  // Add headless argument
//        driver = new EdgeDriver(options);
//        driver = new EdgeDriver();
//        driver = new ChromeDriver();
        FirefoxOptions options = new FirefoxOptions();
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        orangeHRM = new OrangeHRM(driver);
    }

    @Test()
    public void testUsernameAndPasswordFields() {
        // Verify that the Username and Password text fields should be null or blank
        Assert.assertTrue(orangeHRM.getUsernameText().isEmpty(), "username field should be empty by default");
        Assert.assertTrue(orangeHRM.getPasswordText().isEmpty(), "password field should be empty by default");
    }

    @Test()
    public void testLoginFunctionality() {
        orangeHRM.login(USERNAME, PASSWORD);
        Assert.assertTrue(orangeHRM.getItems().contains("Logout"), "Logout option should be displayed");
    }

//    @Test()
    public void testPunchInAndPunchOutFunctionality() throws InterruptedException {
        String dateTime;
        orangeHRM.login(USERNAME, PASSWORD);
        String punchedInOut = orangeHRM.userIsPunchInOrOut();
        if (punchedInOut.equals("Punched In")) {
            punchOut();
        }
        orangeHRM.clickStopWatch();
        Assert.assertTrue(orangeHRM.getPageTitle().contains("Attendance"));
        dateTime = getDateTime();
        orangeHRM.punchIn();
        driver.navigate().back();
        driver.navigate().refresh();
        Assert.assertEquals(orangeHRM.userIsPunchInOrOut(), "Punched In", "Attendance record not updated");
        Assert.assertTrue(orangeHRM.getPunchInOutDetails().contains(dateTime));
        punchOut();
    }

    @Test()
    public void testQuickLaunchButtons() {
        String expectedHexValue = "#64728c"; // default color (no hover)
        orangeHRM.login(USERNAME, PASSWORD);
        orangeHRM.selectQuickHelp("My Leave");
        softAssert.assertEquals(orangeHRM.getPageTitle(), "Leave");
        softAssert.assertEquals(orangeHRM.getSelectedTab(), "My Leave");
        driver.navigate().back();
        softAssert.assertEquals(orangeHRM.getPageTitle(), "Dashboard");
        orangeHRM.hoverOnQuickLaunchItem("My Leave");
        softAssert.assertNotEquals(orangeHRM.getQuickLaunchIconsColor("My Leave"), expectedHexValue);
        softAssert.assertAll("Quick launch validations failed");
    }

    private String getDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(formatter);
    }

    private void punchOut() {
        orangeHRM.clickStopWatch();
        Assert.assertTrue(orangeHRM.getPageTitle().contains("Attendance"));
        orangeHRM.punchOut();
        String dateTime = getDateTime();
        driver.navigate().back();
        driver.navigate().refresh();
        Assert.assertEquals(orangeHRM.userIsPunchInOrOut(), "Punched Out",
                "Attendance record not updated");
        Assert.assertTrue(orangeHRM.getPunchInOutDetails().contains(dateTime));
    }

    @AfterMethod()
    public void tearDown() {
        driver.quit();
    }
}