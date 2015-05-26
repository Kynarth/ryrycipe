package ryrycipe.controller;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import ryrycipe.model.Component;
import ryrycipe.model.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for PlanRecipeComponent view.
 */
public class RecipeComponentController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set an ImageView in the flow layout that represents a empty slot for a material.
        materialsContainer.getChildren().add(new MaterialView());
        materialsContainer.getChildren().addListener((ListChangeListener<? super Node>) observable -> {
            if (observable.getList().size() > component.getAmount()) {
                materialsContainer.getChildren().remove(materialsContainer.getChildren().size() - 1);
                nbMaterials = materialsContainer.getChildren().size();
            }
        });
    }

    /**
     * Initialize the RecipeComponent with informations from a {@link ryrycipe.model.Plan}'s {@link Component}.
     */
    public void setupRecipeComponent() {
        // Initialize RecipeComponent with given component informations
        componentIcon.setImage(component.getImage());
        componentName.setText(component.getName());
        componentIndicator.setText("0/" + component.getAmount());
    }

    /**
     * Set the controller's {@link Component} with {@link ryrycipe.model.Plan}'s one
     *
     * @param component A {@link Component} composing a {@link ryrycipe.model.Plan}.
     */
    public void setComponent(Component component) {
        this.component = component;
    }

    /**
     * Update the displayed number of materials present in the user recipe.
     */
    public void updateIndicator() {
        componentIndicator.setText(nbMaterials + "/" + component.getAmount());
    }

    public FlowPane getMaterialsContainer() {
        return materialsContainer;
    }
}
