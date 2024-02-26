package application.view;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainPanelController {

	private final static int summaryTabFormat = 0, summaryTabIndex = 0;
	private final static int clickCostTabFormat = 1, clickCostTabIndex = 1;
	private final static int timeGranularityIgnoreValue = -1;
	private final static int chartsOverTimeFirstIndex = 4;

    // Reference to the main application
    private Main mainApp;

    @FXML
    private TabPane pane;
    
    @FXML
    private Button exportBtn;

    @FXML
    private ColorPicker colorpick;

    @FXML
    private ComboBox<String> fontSize;

    @FXML
    private TextField campaignName;

    @FXML
    private ComboBox<String> chartType;

    @FXML
    private ComboBox<String> timeGranularity;

    @FXML
    private Button displayButton;

    @FXML
    private Button applyFilt;

    @FXML
    private Button resetFilt;

    /*
     * Filtering options
     */

    // Bounces
    @FXML
    private RadioButton timeSpent;
    @FXML
    private RadioButton pagesVisited;
    @FXML
    private Slider timeSlider;
    @FXML
    private Slider pagesSlider;

    private ToggleGroup buttonGroup;

    // Date range
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    // Audience segments
    @FXML
    private CheckBox male;
    @FXML
    private CheckBox female;

    @FXML
    private CheckBox less25;
    @FXML
    private CheckBox range2534;
    @FXML
    private CheckBox range3544;
    @FXML
    private CheckBox range4554;
    @FXML
    private CheckBox great54;

    @FXML
    private CheckBox low;
    @FXML
    private CheckBox medium;
    @FXML
    private CheckBox high;

    // Impression context
    @FXML
    private CheckBox news;
    @FXML
    private CheckBox shop;
    @FXML
    private CheckBox travel;
    @FXML
    private CheckBox blog;
    @FXML
    private CheckBox hob;
    @FXML
    private CheckBox social;


    private boolean isTabNameCorrect = false;


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        populateTab(summaryTabIndex, summaryTabFormat, timeGranularityIgnoreValue);
        populateTab(clickCostTabIndex, clickCostTabFormat, timeGranularityIgnoreValue);

    }

    // Sets the name of the current campaign
    // Delegated by the Main application controller to MainPanelController
    public void handleNewCampaignName(String name) {

    	campaignName.getStyleClass().add("custom-text-field");
    	campaignName.setText(name);
    }

    public void handleNewTabRequest() {
    	handleNewTab();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    	/*
    	 * Code ensuring the combo boxes and the display charts button are available only when they are supposed to be.
    	 */
    	disableChartButtons(true);
    	disableFilterButtons(true);
    	exportBtn.setDisable(true);
    	timeGranularity.setDisable(true);

    	pane.getSelectionModel().selectedItemProperty().addListener( (observed, from, to) -> {

    		resetFilteringOptions();

            if (!mainApp.isImported() || to.getText() != null) {
            	disableChartButtons(true);
            	timeGranularity.setDisable(true);
            }
            else {
            	disableChartButtons(false);

            	if (chartType.getSelectionModel().getSelectedIndex() >= chartsOverTimeFirstIndex) {
            		timeGranularity.setDisable(false);
            	}
            }
            
        	checkCurrentTabContentForEnablingTabOptions();

    	});



    	// Entries for the Define Bounce radio buttons.
		buttonGroup = new ToggleGroup();

		timeSpent.setUserData(new Integer(0));
		timeSpent.setToggleGroup(buttonGroup);

		pagesVisited.setUserData(new Integer(1));
		pagesVisited.setToggleGroup(buttonGroup);




    	// Entries for the metrics Combo Box.
    	chartType.getItems().add("Summary");
    	chartType.getItems().add("Click Cost Distribution");
    	chartType.getItems().add("Impression Cost Distribution");
    	chartType.getItems().add("Demographics Pie Charts");
    	chartType.getItems().add("Impressions over Time");
    	chartType.getItems().add("Clicks over Time");
    	chartType.getItems().add("Uniques over Time");
    	chartType.getItems().add("Bounces over Time");
    	chartType.getItems().add("Conversions over Time");
    	chartType.getItems().add("Total Cost over Time");
    	chartType.getItems().add("CTR over Time");
    	chartType.getItems().add("CPA over Time");
    	chartType.getItems().add("CPC over Time");
    	chartType.getItems().add("CPM over Time");
    	chartType.getItems().add("Bounce Rate over Time");
    	chartType.getSelectionModel().select(0);



    	// Entries for the time granularity Combo Box.
    	timeGranularity.getItems().add("Hours");
    	timeGranularity.getItems().add("Days");
    	timeGranularity.getItems().add("Weeks");
    	timeGranularity.getSelectionModel().select(1);


    	chartType.setOnAction( (event) -> {
    		final Tooltip tooltip = new Tooltip(chartType.getSelectionModel().getSelectedItem());
            tooltip.setFont(new Font("Calibri", 14));
            Tooltip.install(chartType, tooltip);

        	if (chartType.getSelectionModel().getSelectedIndex() < chartsOverTimeFirstIndex) {
        		timeGranularity.setDisable(true);
        	}
        	else {
        		timeGranularity.setDisable(false);
        	}
    	});

    	timeGranularity.setOnAction( (event) -> {
        	Tooltip.install(timeGranularity, new Tooltip(timeGranularity.getSelectionModel().getSelectedItem()));
    	});



    	// Background colorpicker
    	colorpick.setOnAction( (event) -> {
    		pane.setStyle("-fx-background-color: #" + colorpick.getValue().toString().substring(2));
    	});


    	// Font size
    	fontSize.setButtonCell(new ListCell<String>() {
    	    @Override
    	    public void updateItem(String item, boolean empty) {
    	        super.updateItem(item, empty);
    	        if (item != null) {
    	            setText(item);
    	            setTextFill(Color.web("#626262"));
    	            setAlignment(Pos.CENTER);
    	            Insets old = getPadding();
    	            setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
    	        }
    	    }
    	});

    	for (int i = 10 ; i <= 30; i++) {
    		fontSize.getItems().add(i + "px");
    		fontSize.getSelectionModel().select(2);  // 12 px is selected by default
    	}
    	fontSize.setOnAction( (event) -> {
    		setFontSizeForTabs();
    	});


    	// TabPane
    	pane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

    }

    /*
     * The most important method in all the application!!!
     */
	@FXML
    private void handleDisplayChart() {

		resetFilteringOptions();

    	int chartTypeId = chartType.getSelectionModel().getSelectedIndex();
    	int timeGranularityId = timeGranularityIgnoreValue;

    	if (!timeGranularity.isDisabled()) {
    		timeGranularityId = timeGranularity.getSelectionModel().getSelectedIndex();
    	}

    	populateCurrentTab(chartTypeId, timeGranularityId);

    	checkCurrentTabContentForEnablingTabOptions();
    }

    @FXML
    private void handleApply() {

    	// Bounces
    	Integer selectedBounce = (Integer)buttonGroup.getSelectedToggle().getUserData();
    	Integer valueBounce = Integer.valueOf(0);

    	if (selectedBounce == 0) {
    		valueBounce = Integer.valueOf((int)timeSlider.getValue());
    	}
    	else {
    		valueBounce = Integer.valueOf((int)pagesSlider.getValue());
    	}

    	List<Integer> bounceRate = new ArrayList<>();
    	bounceRate.add(selectedBounce);
    	bounceRate.add(valueBounce);


    	// Date range
    	LocalDate startLocal = startDate.getValue();
    	LocalDate endLocal = endDate.getValue();

    	List<LocalDate> dates = new ArrayList<>();
    	dates.add(startLocal);
    	dates.add(endLocal);


    	// Audience segments
    	Map<Integer, Boolean> audience = new HashMap<>();

    	audience.put(0, male.isSelected());
    	audience.put(1, female.isSelected());
    	audience.put(2, less25.isSelected());
    	audience.put(3, range2534.isSelected());
    	audience.put(4, range3544.isSelected());
    	audience.put(5, range4554.isSelected());
    	audience.put(6, great54.isSelected());
    	audience.put(7, low.isSelected());
    	audience.put(8, medium.isSelected());
    	audience.put(9, high.isSelected());


    	// Impression context
    	Map<Integer, Boolean> context = new HashMap<>();

    	context.put(0, news.isSelected());
    	context.put(1, shop.isSelected());
       	context.put(2, social.isSelected());
    	context.put(3, blog.isSelected());
    	context.put(4, hob.isSelected());
    	context.put(5, travel.isSelected());


    	mainApp.delegateFilterTabToTabController(getCurrentTabIndex(), bounceRate, dates, audience, context);
    }

    @FXML
    private void handleReset() {
    	mainApp.delegateResetFilterTabToTabController(getCurrentTabIndex());
    	resetFilteringOptions();
    }

    @FXML
    private void handleImport() {
    	mainApp.showImportDialog();
    }

    @FXML
    private void handleExport() {

    	Node nodeToBePrinted = getCurrentTab().getContent();
    	mainApp.exportImageFromNode(nodeToBePrinted);
    }

    @FXML
    private void handleNewTab() {

    	final Tab tab = createEditableTab("New Chart");

    	List<Tab> tabs = pane.getTabs();
        tabs.add(tabs.size()-1,tab);

        pane.getSelectionModel().select(tab);
    }

    /*
     * Method to create a new editable tab.
     */
	private Tab createEditableTab(String text) {

		final Label label = new Label(text);
		label.getStyleClass().remove("label");
		final Tab tab = new Tab();
		tab.setGraphic(label);
		final TextField textField = new TextField();

		label.setOnMouseClicked((event) -> {
			if (event.getClickCount() == 2) {
				textField.setText(label.getText());
				tab.setGraphic(textField);
				textField.selectAll();
				textField.requestFocus();
			}
		});

		textField.setOnAction((event) -> {

			String titleText = textField.getText().trim();

			if (titleText.isEmpty() || titleText.length() >  30) {
	    		Alert alert = new Alert(AlertType.WARNING);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("Wrong sized title");
	            alert.setHeaderText("The tab name is either too short or too long for this tab");
	            alert.setContentText("Please provide a non-empty name of at most 10 characters for this tab!");

				isTabNameCorrect = false;

	            alert.showAndWait();
			}
			else {
				label.setText(titleText);
				isTabNameCorrect = true;
			}

			tab.setGraphic(label);
		});

		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {

			if (!newValue && isTabNameCorrect) {
				label.setText(textField.getText());
				tab.setGraphic(label);
			}
		});

    	return tab ;
    }

    private void setFontSizeForTabs() {

		for (Tab tab : pane.getTabs()) {
			if (tab.getId() != null && !tab.getId().equals(String.valueOf(summaryTabFormat)))
				tab.getContent().setStyle("-fx-font-size: " + fontSize.getValue());
		}
    }
    
    // Needed outside to enable the chart and filtering buttons after import is successful
    // Filtering options are also reset after correct import.
    public void signalImportToMainPanel() {
    	
    	resetFilteringOptions();
    	
    	if (getCurrentTab().getId() == null) {
    		disableChartButtons(false);
    	}
    	
    	checkCurrentTabContentForEnablingTabOptions();
    	
    }
    
    private void checkCurrentTabContentForEnablingTabOptions() {
    	
    	if (getCurrentTab().getContent() != null) {
        	exportBtn.setDisable(false);
        	disableFilterButtons(false);
    	}
    	else {
        	exportBtn.setDisable(true);
        	disableFilterButtons(true);
    	}
    }
    

    private void disableChartButtons(boolean disable) {
    	chartType.setDisable(disable);
    	displayButton.setDisable(disable);
    }

    private void disableFilterButtons(boolean disable) {
    	applyFilt.setDisable(disable);
    	resetFilt.setDisable(disable);
    }


    public Tab getCurrentTab() {
    	return pane.getSelectionModel().getSelectedItem();
    }


    private int getCurrentTabIndex() {
    	return pane.getSelectionModel().getSelectedIndex();
    }


    private Tab getTab(int index) {
    	return pane.getTabs().get(index);
    }

    private void populateTab(int index, int type, int granularity) {
    	AnchorPane tabContent = mainApp.retrieveTabFormat(index, type, granularity);
    	getTab(index).setContent(tabContent);

    	getTab(index).setId(String.valueOf(type));
		setFontSizeForTabs();
    }

    private void populateCurrentTab(int type, int granularity) {
    	populateTab (getCurrentTabIndex(), type, granularity);
    }

    // Put all filtering options back to their default setting.
    private void resetFilteringOptions() {

    	buttonGroup.selectToggle(timeSpent);
    	timeSlider.adjustValue(5);
    	pagesSlider.adjustValue(1);

    	startDate.setValue(null);
    	endDate.setValue(null);

    	male.setSelected(true);
    	female.setSelected(true);

    	less25.setSelected(true);
    	range2534.setSelected(true);
    	range3544.setSelected(true);
    	range4554.setSelected(true);
    	great54.setSelected(true);

    	low.setSelected(true);
    	medium.setSelected(true);
    	high.setSelected(true);

    	news.setSelected(true);
    	shop.setSelected(true);
    	travel.setSelected(true);
    	blog.setSelected(true);
    	hob.setSelected(true);
    	social.setSelected(true);

    }


}
