package ryrycipe.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ryrycipe.model.Plan;
import ryrycipe.model.manager.PlanManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the CreatorPane view.
 * It manage the choice of the base plan for the user's recipe and the filtering of materials to include in it.
 * It also handle the user's comment for his plan.
 */
public class CreatorPaneController implements Initializable {

    // --------------- Plan tab's controls -------------------- //

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
    private ComboBox componentCB;

    /**
     * Filter materials following their quality (basic to supreme).
     */
    @FXML
    private ComboBox qualityCB;

    /**
     * Filter materials following their faction (Fyros, Matis, etc...).
     */
    @FXML
    private ComboBox factionCB;

    /**
     * Container where filtered materials are displayed.
     */
    @FXML
    private FlowPane materialChooser;

    // --------------- Controller's attributes --------------- //

    // ObservableList for each combobox.
    private ObservableList<String> planQualityItems = FXCollections.observableArrayList();
    private ObservableList<Plan> planItems = FXCollections.observableArrayList();

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        // Set possible plan's quality in planQualityCB and set default value to 'High'
        planQualityItems.addAll((resources.getString("combobox.quality.items").split(",")));
        planQualityCB.setItems(planQualityItems);
        planQualityCB.setValue(planQualityItems.get(planQualityItems.size() - 1));

        // Set all possible type of plan in planCB
        PlanManager planManager = new PlanManager();
        planItems.addAll(planManager.findAllPlans(planQualityCB.getValue()));
        FXCollections.sort(planItems);
        planCB.setItems(planItems);

        // Disable all filter's parameters if no plan is selected in planCB
        for (Node node: materialFilter.getChildren()) {
            node.disableProperty().bind(planCB.valueProperty().isNull());
        }
    }
}
