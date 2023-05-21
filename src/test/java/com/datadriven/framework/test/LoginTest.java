package com.datadriven.framework.test;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.datadriven.framework.base.BaseUI;
import com.datadriven.framework.utils.TestDataProvider;

public class LoginTest extends BaseUI {

	@AfterTest
	public void endReport() {
		report.flush();
	}
		
	@Test(dataProvider = "getTestDataProviderLogin")
	public void loginIntoApplication(Hashtable<String, String> dataTable) {

		logger = report.createTest("Login Into CRM Application"+ dataTable.get("Username"));
		
		invokeBrowser("chrome");
		openUrl("webURL");
		elementClick("loginBtn_Xpath");
		enterText("username_Name", dataTable.get("Username"));
		enterText("password_Name", dataTable.get("Password"));
		elementClick("login_Xpath");		
		tearDown();
	}
	
	@DataProvider 
	public Object[][] getTestDataProviderLogin(){ 
		 return TestDataProvider.getTestData("TestData.xlsx","credentials","Login Into CRM Application"); 
	 }

	@Test(dataProvider = "getTestDataProviderCreateContact")
	public void createNewContact(Hashtable<String, String> dataTable) {

		logger = report.createTest("create new contact"+ dataTable.get("Username "));
		
		invokeBrowser("chrome");
		openUrl("webURL");
		elementClick("loginBtn_Xpath");
		enterText("username_Name",dataTable.get("Username") );
		enterText("password_Name",dataTable.get("Password"));
		elementClick("login_Xpath");
		elementClick("contact_Xpath");
		elementClick("createContact_Xpath");
		enterText("firstName_Name", dataTable.get("FirstName"));
		enterText("lastName_Name", dataTable.get("LastName"));
		enterText("company_Xpath", dataTable.get("Company"));
		enterText("emailAddress_Xpath", dataTable.get("Email"));
		SelectElementInList("category_Name", dataTable.get("Category"));
		elementClick("save_Xpath");
		
	}
	
	@DataProvider 
	public Object[][] getTestDataProviderCreateContact(){ 
		 return TestDataProvider.getTestData("TestData.xlsx","credentials","Create New Contact"); 
	 }
	
	
}
