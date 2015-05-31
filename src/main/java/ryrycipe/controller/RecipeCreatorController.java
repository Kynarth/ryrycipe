package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.Component;
import ryrycipe.model.Faction;
import ryrycipe.model.Material;
import ryrycipe.model.Plan;
import ryrycipe.model.manager.FactionManager;
import ryrycipe.model.manager.MaterialManager;
import ryrycipe.model.manager.PlanManager;
import ryrycipe.model.view.MaterialView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the RecipeCreator view.
 */
public class RecipeCreatorController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeCreatorController.class.getName());

    /**
     * Contains all controls allowing the user to select materials in functions of parameters.
     */
    @FXML
    private GridPane materialFilter;

    /**
     * Display {@link Material}s from drilling.
     */
    @FXML
    private ToggleButton foragedBtn;

    /**
     * Display {@link Material}s from hunting.
     */
    @FXML
    private ToggleButton quarteredBtn;

    /**
     * Display {@link Material}s that can be used as selected component.
     */
    @FXML
    public ComboBox<Component> componentCB;

    /**
     * Display {@link Material}s with selected quality.
     */
    @FXML
    public ComboBox<String> qualityCB;

    /**
     * Display {@link Material}s belonging to the selected faction.
     */
    @FXML
    public ComboBox<Faction> factionCB;

    /**
     * Contains filtered {@link Material}s.
     */
    @FXML
    public FlowPane materialChooser;

    /**
     * Choose {@link Plan}'s quality.
     */
    @FXML
    private ComboBox<String> planQualityCB;

    /**
     * List of possible {@link Plan}.
     */
    @FXML
    private ComboBox<Plan> planCB;

    /**
     * Contain RecipeComponent for each {@link Plan}'s {@link Component}.
     */
    @FXML
    private VBox componentsContainer;

    // ObservableList for each combobox.
    private ObservableList<Plan> planItems = FXCollections.observableArrayList();
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();
    private ObservableList<Component> componentItems = FXCollections.observableArrayList();
    private ObservableList<String> qualityItems = FXCollections.observableArrayList();
    private ObservableList<Faction> factionItems = FXCollections.observableArrayList();

    private Plan currentPlan;  // Save the plan chose by the user.
    private ResourceBundle resources;
    private Ryrycipe mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        // Initialize the combobox containing the different plan's quality
        planQualityItems.addAll(resources.getString("combobox.quality.items").split(","));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() - 1));

        // Initialize the combobox containing the different plans
        initializePlansCB();
    }

    /**
     * Fill the {@link RecipeCreatorController#planCB} with {@link Plan} in function of selected quality from
     * {@link RecipeCreatorController#planQualityCB}.
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
     * Action performed when the user chooses a {@link Plan} from the associated combobox.
     */
    @FXML
    public void selectPlan() {
        componentsContainer.getChildren().clear();

        // Case when user change quality and reset the list of plans, the previous value of currentPlan is used
        if (planCB.getValue() != null)
            currentPlan = planCB.getValue();
        else
            planCB.setValue(currentPlan);

        // Fill the component container with each plan's component
        currentPlan.getComponents().forEach(this::addComponent);

        // Unlock and initialize material filter
        enableFilter();
        initializeFilter();

        LOGGER.info("User chose a plan.");
    }

    /**
     * Add a RecipeComponent to the {@link RecipeCreatorController#componentsContainer} containing all
     * the {@link Plan}'s components.
     *
     * @param component A {@link Component} from a {@link Plan}.
     */
    private void addComponent(Component component) {
        try {
            // Load the fxml file relative to the RecipeComponent
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipeComponent.fxml"));
            AnchorPane recipeComponent = loader.load();
            // Add an id corresponding to material name to retrieve it later.
            recipeComponent.setId(component.getName());

            // Add the loaded RecipeComponent view to the components container
            componentsContainer.getChildren().add(recipeComponent);

            // Set the RecipeComponent controller and setup the RecipeComponent
            RecipeComponentController controller = loader.getController();
            controller.setComponent(component);
            controller.setupRecipeComponent();

            // Store the node controller to use it later.
            recipeComponent.setUserData(controller);
        } catch (IOException | IllegalStateException e) {
            LOGGER.error("Unable to find the RecipeComponent fxml file");
        }
    }

    /**
     * Enable each controls from {@link RecipeCreatorController#materialFilter} after the user
     * has chosen a {@link Plan} in {@link RecipeCreatorController#planCB}.
     */
    private void enableFilter() {
        materialFilter.getChildren().forEach((control) -> control.setDisable(false));
    }

    /**
     * Fill {@link RecipeCreatorController#componentCB} with chosen {@link Plan}'s {@link Component}.
     */
    private void initializeComponentsCB() {
        componentItems.addAll(currentPlan.getComponents());
        componentCB.setItems(componentItems);
        componentCB.setValue(componentItems.get(0));
        componentCB.setOnAction(event -> displayMaterials());
    }

    /**
     * Fill {@link RecipeCreatorController#qualityCB} with the five possibilities.
     */
    private void initializeQualityCB() {
        qualityItems.clear();
        qualityItems.addAll(resources.getString("combobox.quality.values").split(","));
        qualityCB.setItems(qualityItems);
        qualityCB.setValue(qualityItems.get(0));

        qualityCB.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateFactionItems((Integer) oldValue, (Integer) newValue);
        });
    }

    /**
     * Fill {@link RecipeCreatorController#factionCB} in function of selected quality.
     */
    private void initializeFactionCB() {
        FactionManager factionManager = new FactionManager();
        factionItems.clear();
        // Indexes lower than two corresponds to basic and fin quality which does not allow faction.
        if (qualityCB.getSelectionModel().getSelectedIndex() < 2) {
            factionItems.add(factionManager.find(resources.getString("combobox.faction.generic")));
            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));
        } else {
            for (String factionName : resources.getString("combobox.faction.values").split(",")) {
                factionItems.add(factionManager.find(factionName));
            }

            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));
        }
        factionCB.setOnAction(event -> displayMaterials());
    }

    /**
     * Initialize all {@link RecipeCreatorController#materialFilter} controls.
     */
    private void initializeFilter() {
        initializeComponentsCB();
        initializeQualityCB();
        initializeFactionCB();

        LOGGER.info("Material filter initialized.");
    }

    /**
     * Update the {@link RecipeCreatorController#factionCB} in function of selected quality.
     *
     * @param oldIndex Index of the previous quality.
     * @param newIndex Index of the chosen quality item.
     */
    private void updateFactionItems(int oldIndex, int newIndex) {
        // Case where the user chooses a quality higher than 'Basic' and 'Fine' from them
        if (oldIndex < 2 && newIndex >= 2) {
            factionCB.setOnAction(null);
            factionItems.clear();
            factionCB.setOnAction(event -> displayMaterials());
            FactionManager factionManager = new FactionManager();
            for (String factionName : resources.getString("combobox.faction.values").split(",")) {
                factionItems.add(factionManager.find(factionName));
            }
            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));
        }
        // Case where the user chooses a quality lower than 'Choice' and higher from them
        else if (newIndex < 2 && oldIndex >= 2) {
            factionCB.setOnAction(null);
            factionItems.clear();
            factionCB.setOnAction(event -> displayMaterials());
            FactionManager factionManager = new FactionManager();
            factionItems.add(factionManager.find(resources.getString("combobox.faction.generic")));
            factionCB.setItems(factionItems);
            factionCB.setValue(factionItems.get(0));

        } else {
            // No change required, just updates quality of material in the materials chooser.
            displayMaterials();
        }
    }

    /**
     * Get all parameters from {@link RecipeCreatorController#materialFilter}.
     *
     * @return {@link Map} with the value of each parameter from the {@link RecipeCreatorController#materialFilter}.
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

        parameters.put("faction", factionCB.getValue().getName());
        parameters.put("quality", qualityCB.getValue());
        parameters.put("component", componentCB.getValue().getId());

        return parameters;
    }

    /**
     * Display {@link Material}s fitting the {@link RecipeCreatorController#materialFilter} options.
     */
    @FXML
    private void displayMaterials() {
        MaterialManager materialManager = new MaterialManager();
        materialChooser.getChildren().clear();

        for (Material material : materialManager.filter(getFilterParameters())) {
            MaterialView materialView = material.getImage();
            materialView.setController(this);
            materialChooser.getChildren().add(materialView);
        }

        LOGGER.info("Materials displayed.");
    }

    /**
     * Retrieve the RecipeComponent corresponding to the {@link MaterialView}.
     *
     * @param materialView {@link MaterialView}
     * @return RecipeComponent
     */
    public Node getRecipeComponent(MaterialView materialView) {
        for (Node node : componentsContainer.getChildren()) {
            if (materialView.getMaterial().getComponents().contains(node.getId())) {
                return node;
            }
        }

        return null;
    }

    /**
     * Add the double clicked {@link ryrycipe.model.view.MaterialView} in the correspondant RecipeComponent.
     *
     * @param materialView {@link MaterialView}
     */
    public void addMaterialToRecipe(MaterialView materialView) {
        Node node = getRecipeComponent(materialView);
        if (node != null) {
            // Get the number of materials to used via a dialog
            RecipeComponentController controller = (RecipeComponentController) node.getUserData();
            String nbMaterials = showMaterialNumberDialog(materialView, controller.getNeededMaterialNb());

            if (!nbMaterials.isEmpty()) {
                // Remove the event filter after that the material view get into RecipeComponent
                materialView.removeEventFilter(MouseEvent.MOUSE_CLICKED, materialView.getMouseEventEventHandler());
                controller.getMaterialsContainer().getChildren().add(0, materialView);
                controller.updateIndicator(nbMaterials);
            }

            LOGGER.info("{} has been added to the {} recipe's component",
                    materialView.getMaterial().getDescription(), controller.getComponentName().getText()
            );
        } else {
            LOGGER.error("Can't find the RecipeComponent for the double clicked material view");
        }
    }

    /**
     * Show the dialog allowing the user to select a number of materials.
     *
     * @param materialView {@link MaterialView}
     * @param amount Number of remaining {@link Material}s needed for a recipe component.
     * @return Number of {@link Material}s that the use chose to composed the recipe.
     */
    private String showMaterialNumberDialog(MaterialView materialView, int amount) {
        try {
            // Retrieve dialog's fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/MaterialNumberDialog.fxml"));
            loader.setResources(resources);
            AnchorPane dialogPane = loader.load();

            // Setup dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(resources.getString("dialog.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Get its controller
            MaterialNumberDialogController controller = loader.getController();
            controller.setMaterialImage(materialView);
            controller.setMaterialAmount(amount);
            controller.setDialogStage(dialogStage);
            controller.getNbMaterialField().requestFocus();

            dialogStage.showAndWait();

            return controller.getNbMaterialField().getText();
        } catch (IOException | IllegalStateException e) {
            LOGGER.error("Unable to find the MaterialNumberDialog fxml file");
            return "";
        }
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
