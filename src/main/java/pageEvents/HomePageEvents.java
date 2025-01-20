package pageEvents;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import base.BaseTest;
import utils.Constants;

public class HomePageEvents extends BaseTest {
	
	 private static final Logger log = LogManager.getLogger(HomePageEvents.class); // Logger instance
    // Locator for the "Sign Out" element
    private By signOut = By.xpath("//*[@id='sidebar']/div/div/a[3]");
    

    /**
     * Method to verify the landing page by checking the "Sign Out" text.
     * Ensures that the user has landed on the expected page by asserting the "Sign Out" text.
     */
    public void landingPage() {
        log.info("Verifying the landing page by checking the 'Sign Out' text.");

        // Using BaseTest's reusable method to get text from the "Sign Out" element
        String signOutText = getText(signOut);
        

        // Log the retrieved text for debugging purposes
        log.debug("Retrieved 'Sign Out' text: {}", signOutText);

        // Assertion to ensure the text matches the expected constant
        assertEquals(signOutText, Constants.SIGN_OUT, 
            "Sign Out text does not match the expected value.");

        log.info("Landing page verified successfully. 'Sign Out' text matches the expected value.");
    }
}
