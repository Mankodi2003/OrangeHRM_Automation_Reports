package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import base.BaseClass;
import pages.LoginPage;
import utils.WaitUtil;
import utils.ScreenshotUtil;

import com.aventstack.extentreports.Status;

public class LoginTest extends BaseClass {

	LoginPage loginPage;
	WaitUtil wait;

	@BeforeMethod
	public void setUpBrowser() {
		setUp();
		loginPage = new LoginPage(driver);
		wait = new WaitUtil(driver);
		test = extent.createTest("Login Test - " + this.getClass().getSimpleName());
	}

	@Test(priority = 1)
	public void verifyValidLogin() {
		test.log(Status.INFO, "Starting valid login test");
		loginPage.login("Admin", "admin123");
		wait.waitForUrlContains("dashboard");
		String title = loginPage.getPageTitle();
		Assert.assertTrue(title.contains("OrangeHRM"));
		test.log(Status.PASS, "Login successful and dashboard loaded");
	}

	@Test(priority = 2)
	public void verifyInvalidLogin() {
		test.log(Status.INFO, "Starting invalid login test");
		loginPage.login("Admin", "wrongpass");
		String msg = loginPage.getErrorMessage();
		Assert.assertTrue(msg.contains("Invalid credentials"));
		test.log(Status.PASS, "Error message verified successfully");
	}

	@AfterMethod
	public void closeBrowser(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + result.getName() + ".png";
			ScreenshotUtil.captureScreenshot(driver, result.getName());
			test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
			test.addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test passed successfully");
		}
		tearDown();
	}

	@AfterSuite
	public void endReport() {
		flushReport();
	}
}