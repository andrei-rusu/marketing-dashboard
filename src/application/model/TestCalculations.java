package application.model;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;



public class TestCalculations {

	Parser parser = new Parser();

	File clickLogFile;
	File impresLogFile;
	File serverLogFile;

	@Test
	public void parseClickLog() {
		clickLogFile = new File("click.csv");
		boolean isGoodFile = parser.parseLogFile("clickF", clickLogFile);

		Assert.assertTrue(isGoodFile);
	}

	@Test
	public void parseImpresLog() {
		impresLogFile = new File("impres.csv");
		boolean isGoodFile = parser.parseLogFile("impresF", impresLogFile);

		Assert.assertTrue(isGoodFile);
	}

	@Test
	public void parseServerLog() {
		serverLogFile = new File("server.csv");
		boolean isGoodFile = parser.parseLogFile("serverF", serverLogFile);

		Assert.assertTrue(isGoodFile);
	}

	@Test
	public void testTotalCost(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		calculationsManager.calcTotalCost();

		double expectedTotalCost = calculationsManager.getTotalCost();

		Assert.assertEquals(expectedTotalCost, 42.695644 , 0.000001);

	}

	@Test
	public void testCTR(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();

		double expectedCtr = calculationsManager.calcCTR();

		Assert.assertEquals(expectedCtr, 100 , 0);

	}

	@Test
	public void testCPA(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		calculationsManager.calcTotalCost();
		double expectedCpa = calculationsManager.calcCPA();

		Assert.assertEquals(expectedCpa, 21.348 , 0);

	}

	@Test
	public void testCPC(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		calculationsManager.calcTotalCost();
		double expectedCpc = calculationsManager.calcCPC();

		Assert.assertEquals(expectedCpc, 4.270 , 0);

	}

	@Test
	public void testCPM(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		calculationsManager.calcTotalCost();
		double expectedCpm = calculationsManager.calcCPM();

		Assert.assertEquals(expectedCpm, 4269.564 , 0);

	}

	@Test
	public void testUniques(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		int uniques = parser.getUniqueId().size();

		Assert.assertEquals(uniques, 10 , 0);

	}

	@Test
	public void testConversions(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		int conversions = parser.getConversionCount();

		Assert.assertEquals(conversions, 2 , 0);

	}

	@Test
	public void testBounces(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		Calculations calculations = new Calculations();
		calculationsManager.filterBounceTime(calculations, 5);
		double expectedBounce = calculations.getBounces();

		Assert.assertEquals(expectedBounce, 1 , 0);

	}

	@Test
	public void testBounceRate(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();
		CalculationsManager calculationsManager = parser.getCalculationsManager();
		Calculations calculations = new Calculations();
		calculationsManager.filterBounceTime(calculations, 5);
		calculations.getBounces();
		parser.getClickNum();
		double expectedBounceRate = calculationsManager.getBounceRate();

		Assert.assertEquals(expectedBounceRate, 10 , 0);

	}

	@Test
	public void testResetWrappers(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		parser.instantiateRecordsWrappers();

		parser.resetCalculationValues();

		Assert.assertTrue(parser.getClickWrapper() == null && parser.getImpressionWrapper() == null
				&& parser.getServerWrapper() == null && parser.getPersonWrapper() == null
				&& parser.getCalculationsManager() == null);


	}


	@Test
	public void testParserCopy(){
		parseClickLog();
		parseImpresLog();
		parseServerLog();

		Parser copied = new Parser();
		copied.copy(parser);


		Assert.assertTrue(copied.getTotalCost() == parser.getTotalCost());


	}
	
	//Test Over Days Calculations
	
