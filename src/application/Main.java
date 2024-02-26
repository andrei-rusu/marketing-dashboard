package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import application.model.ModelController;
import application.view.ImportDialogController;
import application.view.MainPanelController;
import application.view.RootLayoutController;
import application.view.TabController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {

 	private Stage primaryStage;
    private BorderPane rootLayout;

    private boolean imported = false;

    private RootLayoutController rootController;
    private ModelController modelController;
    private MainPanelController mainPanelController;


    private HashMap<Integer, TabController> tabControllers;


    private File lastSaveLocation;


    public Main() {

    	modelController = new ModelController();
        tabControllers = new HashMap<>();
    }


    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        //this makes all stages close and the app exit when the main stage is closed
        this.primaryStage.setOnCloseRequest(e -> {

        	Platform.exit();
        	System.exit(0);
        });

        this.primaryStage.setTitle("Marketing Dashboard");
        this.primaryStage.setMinWidth(900);
        this.primaryStage.setMinHeight(670);

        this.primaryStage.getIcons().add(new Image("file:resources/images/chart.png"));

        initRootLayout();

        showMainPanel();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            rootController = loader.getController();
            rootController.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the Main Panel inside the root layout.
     */
    public void showMainPanel() {
        try {
            // Load Main Panel.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainPanel.fxml"));
            AnchorPane mainPanel = (AnchorPane) loader.load();

            // Set MainPanel into the center of root layout.
            rootLayout.setCenter(mainPanel);

            // Give the controller access to the main app.
            mainPanelController = loader.getController();
            mainPanelController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showImportDialog() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ImportDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image("file:resources/images/chart.png"));
            dialogStage.setTitle("Import Files");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller and pass the parser to it.
            ImportDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

            dialogStage.showAndWait();

            imported |= controller.isImportCorrect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showHelpDialog() {
    	
        Stage helpStage = new Stage();
        helpStage.getIcons().add(new Image("file:resources/images/chart.png"));
        helpStage.setTitle("Help User Guide");
        helpStage.initModality(Modality.WINDOW_MODAL);
        helpStage.initOwner(primaryStage);
        
    	
    	WebView browser = new WebView();
    	WebEngine webEngine = browser.getEngine();
    	
    	webEngine.load(new File("resources/user_guide/index.html").toURI().toString());

    	
        Scene scene = new Scene(browser);
        helpStage.setScene(scene);
        
        helpStage.show();
    }

    public boolean exportImageFromCurrentTab() {
    	return exportImageFromNode(mainPanelController.getCurrentTab().getContent());
    }

    public boolean exportImageFromNode(final Node node) {

    	// Node must not be null for the exporting to work.
    	if (node != null) {
			SnapshotParameters param = new SnapshotParameters();
			param.setDepthBuffer(true);
			WritableImage snapshot = node.snapshot(param, null);
			BufferedImage tempImg = SwingFXUtils.fromFXImage(snapshot, null);
	
			// Creating a file chooser for saving the image.
			FileChooser chooser = new FileChooser();
	
			if (lastSaveLocation == null) {
		        //Set to the "Documents" directory path
		    	String userDirectory = System.getProperty("user.home");
		        File startChooserDirectory = new File(userDirectory);
	
		        // if the path specified can be found then the file chooser will open in this location
		        if (startChooserDirectory.canRead()) {
		        	chooser.setInitialDirectory(startChooserDirectory);
		        }
			}
			else {
				chooser.setInitialDirectory(lastSaveLocation);
			}
	
			FileChooser.ExtensionFilter filterChooser
	    		= new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
			chooser.getExtensionFilters().add(filterChooser);
			chooser.setInitialFileName("*.png");
	
			File outputfile = chooser.showSaveDialog(this.primaryStage);
	
			if (outputfile != null) {
				lastSaveLocation = outputfile.getParentFile();
				try {
					ImageIO.write(tempImg, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			
			return true;
    	}
    	
    	// If the Node is null no Image can be exported
    	else {
    		return false;
    	}
    }


    public AnchorPane retrieveTabFormat(int tabIndex, int type, int granularity) {

    	FXMLLoader loader = new FXMLLoader();
    	AnchorPane tabContent = null;
    	TabController controller = null;

        try {
        	switch(type) {
            	case 0: {
            		loader.setLocation(Main.class.getResource("view/SummaryTab.fxml"));
            		break;
            	}
            	case 1:
            	case 2: {
            		loader.setLocation(Main.class.getResource("view/BarChartTab.fxml"));
            		break;
            	}
            	case 3: {
            		loader.setLocation(Main.class.getResource("view/PieChartTab.fxml"));
            		break;
            	}
            	default: {
            		loader.setLocation(Main.class.getResource("view/LineChartTab.fxml"));
            	}
        	}

    		tabContent = (AnchorPane) loader.load();
    		controller = (TabController) loader.getController();
    		controller.setMainApp(this);
    		controller.setType(type, granularity);

    		if (imported) {
    			controller.reset();

    			// Reset calls setData in all controllers except for the SummaryTabControllers
    			if (type == 0) {
    				controller.setData();
    			}
    		}

    		tabControllers.put(tabIndex, controller);

		} catch (IOException e) {
			e.printStackTrace();
		}

        return tabContent;
    }


    public void delegateFilterTabToTabController(int index,
    		List<Integer> bounceRate, List<LocalDate> dates, Map<Integer, Boolean> audience, Map<Integer, Boolean> context) {

    	tabControllers.get(index).filter(bounceRate, dates, audience, context);
    }

    public void delegateResetFilterTabToTabController(int index) {
    	tabControllers.get(index).reset();
    }



    public void updateNewImportedData() {

    	// Populate lists of metrics for Charts and the Calculations for the SummaryTab
    	modelController.populateMetricsLists();
    	modelController.populateCalculationsObject();

    	// Set data to all the tabs after new import.
    	for (TabController controller : tabControllers.values()) {
    		controller.setData();
    	}
    	
    	// Signaling new import to different GUI elements in RootLayout
    	rootController.signalImportToRootLayout();

    	// Signaling new import to different GUI elements in MainPanel
    	mainPanelController.signalImportToMainPanel();
    }



    public void delegateNewCampaignNameToMainPanelController(String name) {
    	mainPanelController.handleNewCampaignName(name);
    }

    public void delegateNewTabActionToMainPanelController() {
    	mainPanelController.handleNewTabRequest();
    }


    public boolean isImported() {
    	return imported;
    }


    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ModelController getModelController() {
    	return modelController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
