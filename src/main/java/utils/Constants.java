package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Constants interface holds constant values used throughout the application.
 * These constants ensure consistent values and reduce the risk of hardcoding errors.
 */
public interface Constants {
    // Logger instance for potential debugging (if required in implementations)
    Logger log = LogManager.getLogger(Constants.class);

    // Constant for the "Sign out" text used in validation
    String SIGN_OUT = "Sign out";

}
