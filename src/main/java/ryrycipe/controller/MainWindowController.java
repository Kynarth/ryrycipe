package ryrycipe.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Manage MainWindow's controls.
 */
public class MainWindowController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void displayCreatorPane() {
        System.out.println("Creator pane");
    }

    @FXML
    private void displaySearchPane() {
        System.out.println("Search pane");
    }

    @FXML
    private void displayUploadPane() {
        System.out.println("Upload pane");
    }

    @FXML
    private void changeLanguage() {
        System.out.println("Change language");
    }
}
