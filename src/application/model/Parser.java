package application.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;


public class Parser {

	private ArrayList<ClickRecord> clicks;
	private ArrayList<ImpressionRecord> impressions;
	private ArrayList<ServerRecord> servers;
	private ArrayList<PersonRecord> people;

	private ArrayList<ClickRecord> filterClicks;
	private ArrayList<ImpressionRecord> filterImpressions;
	private ArrayList<ServerRecord> filterServers;
	private ArrayList<PersonRecord> filterPeople;

	private HashSet<Double> uniqueClickId;
	private HashSet<Double> uniqueImpressionId;

	private ClickLogWrapper clickWrapper;
	private ImpressionLogWrapper impressionWrapper;
	private ServerLogWrapper serverWrapper;
	private PersonWrapper personWrapper;
	private CalculationsManager calculationsWrapper;

	private ClickLogWrapper filterClickWrapper;
	private ImpressionLogWrapper filterImpressionWrapper;
	private ServerLogWrapper filterServerWrapper;
	private PersonWrapper filterPersonWrapper;

	private double totalClickCost = 0 ;
	private double totalImpressionCost = 0;
	private int clickNum = 0;
    private int impressNum = 0;
    private int conversionCount = 0;
    private double totalCost = 0;

    private ArrayList<Integer> bouncesPerTime;
    private ArrayList<Integer> bouncesPerPages;

    public Parser(){
		clicks = new ArrayList<ClickRecord>();
		impressions = new ArrayList<ImpressionRecord>();
		servers = new ArrayList<ServerRecord>();
		people = new ArrayList<PersonRecord>();
		filterClicks = new ArrayList<ClickRecord>();
		filterImpressions = new ArrayList<ImpressionRecord>();
		filterServers = new ArrayList<ServerRecord>();
		filterPeople = new ArrayList<PersonRecord>();
		uniqueClickId = new HashSet<Double>();
		uniqueImpressionId = new HashSet<Double>();

		bouncesPerTime = new ArrayList<>();
		bouncesPerPages = new ArrayList<>();

	}


