package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class OrangeHRM {

    private WebDriver driver;
    private static final Duration SHORTWAIT = Duration.ofSeconds(30);

    @FindBy(name = "username")
    private WebElement username;

    @FindBy(name = "password")
    private WebElement password;

    @FindBy(xpath = "//button[text()=' Login ']")
    private WebElement loginBtn;

    @FindBy(className = "oxd-userdropdown")
    private WebElement userArea;

    @FindBy(id = "logout")
    private WebElement logoutBtn;

    @FindBy(className = "bi-stopwatch")
    private WebElement stopwatch;

    @FindBy(xpath = "//button[text()=' In ']")
    private WebElement punchIn;

    @FindBy(xpath = "//button[text()=' Out ']")
    private WebElement punchOut;

    @FindBy(className = "orangehrm-quick-launch-card")
    private List<WebElement> quickLaunch;

    @FindBy(className = "oxd-topbar-header-title")
    private WebElement pageTitle;

    @FindBy(className = "oxd-topbar-body-nav-tab")
    private List<WebElement> tabs;

    @FindBy(className = "orangehrm-attendance-card-state")
    private WebElement punchInOut;

    @FindBy(className = "orangehrm-attendance-card-details")
    private WebElement punchInOutDetails;


    public OrangeHRM(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String usrname) {
        username.sendKeys(usrname);
    }

    public void enterPassword(String passwd) {
        password.sendKeys(passwd);
    }

    public String getUsernameText() {
        return username.getText();
    }

    public String getPasswordText() {
        return password.getText();
    }

    public boolean isLoginBtnDisplayed() {
        return loginBtn.isDisplayed();
    }

    public boolean isLogoutBtnDisplayed() {
        return logoutBtn.isDisplayed();
    }

    public boolean login(String usrname, String passwd) {
        enterUsername(usrname);
        enterPassword(passwd);
        loginBtn.click();
        return isUserAreaDisplayed();
    }

    public void logout() {
        logoutBtn.click();
    }

    public boolean isUserAreaDisplayed() {
        waitUntilElementIsDisplayed(driver, userArea, SHORTWAIT);
        return userArea.isDisplayed();
    }

    private List<WebElement> getMenuItems() {
        waitUntilElementIsDisplayed(driver, userArea, SHORTWAIT);
        userArea.click();
        return driver.findElements(By.className("oxd-userdropdown-link"));
    }

    public List<String> getItems() {
        return getMenuItems().stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public void selectMenu(String menu) {
        getMenuItems().stream()
                .filter(item -> item.getText().equals(menu)).findFirst().get().click();
    }

    public static void waitUntilElementIsDisplayed(WebDriver driver, WebElement element, Duration seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public List<String> getQuickLaunchOptions() {
        return quickLaunch.stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public void selectQuickHelp(String option) {
        quickLaunch.stream()
                .filter(item -> item.getText().equals(option)).findFirst().get().click();
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    public String getSelectedTab() {
        return tabs.stream()
                .filter(tab -> tab.getAttribute("class").contains("visited")).findFirst().get().getText();
    }

    public void hoverOnQuickLaunchItem(String option) {
        Actions actions = new Actions(driver);
        actions.moveToElement(quickLaunch.stream()
                .filter(item -> item.getText().equals(option)).findFirst().get()).pause(5).build().perform();
    }

    public String getQuickLaunchIconsColor(String option) {
        return Color.fromString(quickLaunch.stream()
                .filter(item -> item.getText().equals(option)).findFirst().get()
                .findElement(By.tagName("button")).getCssValue("color")).asHex();
    }

    public void clickStopWatch() {
        stopwatch.click();
    }

    public String userIsPunchInOrOut() {
        return punchInOut.getText();
    }

    public void punchIn() {
        punchIn.click();
        waitUntilElementIsDisplayed(driver, punchOut, SHORTWAIT);
    }

    public void punchOut() {
        punchOut.click();
        waitUntilElementIsDisplayed(driver, punchIn, SHORTWAIT);
    }

    public String getPunchInOutDetails() {
        return punchInOutDetails.getText();
    }
}
