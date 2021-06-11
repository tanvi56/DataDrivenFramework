package com.datadriven.framework.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.datadriven.framework.utils.DateUtils;
import com.datadriven.framework.utils.ExtentReportManager;

import net.bytebuddy.build.ToStringPlugin.Enhance.Prefix;

public class BaseUI {

	public WebDriver driver;
	public Properties prop;
	public ExtentTest logger;
	public ExtentReports report = ExtentReportManager.getReportInstance();
	SoftAssert softAssert = new SoftAssert();

	// To invoke the browser
	public void invokeBrowser(String browserName) {
		try {
			if (browserName.equalsIgnoreCase("Chrome")) {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browserName.equalsIgnoreCase("Mozila")) {
				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "\\src\\main\\resources\\driversgeckodriver");
				driver = new FirefoxDriver();
			} else if (browserName.equalsIgnoreCase("Opera")) {
				System.setProperty("webdriver.opera.driver",
						System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\operadriver");
				driver = new OperaDriver();
			} else if (browserName.equalsIgnoreCase("IE")) {
				System.setProperty("webdriver.ie.driver",
						System.getProperty("user.dir") + "\\src\\\\main\\resources\\drivers\\IEDriverServer.exe");
				driver = new OperaDriver();
			} else {
				driver = new SafariDriver();
			}
		} catch (Exception e) {
			reportFail(e.getMessage());
		}

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// load properties files
		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream file = new FileInputStream(System.getProperty("user.dir")
						+ "\\src\\main\\resources\\objectRepo\\projectConfig.properties");
				prop.load(file);
			} catch (IOException e) {
				reportFail(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	// Open Url
	public void openUrl(String webURL) {
		try {
			driver.get(prop.getProperty(webURL));
			reportPass(webURL + "Identified Successfully");
		} catch (Exception e) {
			reportFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void tearDown() {
		driver.close();
	}

	public void quitBrowser() {
		driver.quit();
	}

	/****************** Enter Text ***********************/
	public void enterText(String locatorValue, String data) {
		try {
			getElement(locatorValue).sendKeys(data);
			reportPass(data + "entered successfully in" + locatorValue);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Enter Value ***********************/
	public void elementClick(String locatorValue) {
		try {
			getElement(locatorValue).click();
			reportPass(locatorValue + "clicked successfully");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}

	}

	/****************** Verify Element ***********************/

	public boolean isElementPresent(String locatorKey) {
		try {
			if (getElement(locatorKey).isDisplayed())
				getElement(locatorKey).isDisplayed();
			reportPass(locatorKey + "is present");
			return true;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return false;
	}

	public boolean isElementSelected(String locatorKey) {
		try {
			if (getElement(locatorKey).isSelected()) {
				reportPass(locatorKey + " : Element is Selected");
				return true;
			}
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return false;
	}

	public boolean isElementEnabled(String locatorKey) {
		try {
			if (getElement(locatorKey).isEnabled()) {
				reportPass(locatorKey + " : Element is Enabled");
				return true;
			}
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return false;
	}

	public void verifyPageTitle(String pageTitle) {
		try {
			String actualTite = driver.getTitle();
			logger.log(Status.INFO, "Actual Title is : " + actualTite);
			logger.log(Status.INFO, "Expected Title is : " + pageTitle);
			Assert.assertEquals(actualTite, pageTitle);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Identify Element ***********************/
	public WebElement getElement(String locatorKey) {
		WebElement element = null;

		try {
			if (locatorKey.endsWith("_Id")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.id(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_Xpath")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_ClassName")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.className(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_CSS")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_LinkText")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.linkText(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_PartialLinkText")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.partialLinkText(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_Name")) {
				logger.log(Status.INFO, "Locator Identified:" + locatorKey);
				element = driver.findElement(By.name(prop.getProperty(locatorKey)));
			} else {
				reportFail("invalid locator:" + locatorKey);
				Assert.fail("invalid locator:" + locatorKey);
			}
		} catch (Exception e) {
			// fail the testcase and report the error
			reportFail(e.getMessage());
			e.printStackTrace();

			Assert.fail("Failing the testcase:" + e.getMessage());
		}

		return element;
	}

	/****************** Reporting Functions ***********************/

	public void reportFail(String reportString) {

		logger.log(Status.FAIL, reportString);
		takeScreenShotOnFailure();
		Assert.fail(reportString);
	}

	public void reportPass(String reportString) {

		logger.log(Status.PASS, reportString);
	}

	@AfterMethod
	public void afterTest() {
		softAssert.assertAll();
		driver.quit();
	}

	/****************** ScreenShot Functions ***********************/
	public void takeScreenShotOnFailure() {

		TakesScreenshot takeScreenShot = (TakesScreenshot) driver;
		File source = takeScreenShot.getScreenshotAs(OutputType.FILE);

		File destination = new File(
				System.getProperty("user.dir") + "\\ScreenShot" + DateUtils.getTimeStamp() + ".png");
		try {
			FileUtils.copyFile(source, destination);
			logger.addScreenCaptureFromPath(
					System.getProperty("user.dir") + "\\ScreenShot" + DateUtils.getTimeStamp() + ".png");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/****************** Select List Drop Down ******************/
	public void SelectElementInList(String locatorXpath, String Value) {

		try {

			List<WebElement> listElement = driver.findElements(By.xpath(locatorXpath));
			for (WebElement list : listElement) {
				String prefix = list.getText();
				if (prefix.contains(Value)) {
					list.click();
				}
			}
			logger.log(Status.INFO, "Selected the Defined Value : " + Value);

		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Assertion Functions ***********************/
	public void assertTrue(boolean flag) {
		softAssert.assertTrue(flag);
	}

	public void assertfalse(boolean flag) {
		softAssert.assertFalse(flag);
	}

	public void assertequals(String actual, String expected) {
		try {
			logger.log(Status.INFO, "Assertion : Actual is -" + actual + " And Expacted is - " + expected);
			softAssert.assertEquals(actual, expected);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}

	}

}
