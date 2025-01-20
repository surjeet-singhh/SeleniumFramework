package pageEvents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import base.BaseTest;

public class LoginPageEvents extends BaseTest {
    // Logger instance for logging messages
    private static final Logger log = LogManager.getLogger(LoginPageEvents.class);

    // Locators for Login Page elements
    private By usernameTextField = By.cssSelector("input[name='userName']"); // Locator for the username input field
    private By passwordTextField = By.cssSelector("input[name='passWord']"); // Locator for the password input field
    private By signInButton = By.xpath("//*[@id='loginAdvDivId']/div[2]/div/button"); // Locator for the Sign-In button

    /**
     * Method to perform login by entering username and password and clicking the Sign-In button.
     *
     * @param username The username to enter in the username field
     * @param password The password to enter in the password field
     */
    public void signIn(String username, String password) {
        log.info("Starting the sign-in process.");
        
        // Enter username
        log.info("Entering username: {}", username);
        enterText(usernameTextField, username); // Using BaseTest's reusable method to enter text

        // Enter password
        log.info("Entering password: {}", password);
        enterText(passwordTextField, password); // Using BaseTest's reusable method to enter text

        // Click on Sign-In button
        log.info("Clicking on the Sign-In button.");
        click(signInButton); // Using BaseTest's reusable method to click

        log.info("Sign-in process completed.");
    }
}