	public boolean parseLogFile(String parseFileIdentifier, File logFile){

			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 	CsvParserSettings settings = new CsvParserSettings();
			 	settings.setHeaderExtractionEnabled(true);
			 	settings.getFormat().setLineSeparator("\n");
			    CsvParser parser = new CsvParser(settings);
			    boolean goodFile = false;


			    // call beginParsing to read records one by one, iterator-style.
			    try {
					parser.beginParsing(new FileReader(logFile.getPath()));
			    String[] row;
			    while ((row = parser.parseNext()) != null) {

			        if(row.length == 3){
			        	//click
			        	if(!parseFileIdentifier.equals("clickF")){
			        		return goodFile;
			        	}

			        	goodFile = true;

			        	Date date = dateFormat.parse(row[0]);
			        	double id = Double.parseDouble(row[1]);
			        	double clickCost = Double.parseDouble(row[2]);
			        	totalClickCost = totalClickCost + clickCost;

			        	uniqueClickId.add(id);
			        	clickNum++;//Number of Clicks


			        	ClickRecord clickRecord = new ClickRecord(date, id, clickCost);
			        	filterClicks.add(clickRecord);
			        	clicks.add(clickRecord);
			        }
			        else if(row.length == 5){
			        	//server

			        	if(!parseFileIdentifier.equals("serverF")){
			        		return goodFile;
			        	}

			        	goodFile = true;

			        	Date date = dateFormat.parse(row[0]);
			        	double id = Double.parseDouble(row[1]);
			        	Date exitDate;
			        	if(row[2].equals("n/a")){
			        		exitDate = dateFormat.parse("0000-00-00 00:00:00");
			        	}else{
			        	    exitDate = dateFormat.parse(row[2]);
			        	}
			        	int pagesViewed = Integer.parseInt(row[3]);


			        	long bouncePerTime = (exitDate.getTime() - date.getTime())/1000;
			        	if(bouncePerTime > 0 && bouncePerTime <= 15){


			        		bouncesPerTime.add(Math.toIntExact(bouncePerTime));
			        	}

			        	if(pagesViewed > 0 && pagesViewed <= 5){
			        		bouncesPerPages.add(pagesViewed);
			        	}

			        	boolean conversion = false;
			        	if(row[4].equals("Yes")){
			        		conversionCount++; //Number of Conversions

			        		conversion = true;
			        	}


			        	ServerRecord serverRecord = new ServerRecord(date, id, exitDate, pagesViewed , conversion);
			        	filterServers.add(serverRecord);
			        	servers.add(serverRecord);
			        }
			        else if(row.length == 7){
			        	//impression
			        	if(!parseFileIdentifier.equals("impresF")){
			        		return goodFile;
			        	}

			        	goodFile = true;
			        	impressNum++; //Number of Impressions

			        	Date date = dateFormat.parse(row[0]);
			        	double id = Double.parseDouble(row[1]);
			        	int gender = 0;
			        	if(row[2].equals("Female")){
			        		gender = 1;
			        	}

			        	int ageGroup = 2; //<25
			        	if(row[3].equals("25-34")){
			        		ageGroup = 3;
			        	}
			        	else if(row[3].equals("35-44")){
			        		ageGroup = 4;
			        	}
			        	else if(row[3].equals("45-54")){
			        		ageGroup = 5;
			        	}
			        	else if(row[3].equals(">54")){
			        		ageGroup = 6;
			        	}

			        	int income = 7; //Low
			        	if(row[4].equals("Medium")){
					        income = 8;
					    }
			        	else if(row[4].equals("High")){
					        income = 9;
					    }

			        	int context = 0; //News
			        	if(row[5].equals("Shopping")){
					        context = 1;
					    }
			        	else if(row[5].equals("Social Media")){
					        context = 2;
					    }
			        	else if(row[5].equals("Blog")){
					        context = 3;
					    }
			        	else if(row[5].equals("Hobbies")){
					        context = 4;
					    }
			        	else if(row[5].equals("Travel")){
					        context = 5;
					    }
			        	double impressionCost = Double.parseDouble(row[6]);
			        	totalImpressionCost= totalImpressionCost + impressionCost;

			        	ImpressionRecord impressionRecord = new ImpressionRecord(date, id, context, impressionCost);
			        	filterImpressions.add(impressionRecord);
			        	impressions.add(impressionRecord);

			        	if(!uniqueImpressionId.contains(id)){
			        	uniqueImpressionId.add(id);

			        	PersonRecord personRecord = new PersonRecord(id,gender,ageGroup,income);
			        	filterPeople.add(personRecord);
			        	people.add(personRecord);

			        	}

			        }else{
			        	goodFile = false;
			        	break;

			        }

			    }
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			    return goodFile;
	}

	public void instantiateRecordsWrappers(){

		clickWrapper = new ClickLogWrapper(clicks);
	    serverWrapper = new ServerLogWrapper(servers);
	    impressionWrapper = new ImpressionLogWrapper(impressions);
	    personWrapper = new PersonWrapper(people);

	    filterClickWrapper = new ClickLogWrapper(filterClicks);
	    filterServerWrapper = new ServerLogWrapper(filterServers);
	    filterImpressionWrapper = new ImpressionLogWrapper(filterImpressions);
	    filterPersonWrapper = new PersonWrapper(filterPeople);


	    calculationsWrapper = new CalculationsManager(this);

	}

	public void resetCalculationValues(){
		clicks.clear();
		impressions.clear();
	    servers.clear();
		people.clear();
		filterClicks.clear();
		filterImpressions.clear();
	    filterServers.clear();
		filterPeople.clear();
		uniqueClickId.clear();
	    bouncesPerTime.clear();
	    bouncesPerPages.clear();
	    uniqueImpressionId.clear();

		clickWrapper = null;
		impressionWrapper = null;
		serverWrapper = null;
		personWrapper = null;
		filterClickWrapper = null;
		filterImpressionWrapper = null;
		filterServerWrapper = null;
		filterPersonWrapper = null;
		calculationsWrapper = null;

		totalClickCost = 0 ;
		totalImpressionCost = 0;
		clickNum = 0;
	    impressNum = 0;
	    conversionCount = 0;
	    totalCost = 0;

	}

	public ClickLogWrapper getClickWrapper() {
		return clickWrapper;
	}


	public ImpressionLogWrapper getImpressionWrapper() {
		return impressionWrapper;
	}


	public ServerLogWrapper getServerWrapper() {
		return serverWrapper;
	}


	public PersonWrapper getPersonWrapper() {
		return personWrapper;
	}


	public ClickLogWrapper getFilterClickWrapper() {
		return filterClickWrapper;
	}


	public ImpressionLogWrapper getFilterImpressionWrapper() {
		return filterImpressionWrapper;
	}


	public ServerLogWrapper getFilterServerWrapper() {
		return filterServerWrapper;
	}


	public PersonWrapper getFilterPersonWrapper() {
		return filterPersonWrapper;
	}


	public CalculationsManager getCalculationsManager() {
		return calculationsWrapper;
	}



	public double getTotalClickCost() {
		return totalClickCost;
	}


	public double getTotalImpressionCost() {
		return totalImpressionCost;
	}


	public int getClickNum() {
		return clickNum;
	}


	public int getImpressNum() {
		return impressNum;
	}


	public int getConversionCount() {
		return conversionCount;
	}


	public double getTotalCost() {
		return totalCost;
	}


	public HashSet<Double> getUniqueId() {
		return uniqueClickId;
	}


	public ArrayList<Integer> getBouncesPerTime() {
		return bouncesPerTime;
	}


	public ArrayList<Integer> getBouncesPerPages() {
		return bouncesPerPages;
	}


	public void copy(Parser parser) {

		this.clicks = new ArrayList<>(parser.clicks);
		this.impressions = new ArrayList<>(parser.impressions);
		this.servers = new ArrayList<>(parser.servers);
		this.people = new ArrayList<>(parser.people);

		this.uniqueClickId = new HashSet<>(parser.uniqueClickId);

		instantiateRecordsWrappers();

	    totalClickCost = parser.getTotalClickCost() ;
		totalImpressionCost = parser.getTotalImpressionCost();
		clickNum = parser.getClickNum();
	    impressNum = parser.getImpressNum();
	    conversionCount = parser.getConversionCount();
	    totalCost = parser.getTotalCost();

	    this.bouncesPerTime = new ArrayList<>(parser.bouncesPerTime);
	    this.bouncesPerPages = new ArrayList<>(parser.bouncesPerPages);


	}


	public void setImpressionArray(ArrayList<ImpressionRecord> impressions) {
		this.impressionWrapper.impressions = new ArrayList<>(impressions);
	}


	public void setClickArray(ArrayList<ClickRecord> clicks) {
		this.clickWrapper.clicks = new ArrayList<>(clicks);
	}


	public void setServerArray(ArrayList<ServerRecord> server) {
		this.serverWrapper.serverRecArray = new ArrayList<>(server);
	}


	public void setPersonArray(ArrayList<PersonRecord> people) {
		this.personWrapper.people = new ArrayList<>(people);
	}


	public void setFilterImpressionArray(ArrayList<ImpressionRecord> impressions) {
		this.filterImpressionWrapper.impressions = new ArrayList<>(impressions);
	}


	public void setFilterClickArray(ArrayList<ClickRecord> clicks) {
		this.filterClickWrapper.clicks = new ArrayList<>(clicks);
	}


	public void setFilterServerArray(ArrayList<ServerRecord> server) {
		this.filterServerWrapper.serverRecArray = new ArrayList<>(server);
	}


	public void setFilterPersonArray(ArrayList<PersonRecord> people) {
		this.filterPersonWrapper.people = new ArrayList<>(people);
	}

	public void resetFilteredArrays(){
		this.filterClicks = new ArrayList<>(clicks);
		this.filterImpressions = new ArrayList<>(impressions);
		this.filterPeople = new ArrayList<>(people);
		this.filterServers = new ArrayList<>(servers);
	}

}
