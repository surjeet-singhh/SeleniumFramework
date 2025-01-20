package utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IAnnotationTransformer;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.chaintest.plugins.ChainTestListener;

import org.apache.commons.io.FileUtils;

import base.BaseTest;

/**
 * SuiteListener class listens to TestNG events such as test failure and annotations.
 * It takes screenshots upon test failures and configures retry logic for failed tests.
 */
public class SuiteListener implements ITestListener, IAnnotationTransformer {

    private static final Logger log = LogManager.getLogger(SuiteListener.class);

    /**
     * onTestFailure method is triggered when a test fails. It takes a screenshot and stores it locally.
     *
     * @param result The test result containing information about the failed test.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        // Capture screenshot on test failure
        byte[] screenshot = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BYTES);
        ChainTestListener.embed(screenshot, "image/png");

        // Define the file path to save the screenshot
        String filename = System.getProperty("user.dir") + File.separator + "screenshot" + File.separator + result.getMethod().getMethodName();
        File file = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.FILE);

        try {
            // Save the screenshot as a PNG file in the defined path
            FileUtils.copyFile(file, new File(filename + ".png"));
            log.info("Screenshot captured for test failure: {}", result.getMethod().getMethodName());
        } catch (IOException e) {
            log.error("Failed to save screenshot for test failure: {}", result.getMethod().getMethodName(), e);
        }
    }

    /**
     * transform method allows the modification of annotations before test execution.
     * In this case, it sets the retry logic for failed tests.
     *
     * @param annotation The test annotation.
     * @param testClass The test class.
     * @param testConstructor The constructor of the test class.
     * @param testMethod The test method.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Set retry logic for the test method
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
        log.info("Retry logic applied for test: {}", testMethod.getName());
    }

    // Other listener methods can be overridden as needed:
    @Override
    public void onTestStart(ITestResult result) { 
        log.info("Test started: {}", result.getMethod().getMethodName()); 
    }

    @Override
    public void onTestSkipped(ITestResult result) { 
        log.info("Test skipped: {}", result.getMethod().getMethodName()); 
    }

    @Override
    public void onTestSuccess(ITestResult result) { 
        log.info("Test passed: {}", result.getMethod().getMethodName()); 
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) { 
        log.info("Test failed but within success percentage: {}", result.getMethod().getMethodName()); 
    }

    @Override
    public void onStart(org.testng.ITestContext context) {
        log.info("Test suite started: {}", context.getName());
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        log.info("Test suite finished: {}", context.getName());
    }
}
