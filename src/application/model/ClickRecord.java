package application.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClickRecord {

	Date date;
	Double id;
	Double clickCost;

	public ClickRecord(Date d, Double id1, Double cost){
		this.date = d;
		this.id = id1;
		this.clickCost = cost;
	}

	public Date getDate() {
		return date;
	}

	public Double getId() {
		return id;
	}

	public Double getClickCost() {
		return clickCost;
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


}
