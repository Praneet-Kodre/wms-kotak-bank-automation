package testHelper;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class TestSetup {
	private WebDriver driver;
	private static ScreenRecorder screenRecorder;
	public static String DOWNLOADED_FILES_FOLDER = "/target/surefire-reports/TestVideos/";
    //private static int SESSION_TIMEOUT = 10;
	private static final Logger log = Logger.getLogger(TestSetup.class);

	public void startRecording(String className) throws Exception {
		File file = new File(System.getProperty("user.dir") + File.separator + DOWNLOADED_FILES_FOLDER);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		Rectangle captureSize = new Rectangle(0, 0, width, height);
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
						Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
				null, file, className);
		screenRecorder.start();
	}

	@Parameters({ "url", "browser" })
	@BeforeClass(alwaysRun = true)
	public void initDriver(@Optional("nbuat.kotak.com/knb2/") String url, @Optional("chrome") String browser, ITestContext context) throws Exception {
		startDriver(url, browser);
		context.setAttribute("WebDriver", getDriver());
	}
	
	public void startDriver(String url, String browserName) {
		String os = System.getProperty("os.name").toLowerCase();
		String chrDriver = null;
		log.info("OS : " + os);
		if (os.contains("win")) {
			chrDriver = "src/test/resources/chromedriver_win.exe";
		} else if (os.contains("mac")) {
			chrDriver = "src/test/resources/chromedriver_mac";
		} else if (os.contains("linux")) {
			chrDriver = "src/test/resources/chromedriver_linux64";
		}

		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", chrDriver);
			ChromeOptions chromeCaps = new ChromeOptions();
			chromeCaps.addArguments("--remote-allow-origins=*");
			//DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();
			//chromeCaps.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
			//chromeCaps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			driver = new ChromeDriver(chromeCaps);
			//driver.manage().timeouts().implicitlyWait(SESSION_TIMEOUT, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get("https:" + url);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver");
			FirefoxOptions caps = new FirefoxOptions();
			caps.setCapability("marionette", true);
			caps.setCapability("acceptInsecureCerts", true);
			// var capabilities = new FirefoxOptions().addTo(caps);
			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.get("https:" + url);
		}
		// TODO: ADD Firefox, IE AND SAFARI TO THE LIST
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void pageRefresh() {
		driver.navigate().refresh();
	}

	@AfterSuite(alwaysRun = true)
	public void closeBrowser() throws IOException {
		driver.quit();
		screenRecorder.stop();
	}
}
