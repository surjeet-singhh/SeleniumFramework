package qa.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import base.BaseTest;
import pageEvents.HomePageEvents;
import pageEvents.LoginPageEvents;
import pageObjects.PageObjectManager;

/**
 * Test class for the Login page functionality.
 * This class tests the login functionality by entering valid credentials and verifying the landing page.
 */
public class LoginPage extends BaseTest {
    
    // Logger for logging test execution details
    public static Logger log = LogManager.getLogger(LoginPage.class);

    // Page Object Manager for accessing Page Events
    private PageObjectManager pageObjectManager = PageObjectManager.getInstance();

    // Page Events for Login and Home pages
    private LoginPageEvents loginPage = pageObjectManager.getLoginPage();
    private HomePageEvents homePage = pageObjectManager.getHomePage();

    /**
     * Test method for logging into the application.
     * It enters the username and password, clicks the SignIn button, 
     * and verifies the landing page after login.
     */
    @Test
    public void Login() {
        log.info("Starting Login test - Entering username and password");

        // Fetch the username and password from the configuration file
        String username = configReader.getProperty("username");
        String password = configReader.getProperty("password");

        // Validate if username and password are present in the config file
        if (username == null || password == null) {
            log.error("Username or password is missing in the configuration file.");
            throw new RuntimeException("Username or password is missing in the configuration file.");
        }

        // Log the username and password for debug purposes (considering security)
        log.debug("Attempting to login with username: {}", username);

        // Perform login action using the provided credentials
        loginPage.signIn(username, password);

        // Log the status of the action after clicking SignIn
        log.debug("User has successfully logged in and landed on the dashboard");

        // Verify the landing page is correct after login
        homePage.landingPage();
    }
}
