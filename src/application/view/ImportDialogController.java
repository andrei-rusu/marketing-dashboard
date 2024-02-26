package application.view;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import application.Main;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportDialogController {

	private final static String defaultLabelMessage = "[Chosen file]";

    private final static FileChooser.ExtensionFilter filterChooser
    	= new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");

    private final static String CHOSEN_FILE_FONT_STYLE = "-fx-font-weight: bold; -fx-font-style: normal";

    private final static String imprFileIdentifier = "impresF";
    private final static String clickFileIdentifier = "clickF";
    private final static String serverFileIdentifier = "serverF";

    // Reference to the main application
    private Main mainApp;

    // Reference to the current stage.
    private Stage importStage;

    private File impresFile;
    private File clickFile;
    private File serverFile;

    @FXML
    private Button importBut;
    @FXML
    private ImageView loading;
    @FXML
    private TextField nameField;
    @FXML
    private Label impresLabel;
    @FXML
    private Label clickLabel;
    @FXML
    private Label serverLabel;

    private File startChooserDirectory;


    private boolean impresLoaded = false;
    private boolean clickLoaded = false;
    private boolean servLoaded = false;

    private boolean importCorrect = false;

    private boolean haveStartLocation = false;



    public ImportDialogController() {

        //Set to the "Documents" directory path
    	String userDirectory = System.getProperty("user.home");
        startChooserDirectory = new File(userDirectory);

        // if the path specified can be found then the file chooser will open in this location
        if (startChooserDirectory.canRead()) {
        	haveStartLocation = true;
        }

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage importStage) {
    	this.importStage = importStage;
    }


    /**
     * Returns true if the user clicked Import, false otherwise.
     *
     * @return
     */
    public boolean isImportCorrect() {
        return importCorrect;
    }

    @FXML
    private void initialize() {

    	loading.setImage(new Image("file:resources/images/loading.gif"));
    	loading.setVisible(false);
    	importBut.setDisable(true);

    }


    @FXML
    private void handleImpres() {

    	FileChooser fileChooser = new FileChooser();

        // if the path specified can be found then the file chooser will open in this location
        if (haveStartLocation) {
        	fileChooser.setInitialDirectory(startChooserDirectory);
        }


        fileChooser.getExtensionFilters().add(filterChooser);

        // Show open file dialog
        impresFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());


        if (impresFile != null) {

            startChooserDirectory = impresFile.getParentFile();
            haveStartLocation = true;

        	impresLabel.setStyle(CHOSEN_FILE_FONT_STYLE);
        	impresLabel.setText(impresFile.getName());
        	impresLoaded = true;
        	notifyImportButtonState();
        }
        else {
        	impresLabel.setStyle(null);
        	impresLabel.setText(defaultLabelMessage);
        	impresLoaded = false;
        	notifyImportButtonState();
        }

    }


    @FXML
    private void handleClick() {

    	FileChooser fileChooser = new FileChooser();

        // if the initial path specified can be found or if a chooser was opened previously, the file chooser will open in the preset location
        if (haveStartLocation) {
        	fileChooser.setInitialDirectory(startChooserDirectory);
        }


        fileChooser.getExtensionFilters().add(filterChooser);

        // Show open file dialog
        clickFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());


        if (clickFile != null) {

            startChooserDirectory = clickFile.getParentFile();
            haveStartLocation = true;

        	clickLabel.setStyle(CHOSEN_FILE_FONT_STYLE);
        	clickLabel.setText(clickFile.getName());
        	clickLoaded = true;
        	notifyImportButtonState();
        }
        else {
        	clickLabel.setStyle(null);
        	clickLabel.setText(defaultLabelMessage);
        	clickLoaded = false;
        	notifyImportButtonState();
        }


    }


    @FXML
    private void handleServ() {

    	FileChooser fileChooser = new FileChooser();

        // if the path specified can be found then the file chooser will open in this location
        if (haveStartLocation) {
        	fileChooser.setInitialDirectory(startChooserDirectory);
        }


        fileChooser.getExtensionFilters().add(filterChooser);

        // Show open file dialog
        serverFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());


        if (serverFile != null) {

            startChooserDirectory = serverFile.getParentFile();
            haveStartLocation = true;

        	serverLabel.setStyle(CHOSEN_FILE_FONT_STYLE);
        	serverLabel.setText(serverFile.getName());
        	servLoaded = true;
        	notifyImportButtonState();
        }
        else {
        	serverLabel.setStyle(null);
        	serverLabel.setText(defaultLabelMessage);
        	servLoaded = false;
        	notifyImportButtonState();
        }


    }

    @FXML
    private void handleImport() {
    	
    	String nameText = nameField.getText().trim();

    	if (nameText.isEmpty() || nameText.length() > 20) {

    		Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(importStage);
            alert.setTitle("Invalid name selection");
            alert.setHeaderText("Campaign Name not set properly");
            alert.setContentText("Please provide a non-empty name of at most 20 characters for this loaded Campaign!");

            alert.showAndWait();
    	}
    	else {

    		importBut.setVisible(false);
    		loading.setVisible(true);

    	    Task<Boolean> task = new Task<Boolean>() {
    	        @Override
    	        public Boolean call() {
    	            // data retrieval
    	        	int noOfThreads = Runtime.getRuntime().availableProcessors();
    	        	ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

    	        	mainApp.getModelController().resetCalculations();

    	        	Future<Boolean> futureBool = es.submit(() -> {
	    	        	boolean isImprFileGood = mainApp.getModelController().parseFile(imprFileIdentifier,impresFile);
	            		boolean isClickFileGood = mainApp.getModelController().parseFile(clickFileIdentifier,clickFile);
	            		boolean isServerFileGood = mainApp.getModelController().parseFile(serverFileIdentifier,serverFile);

	            		return isImprFileGood && isClickFileGood && isServerFileGood;
    	        	});

    	        	es.shutdown();

            		try {
						return futureBool.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}

            		return false;
    	        }
    	    };

    	    task.setOnSucceeded(e -> {

    	        boolean goodFiles = task.getValue();

        	 	if (!goodFiles){

        	 		loading.setVisible(false);
        	 		importBut.setVisible(true);

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.initOwner(importStage);
                    alert.setTitle("Invalid files selection");
                    alert.setHeaderText("Selected CSV files had an incorrect format");
                    alert.setContentText("Please load the correct CSV files for this Marketing Campaign!");

                    alert.showAndWait();
            	} else{


            		mainApp.updateNewImportedData();
            		mainApp.delegateNewCampaignNameToMainPanelController(nameText);

            		importCorrect = true;
            		importStage.close();
            	}

        	 	task.cancel();
    	    });

    	    new Thread(task).start();
    	}

    }


    private void notifyImportButtonState() {

    	importBut.setDisable(!clickLoaded  || !servLoaded || !impresLoaded);
    }


}
