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

public class CRNAccountOverview extends TestSetup {
	private LoginPage login;
	private ExcelReader ex;
	private Map<String, Map<String, String>> excelData;
	private static final Logger log = Logger.getLogger(CRNAccountOverview.class);

	@BeforeClass(alwaysRun = true)
	@Parameters({ "excelPath" })
	public void init(String excelPath) throws Exception {
		login = new LoginPage(getDriver());
		Executor.setTestCase("Login");
		ex = new ExcelReader(new File(excelPath).getAbsolutePath(), 0);
		excelData = ex.getExcelAsMap();
		log.info("Excel Data for 2nd row and column - Username : " + excelData.get("2").get("Username"));
		log.info("Excel Data for 1st row and column - Password : " + excelData.get("1").get("Password"));
		log.info("Excel Data for 1st row and column - Risk Profile : " + excelData.get("1").get("Risk Profile"));
		log.info("Excel Data for 2nd row and column - Nominee : " + excelData.get("1").get("Nominee"));
		log.info("excelData as Map 1st index : " + excelData.get("1"));
		log.info("excelData as Map full : " + excelData);
	}

	@Parameters({ "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Active CRN Status", priority = 0)
	public void loginActiveCRN(String password, String captchaPath, String tesseractPath) throws Exception {
		login.login(excelData.get("3").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			throw new Exception("Verify Active CRN account overview status Fail");
		} else {
			log.info("Verify Active CRN account overview status Pass");

		}
	}

	@Parameters({ "url" })
	@Test(groups = { "smoke" }, description = "Verify  transaction menu enable", priority = 1)
	public void enableTransactTab(String url) throws Exception {
		if (!login.getTransactOverview()) {
			login.logout(url);
			throw new Exception("Verify  transaction menu enable Fail");
		} else {
			log.info("Verify  transaction menu enable Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify login with Minor CRN status", priority = 2)
	public void loginMinorCRN(String url, String password, String captchaPath, String tesseractPath) throws Exception {
		login.login(excelData.get("4").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify login with Minor CRN status Fail");
		} else {
			log.info("Verify login with Minor CRN status status Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify login with Auth CRN status", priority = 3)
	public void loginAuthCRN(String url, String password, String captchaPath, String tesseractPath) throws Exception {
		login.login(excelData.get("5").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify login with Auth CRN status Fail");
		} else {
			log.info("Verify login with Auth CRN status Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Login with crn which has no bank account", priority = 4)
	public void loginNoBankAccount(String url, String password, String captchaPath, String tesseractPath)
			throws Exception {
		login.login(excelData.get("6").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify Login no bank account Fail");
		} else {
			log.info("Verify Login no bank account Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify login with crn which has RA segment", priority = 5)
	public void loginRAsegmentCRN(String url, String password, String captchaPath, String tesseractPath)
			throws Exception {
		login.login(excelData.get("7").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify login RA segment status Fail");
		} else {
			log.info("Verify login RA segment status Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Login with crn which has CC segment", priority = 6)
	public void loginCCsegmentCRN(String url, String password, String captchaPath, String tesseractPath)
			throws Exception {
		login.login(excelData.get("8").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify login CC segment status Fail");
		} else {
			log.info("Verify login CC segment status Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Login without Investment account", priority = 7)
	public void loginWithoutInvestmentAccount(String url, String password, String captchaPath, String tesseractPath)
			throws Exception {
		login.login(excelData.get("9").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountClosedStatus()) {
			login.logout(url);
			throw new Exception("Verify Login without Investment account Fail");
		} else {
			log.info("Verify Login without Investment account Pass");
			login.logout(url);

		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Login with Investment account", priority = 8)
	public void loginWithInvestmentAccount(String url, String password, String captchaPath, String tesseractPath) throws Exception {
		login.login(excelData.get("10").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountOverview()) {
			login.logout(url);
			throw new Exception("Verify Login with Investment account Fail");
		} else {
			log.info("Verify Login with Investment account Pass");
			login.logout(url);
		}
	}

	@Parameters({ "url", "password", "captchaPath", "tesseractPath" })
	@Test(groups = { "smoke" }, description = "Verify Investment account in closed status", priority = 9)
	public void loginClosedCRN(String url, String password, String captchaPath, String tesseractPath) throws Exception {
		login.login(excelData.get("1").get("CRN"), password, captchaPath, tesseractPath);
		if (!login.getAccountClosedStatus()) {
			login.logout(url);
			throw new Exception("Verify Investment account in closed status Fail");
		} else {
			log.info("Verify Investment account in closed status Pass");
			login.logout(url);
		}
	}
}
