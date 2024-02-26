package application.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServerRecord {

	private Date entryDate;
	private Double id;
	private Date exitDate;
	private int pagesViewed;
	private boolean conversion;

	public ServerRecord(Date entryDate1, Double id1, Date exitDate1, int pagesViewed1, boolean con){
		this.entryDate = entryDate1;
		this.id = id1;
		this.exitDate = exitDate1;
		this.pagesViewed = pagesViewed1;
		this.conversion = con;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public Double getId() {
		return id;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public int getPagesViewed() {
		return pagesViewed;
	}

	public boolean isConversion() {
		return conversion;
	}

	public String getWeek(){

		Calendar cal = Calendar.getInstance();
		cal.setTime(entryDate);
		int week = cal.get(Calendar.WEEK_OF_YEAR);

		return "Week " + String.valueOf(week);
	}

	public String getYearMonthDate(){
		SimpleDateFormat datesFormat = new SimpleDateFormat ("yyyy-MM-dd");

		return datesFormat.format(entryDate);
	}

	public String getYearMonthDateHour(){
		SimpleDateFormat datesFormat = new SimpleDateFormat ("yyyy-MM-dd HH");

		return datesFormat.format(entryDate);
	}


}
