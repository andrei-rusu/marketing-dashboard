package application.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * JUnit4 test for Parser class
 * Assumes user has the log files for two weeks campaigns stored in their working (project) directory
 * Tests are based on the log files for two weeks campaigns
 */

public class TestParser {

	Parser parser;

	@Before
	  public void init() throws Exception {
	    parser = new Parser();
	  }

	  @After
	  public void destroy() throws Exception {
	    parser = null;
	  }


	@Test
	public void testParseEmptyFile() {
	    File file = new File("empty.csv");

	      if (!file.exists()){
	        try {
				file.createNewFile();
		        System.out.println("Empty csv file created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
	      }

		boolean isFileGood = parser.parseLogFile("impresF", file);
		Assert.assertFalse(isFileGood);
	}

	@Test
	public void testParseImpressionLogFile() {
	    File file = new File("impression_log.csv");

	      if (!file.exists()){
		        Assert.fail("Impression log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("impresF", file);
		Assert.assertTrue(isFileGood);
	}

	@Test
	public void testParseClickLogFile() {
	    File file = new File("click_log.csv");

	      if (!file.exists()){
		        Assert.fail("Click log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("clickF", file);
		Assert.assertTrue(isFileGood);
	}

	@Test
	public void testParseServerLogFile() {
	    File file = new File("server_log.csv");

	      if (!file.exists()){
		        Assert.fail("Sever log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("serverF", file);
		Assert.assertTrue(isFileGood);
	}

	@Test
	public void loadWrongServerLogFile() {
	    File file = new File("click_log.csv");

	      if (!file.exists()){
		        Assert.fail("Click log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("serverF", file);
		Assert.assertFalse(isFileGood);
	}


	@Test
	public void loadWrongImpressionLogFile() {
	    File file = new File("click_log.csv");

	      if (!file.exists()){
		        Assert.fail("Click log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("impresF", file);
		Assert.assertFalse(isFileGood);
	}

	@Test
	public void loadWrongClickLogFile() {
	    File file = new File("server_log.csv");

	      if (!file.exists()){
		        Assert.fail("Sever log file not available in current directory.");
	      }

		boolean isFileGood = parser.parseLogFile("clickF", file);
		Assert.assertFalse(isFileGood);
	}


	@Test
	public void testGetClickWrapper() {
		File file = new File("click_log.csv");

		 if (!file.exists()){
		        Assert.fail("Click log file not available in current directory.");
	      }

		 parser.parseLogFile("clickF", file);
		 parser.instantiateRecordsWrappers();
		 ArrayList<ClickRecord> clicks = parser.getClickWrapper().getClicks();
		 Assert.assertEquals(clicks.size(),23923);

	}

	@Test
	public void testGetImpressionWrapper() {
		File file = new File("impression_log.csv");

		 if (!file.exists()){
		        Assert.fail("Impression log file not available in current directory.");
	      }

		 parser.parseLogFile("impresF", file);
		 parser.instantiateRecordsWrappers();
		 ArrayList<ImpressionRecord> impressions = parser.getImpressionWrapper().getImpressions();
		 Assert.assertEquals(impressions.size(),486104);
	}

	@Test
	public void testGetServerWrapper() {

		File file = new File("server_log.csv");

		 if (!file.exists()){
		        Assert.fail("Server log file not available in current directory.");
	      }

		 parser.parseLogFile("serverF", file);
		 parser.instantiateRecordsWrappers();
		 ArrayList<ServerRecord> serverRecordsArray = parser.getServerWrapper().getServerRecArray();
		 Assert.assertEquals(serverRecordsArray.size(),23923);
	}


}