	@Test
	public void testClickNumberOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getClicksOverDays().get("2015-01-01") , 10 , 0);


	}

	@Test
	public void testClickCostOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getTotalCostOverDays().get("2015-01-01") , 42.681 , 0.0001) ;


	}

	@Test
	public void testImpressionsOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ImpressionLogWrapper wrap = parser.getImpressionWrapper();

		Assert.assertEquals(wrap.getImpressionsOverDays().get("2015-01-01") , 10 , 0) ;

	}

	@Test
	public void testUniquesOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getUniquesOverDays().get("2015-01-01"), 10 , 0);


	}

	@Test
	public void testConversionsOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ServerLogWrapper wrap = parser.getServerWrapper();

		Assert.assertEquals(wrap.getConversionsOverDays().get("2015-01-01"), 2 , 0);


	}

	@Test
	public void testBouncesOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ServerLogWrapper wrap = parser.getServerWrapper();

		Assert.assertEquals(wrap.getBouncesOverTime(0, 5 , 1).get("2015-01-01"), 1 , 0);


	}

	@Test
	public void testBouncesRateOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		modelController.getBounceRateOverTime(0, 5 , 1);

		Assert.assertEquals(modelController.getBounceRateOverTime(0, 5 , 1).get("2015-01-01"), 10 , 0);


	}

	@Test
	public void testCTROverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCTROverDays().get("2015-01-01"), 100 , 0);

	}

	@Test
	public void testCPAOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPAOverDays().get("2015-01-01"), 21.3405 , 0.0001);

	}

	@Test
	public void testCPCOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPCOverDays().get("2015-01-01"), 4.268 , 0.001);

	}

	@Test
	public void testCPMOverDays(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPMOverDays().get("2015-01-01"), 4268.1082 , 0);

	}
	
	//Test Over Hours Calculations
	
	@Test
	public void testClickNumberOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getClicksOverHours().get("2015-01-01 12") , 10 , 0);


	}

	@Test
	public void testClickCostOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getTotalCostOverHours().get("2015-01-01 12") , 42.681 , 0.0001) ;


	}

	@Test
	public void testImpressionsOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ImpressionLogWrapper wrap = parser.getImpressionWrapper();

		Assert.assertEquals(wrap.getImpressionsOverHours().get("2015-01-01 12") , 10 , 0) ;

	}

	@Test
	public void testUniquesOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getUniquesOverHours().get("2015-01-01 12"), 10 , 0);


	}

	@Test
	public void testConversionsOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ServerLogWrapper wrap = parser.getServerWrapper();
		
		Assert.assertEquals(wrap.getConversionsOverHours().get("2015-01-01 12"), 2 , 0);


	}
	
	@Test
	public void testCPCOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPCOverHours().get("2015-01-01 12"), 4.268 , 0.001);

	}

	@Test
	public void testCPMOverHours(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPMOverHours().get("2015-01-01 12"), 4268.1082 , 0);

	}
	
	//Test Over Weeks Calculations
	
	@Test
	public void testClickNumberOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();
		
		Assert.assertEquals(wrap.getClicksOverWeeks().get("Week 1") , 10 , 0);


	}

	@Test
	public void testClickCostOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getTotalCostOverWeeks().get("Week 1") , 42.681 , 0.0001) ;


	}

	@Test
	public void testImpressionsOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ImpressionLogWrapper wrap = parser.getImpressionWrapper();

		Assert.assertEquals(wrap.getImpressionsOverWeeks().get("Week 1") , 10 , 0) ;

	}

	@Test
	public void testUniquesOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ClickLogWrapper wrap = parser.getClickWrapper();

		Assert.assertEquals(wrap.getUniquesOverWeeks().get("Week 1"), 10 , 0);


	}

	@Test
	public void testConversionsOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);
		parser.instantiateRecordsWrappers();
		ServerLogWrapper wrap = parser.getServerWrapper();

		Assert.assertEquals(wrap.getConversionsOverWeeks().get("Week 1"), 2 , 0);


	}
	
	@Test
	public void testCPCOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPCOverWeeks().get("Week 1"), 4.268 , 0.001);

	}

	@Test
	public void testCPMOverWeeks(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();

		Assert.assertEquals(modelController.getCPMOverWeeks().get("Week 1"), 4268.1082 , 0);

	}
	
	//Test Filtering
	
	@Test
	public void testFilterImpressLog(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();
		HashSet<Integer> context = new HashSet<>();
		context.add(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		Assert.assertEquals(modelController.filterImpressionLog(context, localDate, localDate).get(0).getContext(), 3 , 0);

	}
	
	@Test
	public void testFilterServerLog(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();
		HashSet<Integer> context = new HashSet<>();
		context.add(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		ArrayList<ImpressionRecord> impress = modelController.filterImpressionLog(
							context, localDate , localDate);
		
		Assert.assertTrue(modelController.filterServerLog(localDate, localDate, impress).isEmpty());

	}
	
	@Test
	public void testFilterClickLog(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();
		HashSet<Integer> context = new HashSet<>();
		context.add(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		ArrayList<ImpressionRecord> impress = modelController.filterImpressionLog(
							context, localDate , localDate);
		
		//Assert.assertTrue(modelController.filterClickLog(localDate, localDate, impress).isEmpty());
		Assert.assertEquals(modelController.filterClickLog(localDate, localDate, impress).size(), 0);
	}
	
	@Test
	public void testFilterPersonLog(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();
		HashSet<Integer> audience = new HashSet<>();
		audience.add(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		ArrayList<ImpressionRecord> impress = modelController.filterImpressionLog(
				audience, localDate , localDate);
		
		Assert.assertTrue(modelController.filterPersonLog(audience, impress).isEmpty());

	}
	
	@Test
	public void testFilterFinalPersonLog(){

		ModelController modelController = new ModelController();
		Parser parser = modelController.getParser();
		clickLogFile = new File("click.csv");
		parser.parseLogFile("clickF", clickLogFile);
		impresLogFile = new File("impres.csv");
		parser.parseLogFile("impresF", impresLogFile);
		serverLogFile = new File("server.csv");
		parser.parseLogFile("serverF", serverLogFile);

		parser.instantiateRecordsWrappers();
		HashSet<Integer> audience = new HashSet<>();
		audience.add(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		ArrayList<ImpressionRecord> impress = modelController.filterImpressionLog(
				audience, localDate , localDate);
		
		ArrayList<PersonRecord> persons = modelController.filterPersonLog(audience, impress);
		
		Assert.assertTrue(modelController.filterFinalImpressionLog(persons, impress).isEmpty());

	}
	
	@Test
	public void tesDateEquality(){

		ModelController modelController = new ModelController();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse("2015-01-01" , formatter);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = format.parse("2015-01-01");
			Assert.assertTrue(modelController.isRecordDateOnSameDayAsFilterDate(date, localDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
