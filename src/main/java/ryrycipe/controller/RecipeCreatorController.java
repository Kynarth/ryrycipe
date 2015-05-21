package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ryrycipe.model.Component;
import ryrycipe.model.Material;
import ryrycipe.model.Plan;
import ryrycipe.model.manager.MaterialManager;
import ryrycipe.model.manager.PlanManager;
import ryrycipe.model.representation.MaterialIcon;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the RecipeCreator.
 */
public class RecipeCreatorController implements Initializable {

    @FXML
    private GridPane materialFilter;

    @FXML
    private ToggleButton foragedBtn;

    @FXML
    private ToggleButton quarteredBtn;

    @FXML
    public ComboBox<Component> componentCB;

    @FXML
    public ComboBox<String> qualityCB;

    @FXML
    public ComboBox<String> factionCB;

    @FXML
    public FlowPane materialChooser;

    @FXML
    private ComboBox<String> planQualityCB;

    @FXML
    private ComboBox<Plan> planCB;

    @FXML
    private VBox componentsContainer;

    private ObservableList<Plan> planItems = FXCollections.observableArrayList();
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();
    private ObservableList<Component> componentItems = FXCollections.observableArrayList();
    private ObservableList<String> factionItems = FXCollections.observableArrayList();
    private ObservableList<String> qualityItems = FXCollections.observableArrayList();
    private Plan currentPlan;  // Save the plan chose by the user.
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        // Initialize the combobox containing the different plan's quality
        planQualityItems.addAll(resources.getString("combobox.quality.items").split(","));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() -1));

        // Initialize the combobox containing the different plans
        initializePlansCB();
    }

    /**
     * Fill the combobox of plans and update it when user changes quality.
     */
    @FXML
    public void initializePlansCB() {
        planItems.clear();
        PlanManager planManager = new PlanManager();
        planItems.addAll(planManager.getAll(planQualityCB.getValue()));
        FXCollections.sort(planItems);
        planCB.setItems(planItems);
    }

    /**
     * Action performed when the user choose a plan from the associated combobox.
     */
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

        // Unlock and initialize material filter
        enableFilter();
        initializeFilter();
    }

    /**
     * Add a RecipeComponent to the ScrollPane containing all the plan's recipe component.
     *
     * @param component A plan's Component.
     */
    private void addComponent(Component component) {
        try {
            // Load the fxml file relative to the RecipeComponent
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipeComponent.fxml"));
            AnchorPane recipeComponent = loader.load();

            // Add the loaded recipeComponent to the ScrollPane
            componentsContainer.getChildren().add(recipeComponent);

            // Set the recipeComponent controller and setup the RecipeComponent
            RecipeComponentController controller = loader.getController();
            controller.setComponent(component);
            controller.setupRecipeComponent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enable controls in the materials filter.
     */
    private void enableFilter() {
        materialFilter.getChildren().forEach((control) -> control.setDisable(false));
    }

    /**
     * Get all parameters from the materials filter.
     *
     * @return A map with the value of each parameters from the filter.
     */
    private Map<String, String> getFilterParameters() {
        Map<String, String> parameters = new HashMap<>();

        if (foragedBtn.isSelected())
            parameters.put("foraged", foragedBtn.getText());
        else
            parameters.put("foraged", null);

        if (quarteredBtn.isSelected())
            parameters.put("quartered", quarteredBtn.getText());
        else
            parameters.put("quartered", null);

        parameters.put("faction", factionCB.getValue());
        parameters.put("quality", qualityCB.getValue());
        parameters.put("component", componentCB.getValue().getId());

        return parameters;
    }

    /**
     * Fill the combobox of components.
     */
    private void initializeComponentsCB() {
        componentItems.addAll(currentPlan.getComponents());
        componentCB.setItems(componentItems);
        componentCB.setValue(componentItems.get(0));
    }

    /**
     * Fill the combobox of qualities.
     */
    private void initializeQualityCB() {
        qualityItems.clear();
        qualityItems.addAll(resources.getString("combobox.quality.values").split(","));
        qualityCB.setItems(qualityItems);
        qualityCB.setValue(qualityItems.get(0));
    }

    /**
     * Fill the combobox of factions.
     */
    private void initializeFactionCB() {
        factionItems.clear();
        // Indexes lower than two corresponds to basic and fin quality which does not allow faction.
        if (qualityCB.getSelectionModel().getSelectedIndex() < 2) {
            factionItems.add(resources.getString("combobox.faction.generic"));
            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));
        } else {
            factionItems.addAll(resources.getString("combobox.faction.values").split(","));
            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));
        }
    }

    /**
     * Initialize all filter's controls
     */
    private void initializeFilter() {
        initializeComponentsCB();
        initializeQualityCB();
        initializeFactionCB();
    }


    /**
     * Display materials fitting the materials filter options.
     */
    @FXML
    private void displayMaterials() {
        MaterialManager materialManager = new MaterialManager();

        for (Material material: materialManager.filter(getFilterParameters())) {
            materialChooser.getChildren().add(new MaterialIcon(material));
        }
    }
}
