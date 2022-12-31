package org.white_sdev.american_airlines_test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.time.LocalDate;

public class LocalUtils {
	final WebDriver driver;
	public LocalUtils(final WebDriver driver) {
		this.driver = driver;
	}
	
	@SneakyThrows
	public void takeScreenshot(String screenShotFileName){
		FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(screenShotFileName));
	}
	
	public String getScreenshotFileName(final String testID) {
		return "./target/test-reports/screenshots/" + testID + LocalDate.now().toString().replace(":| ", "_") + ".png";
	}
	
	public String getScreenshotErrorFileName(final String testCaseId) {
		return "./target/test-reports/errors/"+testCaseId+"/" + LocalDate.now().toString().replace(":| ", "_") + "-ERROR.png";
	}
	
//	@SneakyThrows
//	public void resetLogFile() {
//		File file = new File(getLogsFilePath());
//		FileUtils.delete(file);
//		FileUtils.touch(file);
//	}
	
	@SneakyThrows
	public void backupLogs(String testID) {
		FileUtils.copyFile(new File(getLogsFilePath()), new File( String.format("%s/test-reports/errors/%s/%s.log", projectBuildDirectory, testID, testID) ));
	}
	
	@SneakyThrows
	public void backupLogs(String logsFile, String testID) {
		FileUtils.copyFile(new File(logsFile), new File( String.format("%s/test-reports/errors/%s/%s.log", projectBuildDirectory, testID, testID) ));
	}
	
	String projectBuildDirectory = "./target";
	public String getLogsFilePath(){
		String artifactoryId = "american-airlines-test";
		String logsFile = String.format("%s/test-reports/logs/%s.log", projectBuildDirectory, artifactoryId);
		return logsFile;
	}
	
}