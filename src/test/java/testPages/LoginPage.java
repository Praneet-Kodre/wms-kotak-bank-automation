package testPages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import testHelper.TestInfra;
import static testHelper.LoginConstants.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.DatatypeConverter;

public class LoginPage extends TestInfra {
	private static final Logger log = Logger.getLogger(LoginPage.class);
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public boolean login(String userName, String password, String captchaPath, String tesseractPath) {
		boolean status = false;
		try {
			attemptCaptchaCode(userName, captchaPath, tesseractPath);
			waitForElementToClick(By.id(PASS_WORD));
			setValue(By.id(PASS_WORD), password);
			clickOnWebElement(By.xpath(LIGIN_BUTTON));
			waitForElementToClick(By.cssSelector(USER_DROP_DOWN));
			Thread.sleep(5000);
			if (isElementActive(By.cssSelector(USER_DROP_DOWN))) {
				status = true;
			}
		} catch (Exception e) {
			log.error(e);
			status = false;
		}
		return status;
	}

	public boolean attemptCaptchaCode(String userName, String captchaPath, String tesseractPath) throws TesseractException, IOException {
		boolean result = false;
		int attempts = 0;
		while (attempts < 50) {
			try {
				waitForElementToClick(By.id(USER_NAME));
				setValue(By.id(USER_NAME), userName);
				Thread.sleep(2000);
				setValue(By.xpath(CAPTCHA_ELEMENT), getCaptchaText(captchaPath, tesseractPath));
				Thread.sleep(2000);
				if(isElementActive(By.xpath(NEXT_BUTTON))) {
					clickOnWebElement(By.xpath(NEXT_BUTTON));
				}
				Thread.sleep(2000);
				if(isElementActive(By.id(PASS_WORD))) {
					result = true;
					break;
				} else {
					refreshPage();
					attempts++;
					log.info("attempts : " + attempts);
					continue;
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return result;
	}

	public String getCaptchaText(String captchaPath, String tesseractPath) throws TesseractException, IOException {
		String uri = driver.findElement(By.xpath("//img[contains(@src,'data')]")).getAttribute("currentSrc");
		String[] strings = uri.split(",");
		byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
		File file = new File(new File(captchaPath).getAbsolutePath());
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tesseract tesseract = new Tesseract();
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(1);
		tesseract.setOcrEngineMode(1);
		tesseract.setTessVariable("user_defined_dpi", "300");
		tesseract.setTessVariable("tessedit_char_whitelist",
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		tesseract.setDatapath(new File(tesseractPath).getAbsolutePath());
		String captchaText = tesseract.doOCR(new File(new File(captchaPath).getAbsolutePath())).trim().replaceAll(" ", "");
		log.info("CAPTCHA TEXT : " + captchaText);
		file.delete();
		return captchaText;
	}

	public boolean logout(String url) {
		boolean status = false;
		driver.switchTo().parentFrame();
		try {
			clickOnWebElement(By.cssSelector(USER_DROP_DOWN));
			waitForElementToClick(By.cssSelector(LOGOUT));
			clickOnWebElement(By.cssSelector(LOGOUT));
			Thread.sleep(5000);
			String text = driver.findElement(By.cssSelector("h5")).getText();
			if (text.contains("logged out")) {
				log.info(text);
				log.info("Logged Out");
				status = true;
				driver.get("https:" + url);
			}
		} catch (Exception e) {
			log.error(e);
			status = false;
		}
		return status;
	}


	public boolean getAccountOverview() throws InterruptedException {
		boolean status = false;
		clickOnWebElement(By.xpath(INVESTMENT_MENU));
		clickOnWebElement(By.xpath(MUTUAL_FUND_MENU));
		Thread.sleep(5000);
		driver.switchTo().frame("knb2ContainerFrame");
		if (isElementActive(By.xpath(MY_INVEST_PAGE))) {
			String totalAmount = driver.findElement(By.xpath(TOTAL_INVESTED_AMOUNT)).getText();
			log.info("Total Invest Amount:" + totalAmount);
			status = true;
		} else if (isElementActive(By.xpath(MUTUAL_FUND_INVES_PAGE))) {
			String leadgenerationpage = driver.findElement(By.xpath(MUTUAL_FUND_INVES_PAGE)).getText();
			log.info("Display lead generation Page:" + leadgenerationpage);
			status = true;
		}
		return status;
	}
	public boolean getTransactOverview() throws InterruptedException {
		boolean status = false;
		waitForElementToClick(By.xpath(TRANSCAT_MENU));
		if (isElementActive(By.xpath(TRANSCAT_MENU))) {
			clickOnWebElement(By.xpath(TRANSCAT_MENU));
			clickOnWebElement(By.xpath(TRANSCAT_PURCHASE));
			waitForElementPresent(By.xpath(ONETIME_INVEST));
			String displayText = driver.findElement(By.xpath(ONETIME_INVEST)).getText();
			log.info("Display the one time Investment:" + displayText);
			status = true;
		}
		return status;
	}
}
