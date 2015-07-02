package ryrycipe.controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.LoggerFactory;
import ryrycipe.mediator.impl.MediateCreatorComponentCtrlers;
import ryrycipe.model.Component;
import ryrycipe.model.Faction;
import ryrycipe.model.Plan;
import ryrycipe.model.manager.FactionManager;
import ryrycipe.model.manager.PlanManager;
import ryrycipe.model.manager.view.MaterialView;
import ryrycipe.task.FilterMaterialsTask;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the CreatorPane view.
 * It manage the choice of the base plan for the user's recipe and the filtering of materials to include in it.
 * It also handle the user's comment for his plan.
 */
public class CreatorPaneController implements Initializable {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CreatorPaneController.class.getName());

    // --------------- Plan tab's controls -------------------- //
    /**
     * Plan tab of CreatorPane.
     */
    @FXML
    private TabPane planTab;

    /**
     * Allow the user to choose between normal, medium and high plan's quality.
     */
    @FXML
    private ComboBox<String> planQualityCB;

    /**
     * Display all type of plan like Spear, Vest, Pistol etc...
     */
    @FXML
    private ComboBox<Plan> planCB;

    /**
     * Area where each component from chosen {@link Plan} are displayed.
     */
    @FXML
    private VBox componentsContainer;

    /**
     * {@link TextArea} where the user can leave a comment for his recipe.
     */
    @FXML
    private TextArea recipeComment;

    // --------------- Materials tab's controls --------------- //

    /**
     * Contains controls defining parameters to filter materials
     */
    @FXML
    private GridPane materialFilter;

    /**
     * Filter materials following quality level (between 1 and 250).
     */
    @FXML
    private TextField matQualityLevel;

    /**
     * Filter materials following foraged category.
     */
    @FXML
    private ToggleButton foragedBtn;

    /**
     * Filter materials following quartered category.
     */
    @FXML
    private ToggleButton quarteredBtn;

    /**
     * Filter materials following component like blade, hammer, grip etc.
     */
    @FXML
    private ComboBox<Component> componentCB;

    /**
     * Filter materials following their quality (basic to supreme).
     */
    @FXML
    private ComboBox<String> qualityCB;

    /**
     * Filter materials following their faction (Fyros, Matis, etc...).
     */
    @FXML
    private ComboBox<Faction> factionCB;

    /**
     * Container where filtered materials are displayed.
     */
    @FXML
    private FlowPane materialChooser;

    // --------------- Controller's attributes --------------- //

    // ObservableList for each combobox.
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();
    private ObservableList<Plan> planItems = FXCollections.observableArrayList();
    private ObservableList<Component> componentItems = FXCollections.observableArrayList();
    private ObservableList<String> qualityItems = FXCollections.observableArrayList();
    private ObservableList<Faction> factionItems = FXCollections.observableArrayList();

    private Task<ObservableList<MaterialView>> filterTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup mediator between this controller and ComponentView ones.
        MediateCreatorComponentCtrlers.getInstance().registerCreatorPaneController(this);

        // Set all possible type of plan in planCB in function of selected plan's quality
        planQualityCB.valueProperty().addListener((observable1, oldValue, newValue) -> {
            planItems.clear();
            planItems.addAll(PlanManager.findAllPlans(planQualityCB.getValue()));
            // Sort plans to group them by category
            FXCollections.sort(planItems);
            planCB.setItems(planItems);

            // Disable all filter's parameters if no plan is selected in planCB
            for (Node node: materialFilter.getChildren()) {
                node.disableProperty().bind(planCB.valueProperty().isNull());
            }
        });

        // Set possible plan's quality in planQualityCB and set default value to 'High'
        planQualityItems.addAll((resources.getString("combobox.quality.items").split(",")));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() - 1));

        // Listener to force user to choose a number between 1 and 250 as material quality level
        matQualityLevel.textProperty().addListener(((observable, oldValue, newValue) -> {
            Pattern pattern = Pattern.compile("^[0-9]+$");

            if (!newValue.isEmpty()) {
                if (newValue.equals("0")) {
                    matQualityLevel.setText("");
                }

                // Check if the new entered value is a number. If not set the oldValue
                else if (!pattern.matcher(newValue).matches()) {
                    matQualityLevel.setText(oldValue);
                }

                // Check if the user enters a '0' before other numbers to remove it.
                else if (newValue.charAt(0) == '0' && newValue.length() > 1)
                    matQualityLevel.setText(newValue.substring(1));

                    // Check if the entered value does not exceed the max quality level: 250
                else if (!newValue.isEmpty() && Integer.valueOf(newValue) > 250) {
                    matQualityLevel.setText("250");
                }
            }
        }));

        // Listener to change component combobox items and components' container in function of chosen plan in planCB
        planCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            // components' container
            componentsContainer.getChildren().clear();
            for (Component component: newValue.getComponents()) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/ryrycipe/view/ComponentView.fxml"));
                    loader.setResources(resources);
                    AnchorPane componentView = loader.load();

                    // Bind width to its tab
                    componentView.prefWidthProperty().bind(planTab.widthProperty().subtract(40.0));

                    // Initialize the view
                    ComponentViewController controller = loader.getController();
                    controller.setupComponentView(component);
                    MediateCreatorComponentCtrlers.getInstance().addComponentViewController(controller);

                    componentsContainer.getChildren().add(componentView);
                } catch (IllegalStateException e) {
                    LOGGER.error("Couldn't find the ComponentView.fxml file.");
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }

            // Components' combobox
            componentItems.clear();
            componentItems.addAll(newValue.getComponents());
            componentCB.setItems(componentItems);
            componentCB.getSelectionModel().select(0);
            MediateCreatorComponentCtrlers.getInstance().selectComponentView(componentCB.getValue());
        });

        // Setup quality's items for its combobox
        qualityItems.addAll(resources.getString("combobox.quality.values").split(","));
        qualityCB.setItems(qualityItems);
        qualityCB.getSelectionModel().select(0);

        // Setup faction's items for its combobox
        factionItems.add(FactionManager.find(resources.getString("combobox.faction.generic")));
        factionCB.setItems(factionItems);
        factionCB.getSelectionModel().select(0);

        // Listener to change list of factions in function of selected quality
        qualityCB.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 2 && oldValue.intValue() >= 2) {
                // To avoid to trigger displayMaterials function while faction items are cleared
                factionCB.setOnAction(null);
                factionItems.clear();

                factionItems.add(FactionManager.find(resources.getString("combobox.faction.generic")));
                factionCB.setItems(factionItems);
                factionCB.setOnAction(event -> displayMaterials());
                factionCB.getSelectionModel().select(0);
            } else if (newValue.intValue() >= 2 && oldValue.intValue() < 2) {
                // To avoid to trigger displayMaterials function while faction items are cleared
                factionCB.setOnAction(null);
                factionItems.clear();

                for (String factionName : resources.getString("combobox.faction.values").split(",")) {
                    factionItems.add(FactionManager.find(factionName));
                }

                factionCB.setItems(factionItems);
                factionCB.setOnAction(event -> displayMaterials());
                factionCB.getSelectionModel().select(0);
            } else {
                displayMaterials();
            }
        });
    }

    /**
     * Get all parameters from the filter of materials
     *
     * @return {@link Map} with the value of each parameter from the filter of materials.
     */
    public Map<String, String> getFilterParameters() {
        Map<String, String> parameters = new HashMap<>();

        if (foragedBtn.isSelected())
            parameters.put("foraged", foragedBtn.getText());
        else
            parameters.put("foraged", null);

        if (quarteredBtn.isSelected())
            parameters.put("quartered", quarteredBtn.getText());
        else
            parameters.put("quartered", null);

        parameters.put("qualityLvl", matQualityLevel.getText());
        parameters.put("component", componentCB.getValue().getId());
        parameters.put("faction", factionCB.getValue().getName());
        parameters.put("quality", qualityCB.getValue());
        return parameters;
    }

    /**
     * Display {@link ryrycipe.model.Material}s fitting the materialFilter options.
     */
    @FXML
    public void displayMaterials() {
        // Remove previous MaterialView and get filter's parameters
        materialChooser.getChildren().clear();
        Map<String, String> filterParameter = getFilterParameters();

        // Check if the user has choose a material category
        if (filterParameter.get("foraged") == null && filterParameter.get("quartered") == null) {
            LOGGER.info("Materials not displayed: No category.");
            return;
        }

        // Cancel previous running filtering task
        if (filterTask != null && filterTask.isRunning()) {
            filterTask.cancel();
        }

        // Create new filtering task
        filterTask = new FilterMaterialsTask(materialFilter.getScene(), getFilterParameters());

        // Bind MaterialView from filtering task to materialChooser.
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ListProperty<MaterialView> materialViews = new SimpleListProperty<>();
        materialViews.bind(filterTask.valueProperty());
        materialViews.addListener((observable, oldValue, newValue) ->
            materialChooser.getChildren().add(newValue.get(newValue.size() - 1))
        );

        // launch filtering task
        Thread filterThread = new Thread(filterTask);
        filterThread.setDaemon(true);
        filterThread.start();
    }

    public ComboBox<Component> getComponentCB() {
        return componentCB;
    }
}
