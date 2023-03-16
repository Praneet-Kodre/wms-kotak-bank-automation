package testListener;

import org.apache.log4j.Logger;

public class Executor {

	private static final Logger log = Logger.getLogger(Executor.class);
	public volatile static String testCase = null;

	public static void startAll(String testCase) {
		setTestCase(testCase);
	}

	public static String getTestCase() {
		return testCase;
	}

	public static void setTestCase(String testCase) {
		Executor.testCase = testCase;
	}

	public volatile static boolean exit = false;

	public static void stop() {
		log.info("Stopping IAOP Regression..");
		exit = true;
	}
}
