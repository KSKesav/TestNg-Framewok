package DriverFactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExcelFileUtil;

public class AppTest {
	WebDriver driver;
	String inputpath = "D:\\Automation_Selenium\\OrangeHRM_Maven\\TestInput\\LoginData.xlsx";
	String outputpath = "D:\\Automation_Selenium\\OrangeHRM_Maven\\TestOutput\\Results.xlsx";
	ExtentReports report;
	ExtentTest test;
@BeforeTest
public void setUP() 
{
	report = new ExtentReports("./Reports/DataDriver.html");
	System.setProperty("webdriver.chrome.driver", "D:\\Automation_Selenium\\OrangeHRM_Maven\\chromedriver.exe");
    driver = new ChromeDriver();
    driver.manage().window().maximize();
  
    }
@Test
public void verifyLogin()throws Throwable
{
	ExcelFileUtil xl= new ExcelFileUtil(inputpath);
	int rc = xl.rowCount("Login");
	Reporter.log("No of rows are:::"+rc,true);
	for(int i=1; i<=rc; i++)
	{
		test = report.startTest("Validate Login");
		driver.get("http://orangehrm.qedgetech.com");
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String username = xl.getCellData("Login", i, 0);
		String password = xl.getCellData("Login", i, 1);
		driver.findElement(By.name("txtUsername")).sendKeys(username);
		driver.findElement(By.name("txtPassword")).sendKeys(password);
		driver.findElement(By.name("Submit")).click();
		String expected = "dashboard";
		String actual = driver.getCurrentUrl();
		if(actual.contains(expected))
		{
			xl.setCellData("Login", i, 2, "Login success", outputpath);
			xl.setCellData("Login", i, 3, "Pass", outputpath);
			Reporter.log("Login Success",true);
			test.log(LogStatus.PASS, "Login success");
		}
		else
		{
			xl.setCellData("Login", i, 2, "Login Fail", outputpath);
			xl.setCellData("Login", i, 3, "Fail", outputpath);
			Reporter.log("Login Fail",true);
			test.log(LogStatus.FAIL, "Login Fail");
			//test.log(LogStatus.FAIL, "Login Fail");
		}
		report.endTest(test);
		report.flush();
		}
	}
@AfterTest
public void tearDown()
{
	driver.quit();
}
}






















