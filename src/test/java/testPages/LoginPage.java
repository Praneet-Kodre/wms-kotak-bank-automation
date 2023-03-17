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
			waitForElementToClick(By.id(pass_word));
			setValue(By.id(pass_word), password);
			clickOnWebElement(By.xpath(login_button));
			waitForElementToClick(By.cssSelector(user_drop_down));
			Thread.sleep(5000);
			if (isElementActive(By.cssSelector(user_drop_down))) {
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
				waitForElementToClick(By.id(user_name));
				setValue(By.id(user_name), userName);
				Thread.sleep(2000);
				setValue(By.xpath(captcha_element), getCaptchaText(captchaPath, tesseractPath));
				Thread.sleep(2000);
				if(isElementActive(By.xpath(next_button))) {
					clickOnWebElement(By.xpath(next_button));
				}
				Thread.sleep(2000);
				if(isElementActive(By.id(pass_word))) {
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

	public boolean logout() {
		boolean status = false;
		driver.switchTo().parentFrame();
		try {
			clickOnWebElement(By.cssSelector(user_drop_down));
			waitForElementToClick(By.cssSelector(logout));
			clickOnWebElement(By.cssSelector(logout));
			Thread.sleep(5000);
			String text = driver.findElement(By.cssSelector("h5")).getText();
			if (text.contains("logged out")) {
				log.info(text);
				log.info("Logged Out");
				status = true;
			}
		} catch (Exception e) {
			log.error(e);
			status = false;
		}
		return status;
	}

	public boolean getAccountOverview() throws InterruptedException {
		boolean status = false;
		clickOnWebElement(By.xpath(investment_menu));
		clickOnWebElement(By.xpath(mutual_fund_menu));
		Thread.sleep(5000);
		driver.switchTo().frame("knb2ContainerFrame");
		waitForElementToClick(By.cssSelector(lets_start_button));
		if (isElementActive(By.cssSelector(lets_start_button))) {
			status = true;
		}
		return status;
	}
}
