
package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Calculations {

	private IntegerProperty impressions;
	private IntegerProperty clicks;
	private IntegerProperty uniques;
	private IntegerProperty conversions;
	private IntegerProperty bounces;

	private DoubleProperty impressionCost;
	private DoubleProperty clickCost;
	private DoubleProperty totalCost;
	private DoubleProperty CTR;
	private DoubleProperty CPA;
	private DoubleProperty CPC;
	private DoubleProperty CPM;
	private DoubleProperty bounceRate;


	public Calculations() {
		this.impressions = new SimpleIntegerProperty();
		this.clicks = new SimpleIntegerProperty();
		this.uniques = new SimpleIntegerProperty();
		this.conversions = new SimpleIntegerProperty();
		this.bounces = new SimpleIntegerProperty();
		this.impressionCost = new SimpleDoubleProperty();
		this.clickCost = new SimpleDoubleProperty();
		this.totalCost = new SimpleDoubleProperty();
		this.CTR = new SimpleDoubleProperty();
		this.CPA = new SimpleDoubleProperty();
		this.CPC = new SimpleDoubleProperty();
		this.CPM = new SimpleDoubleProperty();
		this.bounceRate = new SimpleDoubleProperty();
	}


	public void setAllFields(int impressions, int clicks, int uniques, int conversions, int bounces,double bounceRate,
			double imprCost, double clickCost, double totalCost, double CTR, double CPA, double CPC, double CPM) {

		this.impressions.set(impressions);
		this.clicks.set(clicks);
		this.uniques.set(uniques);
		this.conversions.set(conversions);
		this.bounces.set(bounces);
		this.bounceRate.set(bounceRate);
		this.impressionCost.set(imprCost);
		this.clickCost.set(clickCost);
		this.totalCost.set(totalCost);
		this.CTR.set(CTR);
		this.CPA.set(CPA);
		this.CPC.set(CPC);
		this.CPM.set(CPM);
	}

	public IntegerProperty impressionsProperty() {
		return impressions;
	}

	public IntegerProperty clicksProperty() {
		return clicks;
	}

	public IntegerProperty uniquesProperty() {
		return uniques;
	}

	public IntegerProperty conversionsProperty() {
		return conversions;
	}

	public IntegerProperty bouncesProperty(){
		return bounces;
	}


	public DoubleProperty bounceRateProperty(){
		return bounceRate;
	}

	public DoubleProperty CTRProperty() {
		return CTR;
	}

	public DoubleProperty CPAProperty() {
		return CPA;
	}

	public DoubleProperty CPMProperty() {
		return CPM;
	}

	public DoubleProperty CPCProperty() {
		return CPC;
	}

	public DoubleProperty impressionCostProperty() {
		return impressionCost;
	}

	public DoubleProperty clickCostProperty() {
		return clickCost;
	}

	public DoubleProperty totalCostProperty() {
		return totalCost;
	}


	public int getImpressions() {
		return impressions.get();
	}

	public int getClicks() {
		return clicks.get();
	}

	public int getUniques() {
		return uniques.get();
	}

	public int getConversions() {
		return conversions.get();
	}

	public int getBounces() {
		return bounces.get();
	}

	public Double getBounceRate(){
		return bounceRate.get();
	}

	public DoubleProperty getImpressionCost() {
		return impressionCost;
	}


	public DoubleProperty getClickCost() {
		return clickCost;
	}


	public double getTotalCost() {
		return totalCost.get();
	}

	public double getCTR() {
		return CTR.get();
	}

	public double getCPA() {
		return CPA.get();
	}

	public double getCPC() {
		return CPC.get();
	}

	public double getCPM() {
		return CPM.get();
	}


	public void setImpressions(int impr) {
		this.impressions.set(impr);
	}


	public void setClicks(int cl) {
		this.clicks.set(cl);
	}


	public void setUniques(int uniq) {
		this.uniques.set(uniq);
	}


	public void setConversions(int conv) {
		this.conversions.set(conv);
	}


	public void setImpressionCost(double imprCost) {
		this.impressionCost.set(imprCost);
	}


	public void setClickCost(double clCost) {
		this.clickCost.set(clCost);
	}


	public void setTotalCost(double totCost) {
		this.totalCost.set(totCost);
	}


	public void setCTR(double cTR) {
		CTR.set(cTR);
	}


	public void setCPA(double cPA) {
		CPA.set(cPA);
	}


	public void setCPC(double cPC) {
		CPC.set(cPC);
	}


	public void setCPM(double cPM) {
		CPM.set(cPM);
	}


	// Will make a deep clone of this object.
	public Calculations clone() {

		Calculations calc = new Calculations();
		calc.setAllFields(impressions.get(), clicks.get(), uniques.get(), conversions.get(), bounces.get(), bounceRate.get(),
				impressionCost.get(), clickCost.get(), totalCost.get(), CTR.get(), CPA.get(), CPC.get(), CPM.get());
		return calc;
	}

	public Calculations copy(Calculations calc){

		calc.setAllFields(impressions.get(), clicks.get(), uniques.get(), conversions.get(), bounces.get(), bounceRate.get(),
				impressionCost.get(), clickCost.get(), totalCost.get(), CTR.get(), CPA.get(), CPC.get(), CPM.get());

		return calc;
	}


	public void setBounce(int bounces){
		this.bounces.set(bounces);
	}


	public void setBounceRate(double bounceRate) {
		this.bounceRate.set(bounceRate);;
	}


	public void resetCalculations() {
		this.impressions = new SimpleIntegerProperty();
		this.clicks = new SimpleIntegerProperty();
		this.uniques = new SimpleIntegerProperty();
		this.conversions = new SimpleIntegerProperty();
		this.bounces = new SimpleIntegerProperty();
		this.impressionCost = new SimpleDoubleProperty();
		this.clickCost = new SimpleDoubleProperty();
		this.totalCost = new SimpleDoubleProperty();
		this.CTR = new SimpleDoubleProperty();
		this.CPA = new SimpleDoubleProperty();
		this.CPC = new SimpleDoubleProperty();
		this.CPM = new SimpleDoubleProperty();
		this.bounceRate = new SimpleDoubleProperty();

	}

}

