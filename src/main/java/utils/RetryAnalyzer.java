package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer class implements the retry logic for failed test cases in TestNG.
 * It allows test cases to be retried a specified number of times before marking them as failed.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);

    private int count = 0; // Current retry attempt count
    private int retryCount = 0; // Maximum allowed retry attempts

    /**
     * Retry method determines whether to retry a failed test case.
     *
     * @param result The result of the test execution.
     * @return true if the test should be retried; false otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        if (count < retryCount) {
            count++;
            log.warn("Retrying test '{}' (Attempt {}/{})", result.getName(), count, retryCount);
            return true;
        }
        log.error("Test '{}' failed after {} retries", result.getName(), retryCount);
        return false;
    }
}
