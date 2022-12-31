package org.white_sdev.american_airlines_test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.white_sdev.american_airlines_test.pages.FlightStatus;
import org.white_sdev.american_airlines_test.pages.HomePage;

import java.time.Duration;
import java.util.Objects;

@Slf4j
public class AmericanAirlinesTests {
	
	WebDriver driver;
	LocalUtils localUtils;
	String logBreak="\n\n##################################################################\n";
	
	@BeforeEach
	public void launchApp(){
		String logID = "#launchApp():";
		log.trace("{} Start", logID);
		String headless = System.getProperty("headless");
		driver = (Objects.nonNull(headless) && !headless.isEmpty())?
			 new HtmlUnitDriver() : new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
		
		localUtils = new LocalUtils(driver);
		driver.get("https://www.aa.com/homePage.do?locale=es_MX");
		
		log.trace("{} Finish.", logID);
	}
	
	@AfterEach
	public void closeApp(){
		String logID = "#closeApp():";
		log.trace("{} Closing Browser", logID);
		driver.quit();
//		fileAppender.stop();
		getRootLogger().detachAppender(fileAppender);
	}
	
	private Logger getRootLogger(){
		return (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	}
	
	Appender<ILoggingEvent> fileAppender;
	
	public void restartAppender(String testID){
		MDC.put("logFileName", "head1");
		
		
//		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//		rootLogger.getAppender(AmericanAirlinesTests.log.getName());
		
		Logger rootLogger = getRootLogger();
		
		fileAppender = rootLogger.getAppender("FILE");
		if(fileAppender==null) throw new NullPointerException("Appender not found");
		
//		LoggerContext loggerContext = rootLogger.getLoggerContext();
//		loggerContext.putProperty("logFileName", testID);
		
		fileAppender.stop();
//		fileAppender.setName("FILE");
		
//		File logFile = new File();
		((FileAppender)fileAppender).setFile(String.format("./target/test-reports/errors/%s.log", testID));
//		fileAppender.setContext(loggerContext);
//		((FileAppender)fileAppender).setPrudent(true);
		
		fileAppender.start();
//		rootLogger.addAppender(fileAppender);
//
	}
	
	@SneakyThrows
	@Test
	public void wrongFlightTest(){
		String logID="#wrongFlightTest()- ";
		String testID="WRONG-FLIGHT-TEST";
		log.info("{}{}Start {}", logBreak, logID, logBreak);
//		restartAppender(logID);
		try{
			HomePage homePage = PageFactory.initElements(driver, HomePage.class);;
			FlightStatus flightStatus;
			
			homePage.navigateToFlightStatus();
			
			flightStatus = PageFactory.initElements(driver, FlightStatus.class);
			flightStatus.enterAndSearchFlightNumber("98675");
			
//			if(((Integer)1).equals(1)) throw new RuntimeException("Forced exception"); //TODO Delete me
			
			String errorMsg = driver.findElement(By.id("flightNumber.errors")).getText();
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(errorMsg)
					.as("Correct error message is displayed")
					.contains("Vuelo no encontrado. Intente con otro número o haga la búsqueda por ciudades.");
			softly.assertAll();
			localUtils.takeScreenshot(localUtils.getScreenshotFileName(testID));
		}catch(Exception ex){
			String screenShotFileName=localUtils.getScreenshotErrorFileName(testID);
			log.error("{}Error \nEvidences:{}", testID, screenShotFileName, ex);
			localUtils.takeScreenshot(screenShotFileName);
//			log.info("{}Backingup logs: {}", logID, String.format("./target/test-reports/errors/%s.log", testID));
//			localUtils.backupLogs(String.format("./target/test-reports/errors/%s.log", testID), testID);
			throw new RuntimeException( String.format( "An Exception has occurred when executing %s", testID), ex);
		}
		log.info("{}Finish", testID);
	}
	
	@SneakyThrows
	@Test
	public void noFlightNumberProvided(){
		String testID = "#noFlightNumberProvided()-";
		log.info("{}{} Start{}", logBreak, testID, logBreak);
//		restartAppender(testID);
		try {
			
			HomePage homePage;
			FlightStatus flightStatus;
			
			homePage = PageFactory.initElements(driver, HomePage.class);
			homePage.navigateToFlightStatus();
			
			flightStatus = PageFactory.initElements(driver, FlightStatus.class);
			flightStatus.enterAndSearchFlightNumber("");
			
			String errorMsg = driver.findElement(By.id("flightNumber.errors")).getText();
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(errorMsg)
					.as("Correct error message is displayed")
					.contains("Debe ingresar el número del vuelo.");
			softly.assertAll();
			localUtils.takeScreenshot(localUtils.getScreenshotFileName(testID));
		}catch(Exception ex){
			
			String screenShotFileName=localUtils.getScreenshotErrorFileName(testID+"ERROR-");
			log.error("{}Error \nEvidences:{}", testID, screenShotFileName, ex);
			localUtils.takeScreenshot(screenShotFileName);
			log.info("{}Backingup logs: {}", testID, String.format("./target/test-reports/errors/%s.log", testID));
			localUtils.backupLogs(String.format("./target/test-reports/errors/%s.log", testID));
			throw new RuntimeException( String.format( "An Exception has occurred when executing %s", testID), ex);
		}
		log.info("{}Finish", testID);
	}
	
}

