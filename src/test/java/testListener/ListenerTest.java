package testListener;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class ListenerTest implements ITestListener {

	private static final Logger log = Logger.getLogger(ListenerTest.class);
	private WebDriver driver = null;

	// Below onStart and onFinish will call suite level..
	@Override
	public void onFinish(ITestContext Result) {
		log.info("Calling stop method.......");
		Executor.stop();
	}

	@Override
	public void onStart(ITestContext Result) {
		log.info("Started IAOP Regression..");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult Result) {

	}

	// When Test case get failed, this method is called.
	@Override
	public void onTestFailure(ITestResult Result) {
		log.info("The name of the testcase Failed is :" + Result.getName());
		try {
			ITestContext context = Result.getTestContext();
		    driver = (WebDriver) context.getAttribute("WebDriver");
		    takeScreenShot(Result.getName(), driver);
		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void takeScreenShot(String methodName, WebDriver driver) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// The below method will save the screen shot in d drive with test method name
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-uuuu_HH-mm-ss");
	        LocalDateTime now = LocalDateTime.now();
			File destFile = new File((String) reportDirectory+"/failure_screenshots/"+methodName+"_"+dtf.format(now)+".png");
            //FileUtils.copyFile(scrFile, new File("D:\\" + methodName + ".png"));
            FileUtils.copyFile(scrFile, destFile);
            log.info(scrFile);
            log.info(destFile);
            Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// When Test case get Skipped, this method is called.
	@Override
	public void onTestSkipped(ITestResult Result) {
		log.info("The name of the testcase Skipped is :" + Result.getName());
	}

	// When Test case get Started, this method is called.
	@Override
	public void onTestStart(ITestResult Result) {
		log.info("The name of the testcase Started is :" + Result.getName());
	}

	// When Test case get passed, this method is called.
	@Override
	public void onTestSuccess(ITestResult Result) {
		log.info("The name of the testcase Passed is :" + Result.getName());
	}

}