package application.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import application.Main;


public interface TabController {

	public void setData();

	public void filter(final List<Integer> bounceRate, final List<LocalDate> dates,
			final Map<Integer, Boolean> audience, final Map<Integer, Boolean> context);

	public void reset();

	public void setMainApp(Main main);

	public void setType(int type, int granularity);
}
