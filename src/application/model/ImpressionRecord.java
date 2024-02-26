package application.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImpressionRecord {

	Date date;
	Double id;
	int context;
	double impressionCost;

	public ImpressionRecord(Date d, Double id1, int con, double impr){
		this.date = d;
		this.id = id1;
		this.context = con;
		this.impressionCost = impr;
	}

	public String getWeek(){

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.WEEK_OF_YEAR);

		return "Week " + String.valueOf(week);
	}

	public String getYearMonthDate(){
		SimpleDateFormat datesFormat = new SimpleDateFormat ("yyyy-MM-dd");

		return datesFormat.format(date);
	}

	public String getYearMonthDateHour(){
		SimpleDateFormat datesFormat = new SimpleDateFormat ("yyyy-MM-dd HH");

		return datesFormat.format(date);
	}

	public Date getDate() {
		return date;
	}

	public Double getId() {
		return id;
	}

	public int getContext() {
		return context;
	}

	public double getImpressionCost() {
		return impressionCost;
	}

	 @Override
	    public boolean equals(Object o) {

	        if (o == this) return true;
	        if (o instanceof PersonRecord) {
	        	PersonRecord p = (PersonRecord) o;
	        	return p.id == this.id;
	        } else if(o instanceof ImpressionRecord){
	        	ImpressionRecord i = (ImpressionRecord) o;
	        	return i.id == this.id;
	        }

	        return false;
	    }

}
