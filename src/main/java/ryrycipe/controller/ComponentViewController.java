package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.mediator.impl.MediateCreatorComponentCtrlers;
import ryrycipe.model.Component;
import ryrycipe.model.manager.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for ComponentView fxml.
 */
public class ComponentViewController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(ComponentViewController.class.getName());

    /**
     * {@link AnchorPane} containing the plan's component.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * {@link Component} defining the ComponentView.
     */
    private Component component;

    /**
     * {@link ImageView} representing the component.
     */
    @FXML
    private ImageView componentIcon;

    /**
     * {@link Label} with name of the component.
     */
    @FXML
    private Label componentName;

    /**
     * {@link Label} indicating the number of {@link ryrycipe.model.Material} in the recipe for this component
     * and the number of {@link ryrycipe.model.Material} needed.
     *
     */
    @FXML
    private Label componentIndicator;

    /**
     * {@link FlowPane} with all {@link MaterialView} introduced in the recipe for this component.
     */
    @FXML
    private FlowPane materialsContainer;

    /**
     * Define if the ComponentView is currently selected or not.
     */
    private boolean selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set an ImageView in the flow layout that represents a empty slot for a material.
        materialsContainer.getChildren().add(new MaterialView());
    }

    /**
     * Initialize the ComponentView with informations from a {@link ryrycipe.model.Plan}'s {@link Component}.
     */
    public void setupComponentView(Component component) {
        // Initialize ComponentView with given component informations
        this.component = component;
        componentIcon.setImage(this.component.getImage());
        componentName.setText(this.component.getName());
        componentIndicator.setText("0/" + this.component.getAmount());

        LOGGER.info("Plan's component: {} initialized", this.component.getName());
    }

    /**
     * Update the {@link CreatorPaneController#componentCB} in function of clicked ComponentView.
     */
    @FXML
    public void clicked() {
        MediateCreatorComponentCtrlers.getInstance().updateComponentCB(component);
        MediateCreatorComponentCtrlers.getInstance().selectComponentView(component);
    }

    /**
     * Set effect of selection.
     */
    public void selected() {
        anchorPane.setStyle(
            "-fx-background-color: lightgrey;\n" +
            "-fx-border-color: grey;"
        );
        selected = true;
    }

    /**
     * Remove selection's effect.
     */
    public void unselected() {
        anchorPane.setStyle(
            "-fx-background-color: transparent;\n" +
            "-fx-border-color: null;"
        );

        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Component getComponent() {
        return component;
    }
}
