package application.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Main;
import application.model.ClickLogWrapper;
import application.model.ClickRecord;
import application.model.ImpressionLogWrapper;
import application.model.ImpressionRecord;
import application.model.PersonRecord;
import application.model.PersonWrapper;
import application.model.ServerLogWrapper;
import application.model.ServerRecord;
import application.util.LineChartScaleAnimation;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class LineChartTabController implements TabController {

	@FXML
	private LineChart<String, Number> chart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	private Main mainApp;

	private int type;
	private int timeGranularity;

	private List<Integer> bounceRate;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {

		chart.setAnimated(false);
	}

	@Override
	public void setData() {

		chart.getData().clear();
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		switch (type) {

		case 4: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Impressions");

			ImpressionLogWrapper impressionRecords = mainApp.getModelController().getFilterImpressionWrapper();

			if (timeGranularity == 0) {
				chart.setTitle("Impressions over Hours");
				for (Map.Entry<String, Long> entry : impressionRecords.getImpressionsOverHours().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));

				}
			} else if (timeGranularity == 1) {
				chart.setTitle("Impressions over Days");
				for (Map.Entry<String, Long> entry : impressionRecords.getImpressionsOverDays().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}

			} else {
				chart.setTitle("Impressions over Weeks");

				for (Map.Entry<String, Long> entry : impressionRecords.getImpressionsOverWeeks().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}

			}

			break;

		}
		case 5: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Clicks");

			ClickLogWrapper clickRecords = mainApp.getModelController().getFilterClickLogWrapper();

			if (timeGranularity == 0) {

				chart.setTitle("Clicks over Hours");

				for (Map.Entry<String, Long> entry : clickRecords.getClicksOverHours().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("Clicks over Days");

				for (Map.Entry<String, Long> entry : clickRecords.getClicksOverDays().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {
				chart.setTitle("Clicks over Weeks");

				for (Map.Entry<String, Long> entry : clickRecords.getClicksOverWeeks().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			}

			break;

		}
		case 6: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Uniques");

			ClickLogWrapper clickRecords = mainApp.getModelController().getFilterClickLogWrapper();

			if (timeGranularity == 0) {

				chart.setTitle("Uniques over Hours");

				for (Map.Entry<String, Long> entry : clickRecords.getUniquesOverHours().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("Uniques over Days");

				for (Map.Entry<String, Long> entry : clickRecords.getUniquesOverDays().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {

				chart.setTitle("Uniques over Weeks");

				for (Map.Entry<String, Long> entry : clickRecords.getUniquesOverWeeks().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}

			}

			break;

		}
		case 7: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Bounces");

			int bounceType = 0;
			int bounceTypeValue = 5;

			if (bounceRate != null) {
				bounceType = bounceRate.get(0);
				bounceTypeValue = bounceRate.get(1);
			}

			if (timeGranularity == 0) {
				if (bounceType == 0) {
					chart.setTitle("Bounces over Hours - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle(
							"Bounces over Hours - Bounce registered as number of pages visited: " + bounceTypeValue);
				}
			} else if (timeGranularity == 1) {
				if (bounceType == 0) {
					chart.setTitle("Bounces over Days - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle(
							"Bounces over Days - Bounce registered as number of pages visited: " + bounceTypeValue);
				}
			} else {
				if (bounceType == 0) {
					chart.setTitle("Bounces over Weeks - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle(
							"Bounces over Weeks - Bounce registered as number of pages visited: " + bounceTypeValue);
				}
			}

			Map<String, Integer> bounceMap = mainApp.getModelController().getFilterSeverLogWrapper()
					.getBouncesOverTime(bounceType, bounceTypeValue, timeGranularity);

			for (Map.Entry<String, Integer> entry : bounceMap.entrySet()) {
				series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}

			break;
		}
		case 8: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Conversions");

			ServerLogWrapper serverRecords = mainApp.getModelController().getFilterSeverLogWrapper();

			if (timeGranularity == 0) {

				chart.setTitle("Conversions over Hours");

				for (Map.Entry<String, Long> entry : serverRecords.getConversionsOverHours().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("Conversions over Days");

				for (Map.Entry<String, Long> entry : serverRecords.getConversionsOverDays().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {

				chart.setTitle("Conversions over Weeks");

				for (Map.Entry<String, Long> entry : serverRecords.getConversionsOverWeeks().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}

			}

			break;
		}
		case 9: {

			xAxis.setLabel("Time");
			yAxis.setLabel("Total Cost (£)");

			ClickLogWrapper clickOverTimeRecords = mainApp.getModelController().getFilterClickLogWrapper();

			if (timeGranularity == 0) {

				chart.setTitle("Total Cost over Hours");

				for (Map.Entry<String, Double> entry : clickOverTimeRecords.getTotalCostOverHours().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), (entry.getValue() / 100)));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("Total Cost over Days");

				for (Map.Entry<String, Double> entry : clickOverTimeRecords.getTotalCostOverDays().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), (entry.getValue() / 100)));
				}
			} else {
				chart.setTitle("Total Cost over Weeks");

				for (Map.Entry<String, Double> entry : clickOverTimeRecords.getTotalCostOverWeeks().entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), (entry.getValue() / 100)));
				}
			}

			break;

		}
		case 10: {

			xAxis.setLabel("Time");
			yAxis.setLabel("CTR (%)");

			if (timeGranularity == 0) {

				chart.setTitle("CTR over Hours");

				Map<String, Double> ctrMap = mainApp.getModelController().getCTROverHours();

				for (Map.Entry<String, Double> entry : ctrMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("CTR over Days");

				Map<String, Double> ctrMap = mainApp.getModelController().getCTROverDays();

				for (Map.Entry<String, Double> entry : ctrMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {

				chart.setTitle("CTR over Weeks");

				Map<String, Double> ctrMap = mainApp.getModelController().getCTROverWeeks();

				for (Map.Entry<String, Double> entry : ctrMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			}

			break;

		}
		case 11: {

			xAxis.setLabel("Time");
			yAxis.setLabel("CPA (Pence)");

			if (timeGranularity == 0) {

				chart.setTitle("CPA over Hours");

				Map<String, Double> cpaMap = mainApp.getModelController().getCPAOverHours();

				for (Map.Entry<String, Double> entry : cpaMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("CPA over Days");

				Map<String, Double> cpaMap = mainApp.getModelController().getCPAOverDays();

				for (Map.Entry<String, Double> entry : cpaMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {

				chart.setTitle("CPA over Weeks");

				Map<String, Double> cpaMap = mainApp.getModelController().getCPAOverWeeks();

				for (Map.Entry<String, Double> entry : cpaMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			}

			break;

		}
		case 12: {

			xAxis.setLabel("Time");
			yAxis.setLabel("CPC (Pence)");

			if (timeGranularity == 0) {

				chart.setTitle("CPC over Hours");

				Map<String, Double> cpcMap = mainApp.getModelController().getCPCOverHours();

				for (Map.Entry<String, Double> entry : cpcMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("CPC over Days");

				Map<String, Double> cpcMap = mainApp.getModelController().getCPCOverDays();

				for (Map.Entry<String, Double> entry : cpcMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {
				chart.setTitle("CPC over Weeks");

				Map<String, Double> cpcMap = mainApp.getModelController().getCPCOverWeeks();

				for (Map.Entry<String, Double> entry : cpcMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			}

			break;

		}
		case 13: {

			xAxis.setLabel("Time");
			yAxis.setLabel("CPM (Pence)");

			if (timeGranularity == 0) {

				chart.setTitle("CPM over Hours");

				Map<String, Double> cpMMap = mainApp.getModelController().getCPMOverHours();

				for (Map.Entry<String, Double> entry : cpMMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else if (timeGranularity == 1) {

				chart.setTitle("CPM over Days");

				Map<String, Double> cpMMap = mainApp.getModelController().getCPMOverDays();

				for (Map.Entry<String, Double> entry : cpMMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			} else {
				chart.setTitle("CPM over Weeks");

				Map<String, Double> cpMMap = mainApp.getModelController().getCPMOverWeeks();

				for (Map.Entry<String, Double> entry : cpMMap.entrySet()) {
					series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}
			}

			break;
		}
		case 14: {
			xAxis.setLabel("Time");
			yAxis.setLabel("Bounce Rate (%)");

			int bounceType = 0;
			int bounceTypeValue = 5;

			if (bounceRate != null) {
				bounceType = bounceRate.get(0);
				bounceTypeValue = bounceRate.get(1);
			}

			if (timeGranularity == 0) {
				if (bounceType == 0) {
					chart.setTitle("Bounce Rate over Hours - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle("Bounce Rate over Hours - Bounce registered as number of pages visited: "
							+ bounceTypeValue);
				}
			} else if (timeGranularity == 1) {
				if (bounceType == 0) {
					chart.setTitle("Bounce Rate over Days - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle(
							"Bounce Rate over Days - Bounce registered as number of pages visited: " + bounceTypeValue);
				}
			} else {
				if (bounceType == 0) {
					chart.setTitle("Bounce Rate over Weeks - Bounce registered as time spent on the website: "
							+ bounceTypeValue + " seconds");
				} else {
					chart.setTitle("Bounce Rate over Weeks - Bounce registered as number of pages visited: "
							+ bounceTypeValue);
				}
			}

			Map<String, Double> bounceRateMap = mainApp.getModelController().getBounceRateOverTime(bounceType,
					bounceTypeValue, timeGranularity);

			for (Map.Entry<String, Double> entry : bounceRateMap.entrySet()) {
				series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}

			break;
		}
		}

		chart.getData().add(series);
		for (int i = 0; i < series.getData().size(); i++) {
			XYChart.Data<String, Number> item = (XYChart.Data<String, Number>) series.getData().get(i);
			item.getNode().setOnMouseEntered(new LineChartScaleAnimation(chart, item, true));
			item.getNode().setOnMouseExited(new LineChartScaleAnimation(chart, item, false));

			final Tooltip tooltip = new Tooltip(
					"Date: " + item.getXValue().toString() + "\n" + "Value: " + item.getYValue().toString());
			tooltip.setFont(new Font("Calibri", 14));
			Tooltip.install(item.getNode(), tooltip);
		}
	}

	public void setType(int type, int timeGranularity) {
		this.type = type;
		this.timeGranularity = timeGranularity;
	}

	@Override
	public void filter(final List<Integer> bounceRate, final List<LocalDate> dates,
			final Map<Integer, Boolean> audience, final Map<Integer, Boolean> context) {

		int noOfThreads = Runtime.getRuntime().availableProcessors();
    	ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

    	Future<?> future = es.submit(() -> {
    		this.bounceRate = bounceRate;

    		if (bounceRate.get(0) == 0) {
    			mainApp.getModelController().getCalculationsManager().filterBounceTime(bounceRate.get(1));
    		} else {
    			mainApp.getModelController().getCalculationsManager().filterBouncePages(bounceRate.get(1));
    		}

    		LocalDate startDate = LocalDate.ofYearDay(1960, 1);
    		LocalDate endDate = LocalDate.now();

    		HashSet<Integer> selectedAudienceSet = new HashSet<>();
    		for (Map.Entry<Integer, Boolean> entry : audience.entrySet()) {
    			if (entry.getValue().booleanValue()) {
    				selectedAudienceSet.add(entry.getKey());
    			}
    		}

    		HashSet<Integer> selectedContextSet = new HashSet<>();
    		for (Map.Entry<Integer, Boolean> entry : context.entrySet()) {
    			if (entry.getValue().booleanValue()) {
    				selectedContextSet.add(entry.getKey());
    			}
    		}

    		if (dates != null) {
    			if (dates.get(0) != null && dates.get(1) != null) {
    				startDate = dates.get(0);
    				endDate = dates.get(1);
    			}

    			if (dates.get(0) != null && dates.get(1) == null) {
    				startDate = dates.get(0);
    			}

    			if (dates.get(0) == null && dates.get(1) != null) {
    				endDate = dates.get(1);
    			}
    		}

    		ArrayList<ImpressionRecord> newImpressionsArray = mainApp.getModelController()
    				.filterImpressionLog(selectedContextSet, startDate, endDate);
    		ArrayList<PersonRecord> newPeopleArray = mainApp.getModelController()
    				.filterPersonLog(selectedAudienceSet, newImpressionsArray);
    		ArrayList<ImpressionRecord> finalImpress = mainApp.getModelController()
    				.filterFinalImpressionLog(newPeopleArray, newImpressionsArray);
    		ArrayList<ServerRecord> newServerArray = mainApp.getModelController()
    				.filterServerLog(startDate, endDate, finalImpress);
    		ArrayList<ClickRecord> newClickArray = mainApp.getModelController()
    				.filterClickLog(startDate, endDate, finalImpress);

    		ImpressionLogWrapper imprWrapper = new ImpressionLogWrapper(finalImpress);
    		PersonWrapper personWrapper = new PersonWrapper(newPeopleArray);
    		ServerLogWrapper servWrapper = new ServerLogWrapper(newServerArray);
    		ClickLogWrapper clickWrapper = new ClickLogWrapper(newClickArray);

    		mainApp.getModelController().setFilterImpressionWrapper(imprWrapper);
    		mainApp.getModelController().setFilterPersonWrapper(personWrapper);
    		mainApp.getModelController().setFilterClickLogWrapper(clickWrapper);
    		mainApp.getModelController().setFilterSeverLogWrapper(servWrapper);
    	});

    	try {
			future.get();
	    	es.shutdown();
			setData();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void reset() {

		int noOfThreads = Runtime.getRuntime().availableProcessors();
    	ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

    	Future<?> future = es.submit(() -> {
    		if (bounceRate != null) {
    			bounceRate.clear();
    			bounceRate = null;
    		}


    		mainApp.getModelController().setFilterImpressionWrapper(mainApp.getModelController().getImpressionWrapper());
    		mainApp.getModelController().setFilterPersonWrapper(mainApp.getModelController().getPersonWrapper());
    		mainApp.getModelController().setFilterClickLogWrapper(mainApp.getModelController().getClickLogWrapper());
    		mainApp.getModelController().setFilterSeverLogWrapper(mainApp.getModelController().getSeverLogWrapper());
    	});

    	try {
			future.get();
	    	es.shutdown();
			setData();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
}
