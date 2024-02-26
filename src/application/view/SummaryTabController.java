package application.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Main;
import application.model.Calculations;
import application.model.CalculationsManager;
import application.model.ClickLogWrapper;
import application.model.ClickRecord;
import application.model.ImpressionLogWrapper;
import application.model.ImpressionRecord;
import application.model.PersonRecord;
import application.model.PersonWrapper;
import application.model.ServerLogWrapper;
import application.model.ServerRecord;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SummaryTabController implements TabController {
	
	private final static String PLACEHOLDER = "Please upload the log files to see this content.";

	private Main mainApp;

    private ObservableList<Calculations> calculationsList = FXCollections.observableArrayList();
    private Calculations calculations;

    @FXML
    private TableView<Calculations> numberOfMetrics;

    @FXML
    private TableColumn<Calculations,Integer> impresCol;
    @FXML
    private TableColumn<Calculations,Integer> clickCol;
    @FXML
    private TableColumn<Calculations,Integer> uniqueCol;
    @FXML
    private TableColumn<Calculations,Integer> convCol;
    @FXML
    private TableColumn<Calculations,Integer> bounceCol;

    @FXML
    private TableView<Calculations> costs;

    @FXML
    private TableColumn<Calculations,Double> ctrCol;
    @FXML
    private TableColumn<Calculations,Double> cpaCol;
    @FXML
    private TableColumn<Calculations,Double> cpcCol;
    @FXML
    private TableColumn<Calculations,Double> cpmCol;
    @FXML
    private TableColumn<Calculations,Double> bounceRateCol;

    @FXML
    private TableView<Calculations> totalCosts;

    @FXML
    private TableColumn<Calculations,String> clickCostCol;
    @FXML
    private TableColumn<Calculations,String> impresCostCol;
    @FXML
    private TableColumn<Calculations,String> totalCostCol;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        Label placeholder1 = new Label();
        Label placeholder2 = new Label();
        Label placeholder3 = new Label();
        placeholder1.setText(PLACEHOLDER);
        placeholder2.setText(PLACEHOLDER);
        placeholder3.setText(PLACEHOLDER);
        numberOfMetrics.setPlaceholder(placeholder1);
        costs.setPlaceholder(placeholder2);
        totalCosts.setPlaceholder(placeholder3);


        // Add observable list data to the table
        numberOfMetrics.setItems(calculationsList);
        costs.setItems(calculationsList);
        totalCosts.setItems(calculationsList);

    }

    @Override
    public void setData() {

    	if (!calculationsList.isEmpty()) {
    		calculationsList.remove(0);
    	}

    	calculations = mainApp.getModelController().getNewCalculations();
    	calculationsList.add(calculations); // Item gets observed by the ObservableList

    }



    @FXML
    private void initialize() {

    	// Creating a locale instance for formatting metrics.
    	Locale locale  = new Locale("en", "UK");

    	// Default bounce registration.
		bounceCol.setText("Bounces: Time " + 5 + "s");

    	/*
    	 * Code setting listeners for the cells in the TableView for specific Calculations fields.
    	 */
    	impresCol.setCellValueFactory(cellData -> cellData.getValue().impressionsProperty().asObject());
        clickCol.setCellValueFactory(cellData -> cellData.getValue().clicksProperty().asObject());
        uniqueCol.setCellValueFactory(cellData -> cellData.getValue().uniquesProperty().asObject());
        convCol.setCellValueFactory(cellData -> cellData.getValue().conversionsProperty().asObject());
        bounceCol.setCellValueFactory(cellData -> cellData.getValue().bouncesProperty().asObject());
        ctrCol.setCellValueFactory(cellData -> cellData.getValue().CTRProperty().asObject());
        cpcCol.setCellValueFactory(cellData -> cellData.getValue().CPCProperty().asObject());
        cpaCol.setCellValueFactory(cellData -> cellData.getValue().CPAProperty().asObject());
        cpmCol.setCellValueFactory(cellData -> cellData.getValue().CPMProperty().asObject());
      	bounceRateCol.setCellValueFactory(cellData -> cellData.getValue().bounceRateProperty().asObject());
        impresCostCol.setCellValueFactory(cellData ->  Bindings.format(locale,"%.3f", cellData.getValue().impressionCostProperty().asObject()));
        clickCostCol.setCellValueFactory(cellData -> Bindings.format(locale,"%.3f", cellData.getValue().clickCostProperty().asObject()));
        totalCostCol.setCellValueFactory(cellData -> Bindings.format(locale,"%.3f", cellData.getValue().totalCostProperty().asObject()));
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
			ArrayList<ServerRecord> newServerArray = mainApp.getModelController().filterServerLog(startDate, endDate, finalImpress);
			ArrayList<ClickRecord> newClickArray = mainApp.getModelController().filterClickLog(startDate, endDate, finalImpress);
	
	
			ImpressionLogWrapper imprWrapper = new ImpressionLogWrapper(finalImpress);
			PersonWrapper personWrapper = new PersonWrapper(newPeopleArray);
			ServerLogWrapper servWrapper = new ServerLogWrapper(newServerArray);
			ClickLogWrapper clickWrapper = new ClickLogWrapper(newClickArray);
	
			mainApp.getModelController().setFilterImpressionWrapper(imprWrapper);
			mainApp.getModelController().setFilterPersonWrapper(personWrapper);
			mainApp.getModelController().setFilterClickLogWrapper(clickWrapper);
			mainApp.getModelController().setFilterSeverLogWrapper(servWrapper);
	
			int impressions = finalImpress.size();
			int clicks = newClickArray.size();
			mainApp.getModelController().getCalculationsManager().setImpressions(impressions);
			mainApp.getModelController().getCalculationsManager().setClicks(clicks);
			HashSet<Double> uniqueIds = mainApp.getModelController().getCalculationsManager().calculateUniques(clickWrapper);
			int conversions = mainApp.getModelController().getCalculationsManager().calculateConversions(servWrapper);
			double imprCost = mainApp.getModelController().getCalculationsManager().calculateImprCost(imprWrapper);
			double clickCost = mainApp.getModelController().getCalculationsManager().calculateClickCost(clickWrapper);
			double totalCost = mainApp.getModelController().getCalculationsManager().calcTotalCost();
			double CPA = mainApp.getModelController().getCalculationsManager().calcCPA();
			double CPC = mainApp.getModelController().getCalculationsManager().calcCPC();
			double CPM = mainApp.getModelController().getCalculationsManager().calcCPM();
			double CTR = mainApp.getModelController().getCalculationsManager().calcCTR();
	
	
			if (!calculationsList.isEmpty()) {
	
				if(clicks == 0){
					mainApp.getModelController().getCalculationsManager().setBounce(calculations, 0);
					mainApp.getModelController().getCalculationsManager().setBounceRate(calculations, 0);
				}else{
					if(bounceRate.get(0) == 0){
						mainApp.getModelController().getCalculationsManager().filterBounceTime(calculations, bounceRate.get(1));
						
						Platform.runLater(() -> {
							bounceCol.setText("Bounces: Time " + bounceRate.get(1) + "s");
						});
						
					}else {
						mainApp.getModelController().getCalculationsManager().filterBouncePages(calculations, bounceRate.get(1));
						
						Platform.runLater(() -> {
							bounceCol.setText("Bounces: Pages " + bounceRate.get(1));
						});
						
					}
				}
			}
	
			mainApp.getModelController().getCalculationsManager().setImpressions(calculations, impressions);
			mainApp.getModelController().getCalculationsManager().setClicks(calculations, clicks);
			mainApp.getModelController().getCalculationsManager().setUniques(calculations, uniqueIds.size());
			mainApp.getModelController().getCalculationsManager().setConversions(calculations, conversions);
			mainApp.getModelController().getCalculationsManager().setImpressionCost(calculations, imprCost);
			mainApp.getModelController().getCalculationsManager().setClickCost(calculations, clickCost);
			mainApp.getModelController().getCalculationsManager().setTotalCost(calculations, totalCost);
			mainApp.getModelController().getCalculationsManager().setCPA(calculations, CPA);
			mainApp.getModelController().getCalculationsManager().setCPC(calculations, CPC);
			mainApp.getModelController().getCalculationsManager().setCPM(calculations, CPM);
			mainApp.getModelController().getCalculationsManager().setCTR(calculations, CTR);
    	});
    	
    	try {
			future.get();
	    	es.shutdown();
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
			mainApp.getModelController().setFilterClickLogWrapper(mainApp.getModelController().getClickLogWrapper());
			mainApp.getModelController().setFilterSeverLogWrapper(mainApp.getModelController().getSeverLogWrapper());
	
			if(calculations != null){
	
				CalculationsManager calcManager = mainApp.getModelController().getCalculationsManager();
	
				calcManager.setImpressions( mainApp.getModelController().getFilterImpressionWrapper().getImpressions().size());
				calcManager.setClicks(mainApp.getModelController().getFilterClickLogWrapper().getClicks().size());
	
				calcManager.setImpressions(calculations, mainApp.getModelController().getFilterImpressionWrapper().getImpressions().size());
				calcManager.setClicks(calculations, mainApp.getModelController().getFilterClickLogWrapper().getClicks().size());
				calcManager.setUniques(calculations, calcManager.calculateUniques(mainApp.getModelController().getFilterClickLogWrapper()).size());
				calcManager.setConversions(calculations, calcManager.calculateConversions(mainApp.getModelController().getFilterSeverLogWrapper()));
				calcManager.setImpressionCost(calculations, calcManager.calculateImprCost(mainApp.getModelController().getFilterImpressionWrapper()));
				calcManager.setClickCost(calculations, calcManager.calculateClickCost(mainApp.getModelController().getFilterClickLogWrapper()));
				calcManager.setTotalCost(calculations, calcManager.calcTotalCost());
				calcManager.setCPA(calculations, calcManager.calcCPA());
				calcManager.setCPC(calculations, calcManager.calcCPC());
				calcManager.setCPM(calculations, calcManager.calcCPM());
				calcManager.setCTR(calculations, calcManager.calcCTR());
				calcManager.setDefaultBounceAndBounceRate(calculations);
	
				Platform.runLater(() -> {
					bounceCol.setText("Bounces: Time 5s");
				});
				
			}
    	});
    	
    	try {
			future.get();
	    	es.shutdown();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	};
}
