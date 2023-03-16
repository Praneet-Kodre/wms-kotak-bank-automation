package testCases;

import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import testListener.Executor;
import testHelper.ExcelReader;
import testHelper.TestSetup;
import testPages.LoginPage;

public class Login extends TestSetup {
	private LoginPage login;
	private final String path = "D:\\captcha.png";
	private static final Logger log = Logger.getLogger(Login.class);

	@BeforeClass(alwaysRun = true)
	public void init(ITestContext context) throws Exception {
		context.setAttribute("WebDriver", getDriver());
		login = new LoginPage(getDriver());
		Executor.setTestCase("Login");
		ExcelReader ex = new ExcelReader("C:/Users/prane/eclipse-workspace/KotakIaop/kotakiaop/src/test/resources/TestData.xlsx", 0);
        Map<String, Map<String, String>> excelData =ex.getExcelAsMap();
        log.info("Excel Data for 2nd row and column - Username : "+excelData.get("2").get("Username"));
        log.info("Excel Data for 1st row and column - Password : "+excelData.get("1").get("Password"));
        log.info("Excel Data for 1st row and column - Risk Profile : "+excelData.get("1").get("Risk Profile"));
        log.info("Excel Data for 2nd row and column - Nominee : "+excelData.get("1").get("Nominee"));
        log.info("excelData as Map 1st index : "+excelData.get("1"));
        log.info("excelData as Map full : "+excelData);
	}


	@Parameters({ "userName", "password" })
	@Test(groups = { "smoke" }, description = "Login", priority = 0)
	public void login(String userName, String password) throws Exception {
        if (!login.login(userName, password, path)) {
			throw new Exception("FAIL");
		} else {
			log.info("PASS");
		}
	}

	@Test(groups = { "smoke" }, description = "Logout", priority = 1)
	public void logout() throws Exception {
		if (!login.logout()) {
			throw new Exception("FAIL");
		} else {
			log.info("PASS");
		}
	}
}
