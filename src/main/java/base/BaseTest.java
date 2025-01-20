package base;

import java.time.Duration;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import utils.ConfigFileReader;

public class BaseTest {
    private static final Logger log = LogManager.getLogger(BaseTest.class); // Logger instance
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ConfigFileReader configReader;

    /**
     * Set the environment from the test parameter.
     *
     * @param env - The environment name (e.g., dev, qa, prod).
     */
    @BeforeTest
    @Parameters("env")
    public void setEnvironment(String env) {
        if (env != null && !env.isEmpty()) {
            System.setProperty("env", env);
            log.info("Environment set to: {}", env);
        } else {
            System.setProperty("env", "dev");
            log.info("Environment defaulted to: dev");
        }

        // Reload configuration for the current environment
        ConfigFileReader.reloadInstance();
        configReader = ConfigFileReader.getInstance();
        log.debug("Config file reloaded for environment: {}", System.getProperty("env"));

        // Verify the URL loaded from the config file
        String url = configReader.getProperty("url");
        log.info("URL loaded from config: {}", url);
    }

    /**
     * Initialize the WebDriver based on the browser and headless parameter.
     *
     * @param browser - Browser name (chrome, firefox).
     * @param headless - Whether to run in headless mode.
     */
    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setupDriver(String browser, boolean headless) {
        log.info("Setting up WebDriver for browser: {}, headless: {}", browser, headless);
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            if (headless) {
                chromeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
            }
            driver.set(new ChromeDriver(chromeOptions));
            log.info("ChromeDriver initialized.");
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());
            log.info("FirefoxDriver initialized.");
        }

        // Maximize window and load the URL
        getDriver().manage().window().maximize();
        String url = configReader.getProperty("url");
        if (url != null && !url.isEmpty()) {
            getDriver().get(url);
            log.info("Navigated to URL: {}", url);
        } else {
            throw new RuntimeException("URL is not defined in the properties file.");
        }

        // Set implicit wait
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        log.info("Implicit wait set to 20 seconds.");
    }

    /**
     * Tear down the WebDriver after each test method.
     */
    @AfterMethod
    public void tearDown() {
        if (driver.get() != null) {
            log.info("Tearing down WebDriver.");
            driver.get().quit();
            driver.remove();
        }
    }

    /**
     * Get the current WebDriver instance.
     *
     * @return WebDriver instance.
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    
    
 // --------------------- Utility Methods -------------------------------------------------------



    /**
     * Introduces a static wait (Thread.sleep) for the specified duration.
     *
     * @param millis - The wait time in milliseconds.
     */
    public void staticWait(long millis) {
        log.debug("Static wait for {} ms.", millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Static wait interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Waits for an element to be visible on the page.
     *
     * @param locator - The By locator for the element.
     * @param timeout - The timeout in seconds to wait.
     * @return The visible WebElement.
     */
    public WebElement waitForElementToBeVisible(By locator, int timeout) {
        log.info("Waiting for element to be visible: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable on the page.
     *
     * @param locator - The By locator for the element.
     * @param timeout - The timeout in seconds to wait.
     * @return The clickable WebElement.
     */
    public WebElement waitForElementToBeClickable(By locator, int timeout) {
        log.info("Waiting for element to be clickable: {}", locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     *
     * @param locator - The By locator for the element.
     */
    public void click(By locator) {
        log.info("Clicking on element: {}", locator);
        waitForElementToBeClickable(locator, 10).click();
    }

    /**
     * Clears any existing text in an element and enters the provided text.
     *
     * @param locator - The By locator for the element.
     * @param text    - The text to enter into the element.
     */
    public void enterText(By locator, String text) {
        log.info("Entering text '{}' into element: {}", text, locator);
        WebElement element = waitForElementToBeVisible(locator, 10);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Retrieves the visible text of an element.
     *
     * @param locator - The By locator for the element.
     * @return The text of the element.
     */
    public String getText(By locator) {
        log.info("Getting text from element: {}", locator);
        return waitForElementToBeVisible(locator, 10).getText();
    }

    /**
     * Scrolls to the specified element using JavaScript.
     *
     * @param locator - The By locator for the element.
     */
    public void scrollToElement(By locator) {
        log.info("Scrolling to element: {}", locator);
        WebElement element = getDriver().findElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Hovers over an element using Actions.
     *
     * @param locator - The By locator for the element.
     */
    public void hoverOverElement(By locator) {
        log.info("Hovering over element: {}", locator);
        Actions actions = new Actions(getDriver());
        actions.moveToElement(waitForElementToBeVisible(locator, 10)).perform();
    }

    /**
     * Hovers over one element and clicks on another element.
     *
     * @param hoverLocator - The By locator for the element to hover.
     * @param clickLocator - The By locator for the element to click.
     */
    public void hoverAndClick(By hoverLocator, By clickLocator) {
        log.info("Hovering over element: {} and clicking on element: {}", hoverLocator, clickLocator);
        WebElement hoverElement = waitForElementToBeVisible(hoverLocator, 10);
        Actions actions = new Actions(getDriver());
        actions.moveToElement(hoverElement).perform();

        WebElement clickElement = waitForElementToBeClickable(clickLocator, 10);
        clickElement.click();
    }

    /**
     * Retrieves the specified attribute value from an element.
     *
     * @param locator   - The By locator for the element.
     * @param attribute - The attribute name.
     * @return The attribute value.
     */
    public String getAttribute(By locator, String attribute) {
        log.info("Getting attribute '{}' from element: {}", attribute, locator);
        return waitForElementToBeVisible(locator, 10).getAttribute(attribute);
    }

    /**
     * Checks if an element is displayed on the page.
     *
     * @param locator - The By locator for the element.
     * @return True if the element is displayed, false otherwise.
     */
    public boolean isElementDisplayed(By locator) {
        log.info("Checking if element is displayed: {}", locator);
        try {
            return getDriver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            log.warn("Element not found: {}", locator);
            return false;
        }
    }

    /**
     * Waits for the page to load completely using JavaScript.
     */
    public void waitForPageLoad() {
        log.info("Waiting for page to load completely.");
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Accepts a browser alert dialog.
     */
    public void acceptAlert() {
        log.info("Accepting alert.");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    /**
     * Dismisses a browser alert dialog.
     */
    public void dismissAlert() {
        log.info("Dismissing alert.");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    /**
     * Retrieves the text of a browser alert dialog.
     *
     * @return The alert text.
     */
    public String getAlertText() {
        log.info("Getting text from alert.");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    /**
     * Refreshes the current page.
     */
    public void refreshPage() {
        log.info("Refreshing the page.");
        getDriver().navigate().refresh();
    }

    /**
     * Navigates back to the previous page in the browser history.
     */
    public void navigateBack() {
        log.info("Navigating back.");
        getDriver().navigate().back();
    }

    /**
     * Navigates forward to the next page in the browser history.
     */
    public void navigateForward() {
        log.info("Navigating forward.");
        getDriver().navigate().forward();
    }

    /**
     * Navigates to a specified URL.
     *
     * @param url - The URL to navigate to.
     */
    public void navigateTo(String url) {
        log.info("Navigating to URL: {}", url);
        getDriver().navigate().to(url);
    }
    
    /**
     * Switches to a specific frame by its locator.
     * It switches the context of the WebDriver to the specified frame.
     * 
     * @param locator Locator of the frame element (By).
     */
    public void switchToFrame(By locator) {
        WebElement frameElement = getDriver().findElement(locator);
        getDriver().switchTo().frame(frameElement);
    }

    /**
     * Switches to the default content (main page) after working with frames.
     * This method is useful when you are done with the frame and need to go back to the main page.
     */
    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }
    
    /**
     * Switches to the new window opened in the browser and returns the parent window's handle.
     * This way, you can switch back to the parent window later.
     *
     * @return the parent window's handle.
     */
    public String switchToNewWindow() {
        // Get the current window handle (parent window)
        String parentWindowHandle = getDriver().getWindowHandle();
        
        // Get all window handles
        Set<String> allWindowHandles = getDriver().getWindowHandles();
        
        // Iterate through all window handles and switch to the new window
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(parentWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
        
        return parentWindowHandle;  // Return the parent window handle to switch back later
    }
    
    /**
     * Switches back to the parent window using the parent window handle.
     *
     * @param parentWindowHandle The window handle of the parent window to switch back to.
     */
    public void switchToParentWindow(String parentWindowHandle) {
        getDriver().switchTo().window(parentWindowHandle);
    }

    /**
     * Perform a keyboard action using Keys.
     *
     * @param locator The WebElement to interact with.
     * @param key The Keys value to simulate (e.g., Keys.ENTER, Keys.TAB).
     */
    public void sendKeysToElement(WebElement element, Keys key) {
        element.sendKeys(key);
    }
    
    /**
     * Handle dropdown selection by visible text.
     *
     * @param locator The locator of the dropdown element.
     * @param visibleText The text to select in the dropdown.
     */
    public void selectDropdownByVisibleText(By locator, String visibleText) {
        WebElement dropdownElement = getDriver().findElement(locator);
        Select select = new Select(dropdownElement);
        select.selectByVisibleText(visibleText);
    }

    /**
     * Handle dropdown selection by value.
     *
     * @param locator The locator of the dropdown element.
     * @param value The value to select in the dropdown.
     */
    public void selectDropdownByValue(By locator, String value) {
        WebElement dropdownElement = getDriver().findElement(locator);
        Select select = new Select(dropdownElement);
        select.selectByValue(value);
    }

    /**
     * Handle dropdown selection by index.
     *
     * @param locator The locator of the dropdown element.
     * @param index The index to select in the dropdown (0-based).
     */
    public void selectDropdownByIndex(By locator, int index) {
        WebElement dropdownElement = getDriver().findElement(locator);
        Select select = new Select(dropdownElement);
        select.selectByIndex(index);
    }
    
    /**
     * Perform a right-click (context-click) on an element using Actions class.
     *
     * @param locator The locator of the element to right-click on.
     */
    public void rightClickElement(By locator) {
        WebElement element = getDriver().findElement(locator);
        Actions actions = new Actions(getDriver());
        actions.contextClick(element).perform();
    }
    
    /**
     * Perform a click-and-hold action on an element using Actions class.
     *
     * @param locator The locator of the element to click and hold.
     */
    public void clickAndHoldElement(By locator) {
        WebElement element = getDriver().findElement(locator);
        Actions actions = new Actions(getDriver());
        actions.clickAndHold(element).perform();
    }
    
    /**
     * Perform a release action after a click-and-hold using Actions class.
     *
     * @param locator The locator of the element to release.
     */
    public void releaseElement(By locator) {
        WebElement element = getDriver().findElement(locator);
        Actions actions = new Actions(getDriver());
        actions.release(element).perform();
    }
    
    /**
     * Wait for the loader to finish loading before interacting with the page.
     * @param loaderLocator The locator for the loader element (e.g., ID, class, or CSS selector).
     * @param timeout The maximum time to wait for the loader to disappear.
     */
    public void waitForLoaderToDisappear(By loaderLocator, int timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));

        // Wait until the loader is NOT visible (disappears)
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
            System.out.println("Loader has finished loading.");
        } catch (Exception e) {
            System.err.println("Loader did not disappear within the timeout.");
        }
    }
    
    /**
     * Wait for the loader to appear and then disappear, if it appears, before interacting with the page.
     * This is a more comprehensive method that accounts for both loader appearance and disappearance.
     * @param loaderLocator The locator for the loader element.
     * @param timeout The maximum time to wait for the loader to disappear.
     */
    public void waitForLoaderAndContent(By loaderLocator, int timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));

        // Wait for the loader to appear (if necessary)
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loaderLocator));
            System.out.println("Loader started loading.");
        } catch (Exception e) {
            System.err.println("Loader did not appear within the timeout.");
        }

        // Wait until the loader disappears
        waitForLoaderToDisappear(loaderLocator, timeout);
    }
    
    
 

}
