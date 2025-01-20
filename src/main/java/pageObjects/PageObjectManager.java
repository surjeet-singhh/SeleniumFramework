package pageObjects;

import pageEvents.HomePageEvents;
import pageEvents.LoginPageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageObjectManager {
    private static PageObjectManager pageObjectManager;
    private LoginPageEvents loginPage;
    private HomePageEvents homePage;

    // Logger instance for logging messages
    private static final Logger log = LoggerFactory.getLogger(PageObjectManager.class);

    /**
     * Constructor to initialize all page event objects.
     * Ensures that all page event objects are created when the manager is instantiated.
     */
    public PageObjectManager() {
        log.info("Initializing PageObjectManager.");
        
        // Initializing LoginPageEvents
        loginPage = new LoginPageEvents();
        log.debug("LoginPageEvents instance created.");

        // Initializing HomePageEvents
        homePage = new HomePageEvents();
        log.debug("HomePageEvents instance created.");
    }

    /**
     * Singleton implementation for getting a single instance of PageObjectManager.
     * Ensures only one instance of PageObjectManager exists across the application.
     *
     * @return Single instance of PageObjectManager
     */
    public static PageObjectManager getInstance() {
        if (pageObjectManager == null) {
            log.info("Creating a new instance of PageObjectManager.");
            pageObjectManager = new PageObjectManager();
        }
        return pageObjectManager;
    }

    /**
     * Getter for LoginPageEvents object.
     *
     * @return LoginPageEvents object
     */
    public LoginPageEvents getLoginPage() {
        log.debug("Returning LoginPageEvents instance.");
        return loginPage;
    }

    /**
     * Getter for HomePageEvents object.
     *
     * @return HomePageEvents object
     */
    public HomePageEvents getHomePage() {
        log.debug("Returning HomePageEvents instance.");
        return homePage;
    }
}
