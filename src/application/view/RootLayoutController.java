package application.view;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
	
	@FXML
	private MenuItem export;
	
	// Reference to the main application
    private Main mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public void signalImportToRootLayout() {
    	export.setDisable(false);
    }
    
    /**
     * Opens import dialog
     */
    @FXML
    private void handleImport() {
        
    	mainApp.showImportDialog();
    }
    
    @FXML
    private void handleExport() {
    	
    	if (!mainApp.exportImageFromCurrentTab()) {
    		
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Export Failed");
            alert.setHeaderText("Something went wrong while trying to export this PNG!");
            alert.setContentText("Please check the chart you tried to export as a PNG again. Remember that a tab content can be exported only if a chart was selected for that specific tab.");

            alert.showAndWait();
    	}
    }
    
    /**
     * Opens import dialog
     */
    @FXML
    private void handleNewTab() {
        
    	mainApp.delegateNewTabActionToMainPanelController();
    }
    
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {

    	Platform.exit();
    	System.exit(0);
    }
    
    /**
     * Opens help contents dialog
     */
    @FXML
    private void handleHelpContents() {
    	mainApp.showHelpDialog();
    }
    
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Marketing Dashboard");
        alert.setHeaderText("Marketing Dashboard\nVersion: 1.1\nBuild: 2017-04-27");
        alert.setContentText("Marketing Dashboard is a tool that will allow you to effectively and efficiently analyse advertising campaigns. All you need to do is provide the log files delivered by your marketing agent, and you will be able to see the interactive graphs and charts that will show the audience and provide insight into how you can make your campaign more effective.");

        alert.show();
    	
    }
}
