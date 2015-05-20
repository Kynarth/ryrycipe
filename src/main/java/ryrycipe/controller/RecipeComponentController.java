package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import ryrycipe.model.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the PlanRecipeComponent.
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set an ImageView in the flow layout that represents a empty slot for a material.
        ImageView emptySlot = new ImageView(new Image("/images/backgrounds/BK_empty.png"));
        materialsContainer.getChildren().add(emptySlot);

    }

    public void setComponent(Component component) {
        this.component = component;

        // Initialize RecipeComponent with given component informations
        componentIcon.setImage(component.getImage());
        componentName.setText(component.getName());
        componentIndicator.setText("0/" + component.getAmount());
    }
}
