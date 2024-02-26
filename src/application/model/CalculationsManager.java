package application.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import application.util.NumberFormatter;


public class CalculationsManager {

	double totalCost = 0;
	double totalImpressionCost = 0;
	double totalClickCost = 0;
	int impressNum = 0;
	int clickNum = 0;
	int conversionCount = 0;
	int bounces = 0;
	double bounceRate = 0;
	HashSet<Double> uniqueId;

	ArrayList<Integer> bouncesPerTime;
	ArrayList<Integer> bouncesPerPage;

	NumberFormatter formatter;

	public CalculationsManager(Parser p){

		this.totalCost = p.getTotalCost();
		this.totalImpressionCost = p.getTotalImpressionCost();
		this.totalClickCost = p.getTotalClickCost();
		this.impressNum = p.getImpressNum();
		this.clickNum = p.getClickNum();
		this.conversionCount = p.getConversionCount();
		this.uniqueId = new HashSet<>(p.getUniqueId());

		bouncesPerTime = new ArrayList<>(p.getBouncesPerTime());
		bouncesPerPage = new ArrayList<>(p.getBouncesPerPages());

		formatter = new NumberFormatter();

	}

	public void modifyCalculations(Calculations calculations) {

		calcTotalCost();
		double roundedTotalCost = formatter.format(totalCost/100);
		double roundedTotalImpressionCost = formatter.format(totalImpressionCost/100);
		double roundedTotalClickCost = formatter.format(totalClickCost/100);
		calculations.setAllFields(impressNum, clickNum, uniqueId.size(), conversionCount, filterBounceTime(calculations, 5),
				calculateBounceRate(bounces), roundedTotalImpressionCost, roundedTotalClickCost, roundedTotalCost, calcCTR(),
				calcCPA(), calcCPC(), calcCPM());
	}

	public void setImpressions(Calculations calculations, int impr) {
		calculations.setImpressions(impr);
	}


	public void setClicks(Calculations calculations, int cl) {
		calculations.setClicks(cl);
	}


	public void setUniques(Calculations calculations, int uniq) {
		calculations.setUniques(uniq);
	}


	public void setConversions(Calculations calculations, int conv) {
		calculations.setConversions(conv);
	}


	public void setImpressionCost(Calculations calculations, double imprCost) {
		calculations.setImpressionCost(imprCost);
	}


	public void setClickCost(Calculations calculations, double clCost) {
		calculations.setClickCost(clCost);
	}


	public void setTotalCost(Calculations calculations, double totCost) {
		calculations.setTotalCost(totCost);
	}


	public void setCTR(Calculations calculations, double cTR) {
		calculations.setCTR(cTR);
	}


	public void setCPA(Calculations calculations, double cPA) {
		calculations.setCPA(cPA);
	}


	public void setCPC(Calculations calculations, double cPC) {
		calculations.setCPC(cPC);
	}


	public void setCPM(Calculations calculations, double cPM) {
		calculations.setCPM(cPM);
	}

	//clicks/impressions
	public double calcCTR(){

		if(impressNum != 0){
			double ctr = (double) clickNum/impressNum;

			return formatter.format(ctr*100);
		}

		return 0;
	}

	public double calcTotalCost(){

		totalCost = totalClickCost + totalImpressionCost;

		return (totalCost != 0) ? formatter.format(totalCost/100) : 0;
	}

	public double calcCPA(){

		if(conversionCount != 0){
		double cpa = totalCost/conversionCount;

		return formatter.format(cpa);
		}

		return 0;
	}

	public double calcCPC(){

		if(clickNum != 0){
		double cpc = totalCost/clickNum;

		return formatter.format(cpc);
		}
		return 0;
	}

	//total cost / impression count * 1000
	public double calcCPM(){

		if(impressNum != 0){
		double cpm = (double) ((totalCost /impressNum)*1000);

		return formatter.format(cpm);
		}

		return 0;
	}

	public int filterBounceTime(Calculations calculations, int seconds){

		bounces = Math.toIntExact(bouncesPerTime.stream()
	    .filter(s -> s <= seconds).count());

		calculateBounceRate(bounces);
		calculations.setBounce(bounces);
		calculations.setBounceRate(calculateBounceRate(bounces));

		return bounces;
	}

	public int filterBouncePages(Calculations calculations, int pages){
		bounces = Math.toIntExact(bouncesPerPage.stream()
		.filter(s -> s <= pages).count());

		calculateBounceRate(bounces);
		calculations.setBounce(bounces);
		calculations.setBounceRate(calculateBounceRate(bounces));

		return bounces;
	}

	public int filterBounceTime(int seconds){

		bounces = Math.toIntExact(bouncesPerTime.stream()
	    .filter(s -> s <= seconds).count());

		calculateBounceRate(bounces);

		return bounces;
	}

	public int filterBouncePages(int pages){
		bounces = Math.toIntExact(bouncesPerPage.stream()
		.filter(s -> s <= pages).count());

		calculateBounceRate(bounces);
		return bounces;
	}

	public double calculateBounceRate(int bounces){

		if(clickNum != 0){
		bounceRate = ((bounces/(double) clickNum) * 100);

		return formatter.format(bounceRate);
		}

		return 0;
	}

	public int getBounces() {
		return bounces;
	}

	public double getBounceRate() {
		return bounceRate;
	}

	protected double getTotalCost(){
		return totalCost;
	}

	public HashSet<Double> calculateUniques(ClickLogWrapper fitleredWrapper) {

		HashSet<Double> uniques = fitleredWrapper.clicks.parallelStream()
				.map(i -> i.id)
				.collect(Collectors.toCollection(HashSet::new));

		this.uniqueId = new HashSet<>(uniques);
		return uniqueId;
	}


	public int calculateConversions(ServerLogWrapper fitleredWrapper) {

		Long conversions = fitleredWrapper.serverRecArray.parallelStream()
				.filter(c -> c.isConversion())
				.count();

		this.conversionCount = Math.toIntExact(conversions);
		return conversionCount;
	}


	public Double calculateImprCost(ImpressionLogWrapper filteredWrapper) {
		Double imprCost = filteredWrapper.impressions.parallelStream()
				.mapToDouble(i -> i.getImpressionCost()).sum();

		this.totalImpressionCost = imprCost;
		return (imprCost != 0) ? formatter.format(totalImpressionCost/100) : 0;
	}


	public Double calculateClickCost(ClickLogWrapper filteredWrapper) {
		Double clickCost = filteredWrapper.clicks.parallelStream()
				.mapToDouble(i -> i.getClickCost()).sum();

		this.totalClickCost = clickCost;
		return (totalClickCost != 0) ? formatter.format(totalClickCost/100) : 0;
	}

	public void setDefaultBounceAndBounceRate(Calculations calculations) {
		this.bounces = this.filterBounceTime(5);
		this.bounceRate = this.calculateBounceRate(bounces);

		calculations.setBounce(bounces);
		calculations.setBounceRate(bounceRate);

	}

	public void setImpressions(int impressions) {
		this.impressNum = impressions;
	}

	public void setClicks(int clicks) {
		this.clickNum = clicks;
	}

	public void setBounce(Calculations calculations, int bounce){
		this.bounces = 0;
		calculations.setBounce(0);
	}


	public void setBounceRate(Calculations calculations, int bounceRate){
		this.bounceRate = 0;
		calculations.setBounceRate(0);
	}
}
