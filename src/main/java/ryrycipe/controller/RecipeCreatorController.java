package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ryrycipe.model.Component;
import ryrycipe.model.Plan;
import ryrycipe.model.manager.PlanManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the RecipeCreator.
 */
public class RecipeCreatorController implements Initializable {

    @FXML
    private GridPane materialFilter;

    @FXML
    private ComboBox<String> planQualityCB;

    @FXML
    private ComboBox<Plan> planCB;

    @FXML
    private VBox componentsContainer;

    private ObservableList<Plan> planItems = FXCollections.observableArrayList();
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();

    private Plan currentPlan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the combobox containing the different plan's quality
        planQualityItems.addAll(resources.getString("combobox.quality.items").split(","));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() -1));

        // Initialize the combobox containing the different plans
        initializePlanList();

    }

    @FXML
    public void initializePlanList() {
        planItems.clear();
        PlanManager planManager = new PlanManager();
        planItems.addAll(planManager.getAll(planQualityCB.getValue()));
        FXCollections.sort(planItems);
        planCB.setItems(planItems);
    }

    @FXML
    public void selectPlan() {
        // Initialize the list view with plan's information
        componentsContainer.getChildren().clear();

        // Case when user change quality and reset the list of plans, the previous value of currentPlan is used.
        if (planCB.getValue() != null)
            currentPlan = planCB.getValue();
        else
            planCB.setValue(currentPlan);

        currentPlan.getComponents().forEach(this::addComponent);
    }

    /**
     * Add a RecipeComponent to the ScrollPane containing all the plan's recipe component.
     *
     * @param component A plan's Component.
     */
    public void addComponent(Component component) {
        try {
            // Load the fxml file relative to the RecipeComponent
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipeComponent.fxml"));
            AnchorPane recipeComponent = loader.load();

            // Add the loaded recipeComponent to the ScrollPane
            componentsContainer.getChildren().add(recipeComponent);

            // Set the recipeComponent controller and setup the RecipeComponent
            RecipeComponentController controller = loader.getController();
            controller.setRecipeCreatorController(this);
            controller.setComponent(component);
            controller.setupRecipeComponent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enable controls in the materials filter.
     */
    public void enableFilter() {
        materialFilter.getChildren().forEach((control) -> control.setDisable(false));
    }
}
