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
import application.util.BarChartScaleAnimation;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;


public class BarChartTabController implements TabController {

	@FXML
	private BarChart<String,Number> chart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	private Main mainApp;

	private int type;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {

        chart.setCategoryGap(0);
        chart.setBarGap(1);
        chart.setAnimated(false);
	}

	@Override
	public void setData() {

		chart.getData().clear();
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		switch (type) {

			case 1: {

				xAxis.setLabel("Click Cost Division (Pence)");
				yAxis.setLabel("Frequency");

		        chart.setTitle("Click Cost Distribution");

		        ClickLogWrapper clickRecords = mainApp.getModelController().getFilterClickLogWrapper();

		        series.getData().add(new XYChart.Data<>("0-2", clickRecords.getClickCostDistribution(0, 2)));
		        series.getData().add(new XYChart.Data<>("2-4", clickRecords.getClickCostDistribution(2, 4)));
		        series.getData().add(new XYChart.Data<>("4-6", clickRecords.getClickCostDistribution(4, 6)));
		        series.getData().add(new XYChart.Data<>("6-8", clickRecords.getClickCostDistribution(6, 8)));
		        series.getData().add(new XYChart.Data<>("8-10", clickRecords.getClickCostDistribution(8, 10)));
		        series.getData().add(new XYChart.Data<>("10-12", clickRecords.getClickCostDistribution(10, 12)));
		        series.getData().add(new XYChart.Data<>("12-14", clickRecords.getClickCostDistribution(12, 14)));
		        series.getData().add(new XYChart.Data<>("14-17", clickRecords.getClickCostDistribution(14, 17)));
		        break;

			}
			case 2: {

				xAxis.setLabel("Impression Cost Division (Pence)");
				yAxis.setLabel("Frequency");

		        chart.setTitle("Impression Cost Distribution");

		        ImpressionLogWrapper impressionRecords = mainApp.getModelController().getFilterImpressionWrapper();

		        series.getData().add(new XYChart.Data<>("0-0.001", impressionRecords.getImpressionCostDistribution(0f, 0.001f)));
		        series.getData().add(new XYChart.Data<>("0.001-0.002", impressionRecords.getImpressionCostDistribution(0.001f, 0.002f)));
		        series.getData().add(new XYChart.Data<>("0.002-0.003", impressionRecords.getImpressionCostDistribution(0.002f, 0.003f)));
		        series.getData().add(new XYChart.Data<>("0.003-0.004", impressionRecords.getImpressionCostDistribution(0.003f, 0.004f)));
		        series.getData().add(new XYChart.Data<>("0.004-0.005", impressionRecords.getImpressionCostDistribution(0.004f, 0.005f)));
		        break;
			}
		}
		chart.getData().add(series);
		for(int i=0 ; i<series.getData().size() ; i++){
			XYChart.Data<String, Number> item = (XYChart.Data<String, Number>)series.getData().get(i);
			item.getNode().setOnMouseEntered(new BarChartScaleAnimation(chart, item, true));
			item.getNode().setOnMouseExited(new BarChartScaleAnimation(chart, item, false));

			final Tooltip tooltip = new Tooltip("Range: " + item.getXValue().toString() + "\n" + "Frequency: " + item.getYValue().toString());
            tooltip.setFont(new Font("Calibri", 14));
            Tooltip.install(item.getNode(), tooltip);
		}

	}


	public void setType(int type, int timeGranularity) {
		this.type = type;

	}

	@Override
	public void filter(final List<Integer> bounceRate, final List<LocalDate> dates,
			final Map<Integer,Boolean> audience, final Map<Integer,Boolean> context) {

		int noOfThreads = Runtime.getRuntime().availableProcessors();
    	ExecutorService es = Executors.newFixedThreadPool(noOfThreads);
    	
    	Future<?> future = es.submit(() -> {

			LocalDate startDate = LocalDate.ofYearDay(1960, 1);
			LocalDate endDate = LocalDate.now();
	
			HashSet<Integer> selectedAudienceSet = new HashSet<>();
			for (Map.Entry<Integer, Boolean> entry : audience.entrySet()) {
			    if(entry.getValue().booleanValue()){
			    	selectedAudienceSet.add(entry.getKey());
			    }
			}
	
			HashSet<Integer> selectedContextSet = new HashSet<>();
			for (Map.Entry<Integer, Boolean> entry : context.entrySet()) {
			    if(entry.getValue().booleanValue()){
			    	selectedContextSet.add(entry.getKey());
			    }
			}
	
			if(dates != null){
				if(dates.get(0) != null && dates.get(1) != null){
					startDate = dates.get(0);
					endDate = dates.get(1);
					}
	
				 if(dates.get(0) != null && dates.get(1) == null){
					startDate = dates.get(0);
				}
	
				if(dates.get(0) == null && dates.get(1) != null){
					endDate = dates.get(1);
				}
			}
	
			ArrayList<ImpressionRecord> newImpressionsArray = mainApp.getModelController().filterImpressionLog(selectedContextSet, startDate, endDate);
			ArrayList<PersonRecord> newPeopleArray = mainApp.getModelController().filterPersonLog(selectedAudienceSet, newImpressionsArray);
			ArrayList<ImpressionRecord> finalImpress = mainApp.getModelController().filterFinalImpressionLog(newPeopleArray, newImpressionsArray);
	//		ArrayList<ServerRecord> newServerArray = mainApp.getModelController().filterServerLog(startDate, endDate, finalImpress);
			ArrayList<ClickRecord> newClickArray = mainApp.getModelController().filterClickLog(startDate, endDate, finalImpress);
	
	
			ImpressionLogWrapper imprWrapper = new ImpressionLogWrapper(finalImpress);
	//		PersonWrapper personWrapper = new PersonWrapper(newPeopleArray);
	//		ServerLogWrapper servWrapper = new ServerLogWrapper(newServerArray);
			ClickLogWrapper clickWrapper = new ClickLogWrapper(newClickArray);
	
			mainApp.getModelController().setFilterImpressionWrapper(imprWrapper);
			mainApp.getModelController().setFilterClickLogWrapper(clickWrapper);
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
			mainApp.getModelController().setFilterImpressionWrapper(mainApp.getModelController().getImpressionWrapper());
			mainApp.getModelController().setFilterClickLogWrapper(mainApp.getModelController().getClickLogWrapper());
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
