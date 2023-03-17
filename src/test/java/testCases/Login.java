package testCases;

import java.io.File;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import testListener.Executor;
import testHelper.ExcelReader;
import testHelper.TestSetup;
import testPages.LoginPage;

public class Login extends TestSetup {
	private LoginPage login;
	private ExcelReader ex;
	private Map<String, Map<String, String>> excelData;
	private static final Logger log = Logger.getLogger(Login.class);

	@BeforeClass(alwaysRun = true)
	@Parameters({ "excelPath" })
	public void init(String excelPath) throws Exception {
		login = new LoginPage(getDriver());
		Executor.setTestCase("Login");
		ex = new ExcelReader(new File(excelPath).getAbsolutePath(), 0);
		excelData =ex.getExcelAsMap();
        log.info("Excel Data for 2nd row and column - Username : "+excelData.get("2").get("Username"));
        log.info("Excel Data for 1st row and column - Password : "+excelData.get("1").get("Password"));
        log.info("Excel Data for 1st row and column - Risk Profile : "+excelData.get("1").get("Risk Profile"));
        log.info("Excel Data for 2nd row and column - Nominee : "+excelData.get("1").get("Nominee"));
        log.info("excelData as Map 1st index : "+excelData.get("1"));
        log.info("excelData as Map full : "+excelData);
	}

	@Parameters({ "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Login", priority = 0)
	public void loginActiveCRN(String password, String captchaPath, String tesseractPath) throws Exception {
		if (!login.login(excelData.get("1").get("CRN"), password, captchaPath, tesseractPath)) {
			throw new Exception("Login FAIL");
		} else {
			log.info("Login PASS");
		}
		if (!login.getAccountOverview()) {
			throw new Exception("getAccountOverview FAIL");
		} else {
			log.info("getAccountOverview PASS");

		}
		if (!login.logout()) {
			throw new Exception("Logout FAIL");
		} else {
			log.info("Logout PASS");
		}
	}
}
