package testHelper;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestInfra {
	protected WebDriver driver;
	private static final Logger log = Logger.getLogger(TestInfra.class);
	private static final int ELEMENT_WAIT_TIMEOUT = 10;
	private static final int SHORT_ELEMENT_WAIT_TIMEOUT = 10;
	
	public TestInfra(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
	public void setValue(WebElement field, String strUserName) {
		field.clear();
		field.sendKeys(strUserName);
	}

	public void setValue(By by, String value) {
		setValue(driver.findElement(by), value);
	}

	public boolean clickOnWebElement(By byPath) {
		try {
			WebElement webElement = driver.findElement(byPath);
			clickOnWebElement(webElement);
		} catch (Exception e) {
			log.error(e);
			return false;
		}
		return true;
	}

	public void textToBePresentInElement(String locator) {
		(new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT))).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return driver.findElement(By.tagName(locator)).getText().length() != 0;
			}
		});
	}

	public void textToBePresentInElementValue(By locator, String Text) {
		(new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT)))
				.until(ExpectedConditions.textToBePresentInElementValue(locator, Text));
	}

	public void textToBePresentInElement(By locator, String Text) {
		(new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT)))
				.until(ExpectedConditions.textToBePresentInElementLocated(locator, Text));
	}

	public void waitForElementPresent(By locator) {
		(new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT))).until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public boolean waitForElementStaleUp(WebElement el) {
		return (new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT))).until(ExpectedConditions.stalenessOf(el));
	}

	public WebElement waitForElementVisibility(WebElement el) {
		return (new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT))).until(ExpectedConditions.visibilityOf(el));
	}

	public WebElement waitForElementVisibilityLocatedBy(WebElement el) {
		return (new WebDriverWait(driver, Duration.ofSeconds(SHORT_ELEMENT_WAIT_TIMEOUT))).until(ExpectedConditions.visibilityOf(el));
	}

	public boolean waitForInvisibilityOfElementLocated(final By locator) {
		return (new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT)))
				.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public WebElement waitForElementToClick(final By locator) {
		return (new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT)))
				.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public WebElement waitForElementToClick(WebElement el) {
		return (new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT))).until(ExpectedConditions.elementToBeClickable(el));
	}

	public boolean clickOnWebElement(WebElement webElement) {
		try {
			waitForElementVisibility(webElement);
			webElement.click();
		} catch (StaleElementReferenceException e) {
			waitForElementStaleUp(webElement);
			log.info("waiting for element to staleup");
			WebElement webElement1 = webElement;
			webElement1.click();
		} catch (Exception e) {
			log.error(e);
			return false;
		}
		return true;
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public boolean isElementActive(By by) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		boolean exists = (driver.findElements(by).size() != 0);

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		return exists;
	}
}
