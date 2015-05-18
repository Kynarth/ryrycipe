package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import ryrycipe.util.PropertiesUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the RecipeCreator.
 */
public class RecipeCreatorController implements Initializable {

    @FXML
    private ComboBox<String> planQualityCB;

    @FXML
    private ComboBox<String> planCB;

    private ObservableList<String> planItems = FXCollections.observableArrayList();
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the combobox containing the different plan's quality
        planQualityItems.addAll(resources.getString("combobox.quality.items").split(","));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() -1));

        // Initialize the combobox containing the different plans

    }
}
