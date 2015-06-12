package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import java.util.*;
import java.util.regex.Pattern;

/**
 * Controller for the RecipeCreator view.
 */
public class RecipeCreatorController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeCreatorController.class.getName());

    /**
     * Contains all stats from a selected material in the filter.
     */
    @FXML
    private GridPane materialStatsContainer;

    /**
     * ImageView containing image from clicked MaterialView
     */
    @FXML
    private ImageView materialIcon;

    /**
     * Material's description of selected MaterialView
     */
    @FXML
    private Label materialDescription;

    /**
     */
    @FXML
    private TextArea recipeComment;

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
    private ComboBox<Component> componentCB;

    /**
     * Display {@link Material}s with selected quality.
     */
    @FXML
    private ComboBox<String> qualityCB;

    /**
     * Display {@link Material}s belonging to the selected faction.
     */
    @FXML
    private ComboBox<Faction> factionCB;

    /**
     * Contains filtered {@link Material}s.
     */
    @FXML
    private FlowPane materialChooser;

    /**
     * Choose which level of quality for filtered {@link Material}s.
     */
    @FXML
    private TextField matQualityLevel;

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

    /**
     * Save the plan chose by the user.
     */
    private Plan currentPlan;

    /**
     * Reference to {@link Ryrycipe}
     */
    private Ryrycipe mainApp;

    /**
     * {@link List} of {@link MaterialView} that have been added to the recipe.
     */
    private List<Material> usedMaterials = new ArrayList<>();

    /**
     * {@link ResourceBundle}
     */
    private ResourceBundle resources;

    /**
     * {@link RecipeComponentController} corresponding to the selected component from
     * {@link RecipeCreatorController#componentCB}.
     */
    private RecipeComponentController RCController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        matQualityLevel.textProperty().addListener(((observable, oldValue, newValue) -> {
            Pattern pattern = Pattern.compile("^[0-9]+$");

            if (!newValue.isEmpty()){
                if (newValue.equals("0")) {
                    matQualityLevel.setText("");
                    return; // to not enter following if statements
                }

                // Check if the new entered value is a number. If not set the oldValue
                if (!pattern.matcher(newValue).matches()) {
                    matQualityLevel.setText(oldValue);
                    return; // to not enter following if statements
                }

                // Check if the user enters a '0' before other numbers to remove it.
                if (newValue.charAt(0) == '0' && newValue.length() > 1)
                    matQualityLevel.setText(newValue.substring(1));

                // Check if the entered value does not exceed the max quality level: 250
                if (!newValue.isEmpty() && Integer.valueOf(newValue) > 250) {
                    matQualityLevel.setText("250");
                }
            }
        }));

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
    private void initializePlansCB() {
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
    private void selectPlan() {
        usedMaterials.clear();  // To free previous used materials for the new plan
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
            controller.setCreatorController(this);

            // Store the node controller to use it later.
            recipeComponent.setUserData(controller);
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
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
        componentCB.setOnAction(null); // Remove listener to not trigger displayMaterials while initializing
        componentItems.clear();
        componentItems.addAll(currentPlan.getComponents());
        componentCB.setItems(componentItems);
        componentCB.setValue(componentItems.get(0));

        // Set RCController
        componentsContainer.getChildren().stream().filter(
            node -> componentCB.getValue().toString().equals(node.getId())
        ).forEach(
            node -> RCController = (RecipeComponentController) node.getUserData()
        );

        componentCB.setOnAction(event -> updateComponent());
    }

    /**
     * Fill {@link RecipeCreatorController#qualityCB} with the five possibilities.
     */
    private void initializeQualityCB() {
        qualityCB.setOnAction(null); // Remove listener to not trigger displayMaterials while initializing
        qualityItems.clear();
        qualityItems.addAll(resources.getString("combobox.quality.values").split(","));
        qualityCB.setItems(qualityItems);
        qualityCB.setValue(qualityItems.get(0));

        qualityCB.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateFactionItems((Integer) oldValue, (Integer) newValue);
        });

        // Do not need to trigger displayMaterials method. We be done by the updateFactionItems one
    }

    /**
     * Fill {@link RecipeCreatorController#factionCB} in function of selected quality.
     */
    private void initializeFactionCB() {
        factionCB.setOnAction(null); // Remove listener to not trigger displayMaterials while initializing
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
     * Find the corresponding {@link RecipeComponentController} to the selected component from
     * {@link RecipeCreatorController#componentCB}.
     */
    @FXML
    private void updateComponent() {
        componentsContainer.getChildren().stream().filter(
            node -> componentCB.getValue().toString().equals(node.getId())
        ).forEach(
            node -> RCController = (RecipeComponentController) node.getUserData()
        );

        displayMaterials();

        if (RCController == null)
            LOGGER.error("Can't find the RecipeComponent for {}.", componentCB.getValue());
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
        materialChooser.getChildren().clear();
        MaterialManager materialManager = new MaterialManager();
        Map<String, String> filterParameter = getFilterParameters();

        // Check if the user has choose a material category
        if (filterParameter.get("foraged") == null && filterParameter.get("quartered") == null) {
            LOGGER.info("Materials not displayed: No category.");
            return;
        }

        for (Material material : materialManager.filter(filterParameter)) {
            // Check if the material has already been added to the plan
            if (this.usedMaterials.contains(material))
                continue;
            material.setMatQualityLevel(matQualityLevel.getText());
            MaterialView materialView = new MaterialView(material.getImage(), material);
            materialView.setRCController(RCController);
            materialView.setCreatorController(this);
            materialView.setMainApp(mainApp);
            materialChooser.getChildren().add(materialView);
        }

        LOGGER.info("Materials displayed.");
    }

    /**
     * Check if all RecipeComponent have enough to materials to fill requirements.
     *
     * @return True if each plan's RecipeComponent is completed else false
     */
    public boolean isPlanFilled() {
        if (!componentsContainer.getChildren().isEmpty()) {
            for (Node node : componentsContainer.getChildren()) {
                RecipeComponentController controller = (RecipeComponentController) node.getUserData();
                if (!controller.isFilled())
                    return false;
            }
        }

        return true;
    }

    /**
     * Displays material in function of entered level quality in {@link RecipeCreatorController#matQualityLevel}.
     *
     * @param event {@link KeyEvent}
     */
    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            displayMaterials();
        }
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }

    public List<Material> getUsedMaterials() {
        return usedMaterials;
    }

    public ComboBox<Plan> getPlanCB() {
        return planCB;
    }

    public GridPane getMaterialStatsContainer() {
        return materialStatsContainer;
    }

    public Label getMaterialDescription() {
        return materialDescription;
    }

    public ImageView getMaterialIcon() {
        return materialIcon;
    }

    public ComboBox<Component> getComponentCB() {
        return componentCB;
    }

    public ComboBox<String> getQualityCB() {
        return qualityCB;
    }

    public ComboBox<Faction> getFactionCB() {
        return factionCB;
    }

    public FlowPane getMaterialChooser() {
        return materialChooser;
    }

    public TextArea getRecipeComment() {
        return recipeComment;
    }

    public VBox getComponentsContainer() {
        return componentsContainer;
    }
}
