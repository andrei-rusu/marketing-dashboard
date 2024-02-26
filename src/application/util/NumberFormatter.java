package application.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

	 String pattern = "#";
	  Locale locale = new Locale("en", "UK");
	  private DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);

	  public NumberFormatter(){
			df.applyPattern(pattern);
			df.setMaximumFractionDigits(3);
	  }

	  public double format(double val){
		  return new Double(df.format(val)).doubleValue();
	  }

}
