package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Component;
import ryrycipe.model.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for PlanRecipeComponent view.
 */
public class RecipeComponentController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeComponentController.class.getName());

    @FXML
    private ImageView componentIcon;

    @FXML
    private Label componentName;

    @FXML
    private Label componentIndicator;

    @FXML
    private FlowPane materialsContainer;

    private Component component;
    private int nbMaterials = 0;
    private boolean filled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set an ImageView in the flow layout that represents a empty slot for a material.
        materialsContainer.getChildren().add(new MaterialView());
    }

    /**
     * Initialize the RecipeComponent with informations from a {@link ryrycipe.model.Plan}'s {@link Component}.
     */
    public void setupRecipeComponent() {
        // Initialize RecipeComponent with given component informations
        componentIcon.setImage(component.getImage());
        componentName.setText(component.getName());
        componentIndicator.setText("0/" + component.getAmount());

        LOGGER.info("Plan's component: {} initialized", component.getName());
    }

    /**
     * Update the displayed number of {@link ryrycipe.model.Material} present in the user recipe.
     *
     * @param nbMaterials Number of {@link ryrycipe.model.Material}s used for the recipe for corresponding component.
     */
    public void updateIndicator(int nbMaterials) {
        this.nbMaterials += nbMaterials;
        componentIndicator.setText(this.nbMaterials + "/" + component.getAmount());

        // Remove the empty MaterialView when the number of needed material is filled
        if (this.nbMaterials == component.getAmount()) {
            materialsContainer.getChildren().remove(materialsContainer.getChildren().size() - 1);
            filled = true;
            LOGGER.info("Recipe component filled.");
        }

        // Add empty MaterialView if RecipeComponent is no longer filled.
        if (this.nbMaterials < component.getAmount() && filled) {
            materialsContainer.getChildren().add(new MaterialView());
            filled = false;
            LOGGER.info("Recipe component is no longer filled.");
        }

        LOGGER.info("Component: {} updated", component.getName());
    }

    /**
     * Return the number of remaining of {@link ryrycipe.model.Material}s to fill the recipe component.
     *
     * @return int - Number of {@link ryrycipe.model.Material}s to fill the recipe component.
     */
    public int getNeededMaterialNb() {
        return component.getAmount() - nbMaterials;
    }

    /**
     * Set the controller's {@link Component} with {@link ryrycipe.model.Plan}'s one
     *
     * @param component A {@link Component} composing a {@link ryrycipe.model.Plan}.
     */
    public void setComponent(Component component) {
        this.component = component;
    }

    public FlowPane getMaterialsContainer() {
        return materialsContainer;
    }

    public Label getComponentName() {
        return componentName;
    }
}
