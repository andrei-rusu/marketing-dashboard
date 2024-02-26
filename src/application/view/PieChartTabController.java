package application.view;

import java.text.DecimalFormat;
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
import application.model.ImpressionLogWrapper;
import application.model.ImpressionRecord;
import application.model.PersonRecord;
import application.model.PersonWrapper;
import application.util.PieChartScaleAnimation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class PieChartTabController implements TabController {
	private Main mainApp;

	@FXML
	private PieChart genderChart;

	@FXML
	private PieChart ageChart;

	@FXML
	private PieChart incomeChart;

	@FXML
	private PieChart contextChart;


	private ObservableList<PieChart.Data> genderData = FXCollections.observableArrayList();
	private ObservableList<PieChart.Data> ageData = FXCollections.observableArrayList();
	private ObservableList<PieChart.Data> incomeData = FXCollections.observableArrayList();
	private ObservableList<PieChart.Data> contextData = FXCollections.observableArrayList();

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
		genderData = genderChart.getData();
		ageData = ageChart.getData();
		incomeData = incomeChart.getData();
		contextData = contextChart.getData();
	}

	private void addPieChartAnimation(PieChart chart) {
		int total = 0;
		for(final PieChart.Data d : chart.getData())
			total += (int)d.getPieValue();
		for (final PieChart.Data d : chart.getData()) {
			d.getNode().setOnMouseEntered(new PieChartScaleAnimation(chart, d, true));
			d.getNode().setOnMouseExited(new PieChartScaleAnimation(chart, d, false));

			DecimalFormat twoD = new DecimalFormat("#.00");
			String text = d.getName() + ": " + ((int)d.getPieValue()) + "\n"
		                  + "Percentage: " + twoD.format(d.getPieValue()/total*100.0) + "%";
			final Tooltip tooltip = new Tooltip(text);
            tooltip.setFont(new Font("Calibri", 14));
            Tooltip.install(d.getNode(), tooltip);
		}
	}

	public void setData() {

		PersonWrapper personWrapper = mainApp.getModelController().getFilterPersonWrapper();
		ImpressionLogWrapper impresWrapper = mainApp.getModelController().getFilterImpressionWrapper();

		genderData.clear();
		genderData.add(new PieChart.Data("Male", personWrapper.filterGender(0)));
		genderData.add(new PieChart.Data("Female", personWrapper.filterGender(1)));

		ageData.clear();
		ageData.add(new PieChart.Data("<25", personWrapper.filterAge(2)));
		ageData.add(new PieChart.Data("25-34", personWrapper.filterAge(3)));
		ageData.add(new PieChart.Data("35-44", personWrapper.filterAge(4)));
		ageData.add(new PieChart.Data("45-54", personWrapper.filterAge(5)));
		ageData.add(new PieChart.Data(">54", personWrapper.filterAge(6)));

		incomeData.clear();
		incomeData.add(new PieChart.Data("Low", personWrapper.filterIncome(7)));
		incomeData.add(new PieChart.Data("Medium", personWrapper.filterIncome(8)));
		incomeData.add(new PieChart.Data("High", personWrapper.filterIncome(9)));

		contextData.clear();
		contextData.add(new PieChart.Data("News", impresWrapper.filterContext(0)));
		contextData.add(new PieChart.Data("Shopping", impresWrapper.filterContext(1)));
		contextData.add(new PieChart.Data("Social Media", impresWrapper.filterContext(2)));
		contextData.add(new PieChart.Data("Blog", impresWrapper.filterContext(3)));
		contextData.add(new PieChart.Data("Hobbies", impresWrapper.filterContext(4)));
		contextData.add(new PieChart.Data("Travel", impresWrapper.filterContext(5)));

		addPieChartAnimation(genderChart);
		addPieChartAnimation(ageChart);
		addPieChartAnimation(incomeChart);
		addPieChartAnimation(contextChart);
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


    		ImpressionLogWrapper imprWrapper = new ImpressionLogWrapper(finalImpress);
    		PersonWrapper personWrapper = new PersonWrapper(newPeopleArray);

    		mainApp.getModelController().setFilterImpressionWrapper(imprWrapper);
    		mainApp.getModelController().setFilterPersonWrapper(personWrapper);
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
	public void setType(int type, int granularity) {}

	@Override
	public void reset() {
		
		int noOfThreads = Runtime.getRuntime().availableProcessors();
    	ExecutorService es = Executors.newFixedThreadPool(noOfThreads);
    	
    	Future<?> future = es.submit(() -> {

			mainApp.getModelController().setFilterImpressionWrapper(mainApp.getModelController().getImpressionWrapper());
			mainApp.getModelController().setFilterPersonWrapper(mainApp.getModelController().getPersonWrapper());
    	});
    	
    	try {
			future.get();
	    	es.shutdown();
			setData();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	};

}
