package testCases;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testHelper.ExcelReader;
import testHelper.TestSetup;
import testListener.Executor;
import testPages.LoginPage;

public class AccountOpeningProcess extends TestSetup {
	private LoginPage login;
	private ExcelReader ex;
	private Map<String, Map<String, String>> excelData;
	private static final Logger log = Logger.getLogger(AccountOpeningProcess.class);

	@BeforeClass(alwaysRun = true)
	@Parameters({ "excelPath" })
	public void init(String excelPath) throws Exception {
		login = new LoginPage(getDriver());
		Executor.setTestCase("AccountOpeningProcess");
		ex = new ExcelReader(new File(excelPath).getAbsolutePath(), 0);
		excelData = ex.getExcelAsMap();
		log.info("Excel Data for 2nd row and column - Username : " + excelData.get("2").get("Username"));
		log.info("Excel Data for 1st row and column - Password : " + excelData.get("1").get("Password"));
		log.info("Excel Data for 1st row and column - Risk Profile : " + excelData.get("1").get("Risk Profile"));
		log.info("Excel Data for 2nd row and column - Nominee : " + excelData.get("1").get("Nominee"));
		log.info("excelData as Map 1st index : " + excelData.get("1"));
		log.info("excelData as Map full : " + excelData);
		startRecording(this.getClass().getSimpleName());
	}

	@Parameters({"password", "captchaPath", "tesseractPath" })
	@Test(groups = {
			"smoke" }, description = "Verify account opening journey and show Let's Get Strated button", priority = 0)
	public void showLetsStartBtn(String password, String captchaPath, String tesseractPath)
			throws Exception {
		login.login(excelData.get("2").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			throw new Exception("show Let's Get Strated button Fail");
		} else {
			log.info("show Let's Get Strated button Pass");
		}
	}

	@Parameters({ "url" })
	@Test(groups = {
			"smoke" }, description = "Verify post click on'Let's Get Strated'button display PAN details", priority = 1)
	public void showPanDetailsPage(String url) throws Exception {
		if (!login.getPanStatus()) {
			login.logout(url);
			throw new Exception("Verify PAN details Fail");
		} else {
			log.info("Verify PAN details Pass");
			login.logout(url);
		}
	}
}
