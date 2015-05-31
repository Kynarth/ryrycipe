package ryrycipe.model.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.controller.MaterialNumberDialogController;
import ryrycipe.controller.RecipeComponentController;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.model.Material;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Aspect of a {@link ryrycipe.model.Material} in the application.
 */
public class MaterialView extends ImageView {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeCreatorController.class.getName());

    /**
     * Reference to {@link Ryrycipe}
     */
    private Ryrycipe mainApp;

    /**
     * MaterialView's {@link Material}
     */
    private Material material;

    /**
     * Number of {@link Material}s contained within it
     */
    private int nbMaterials;

    /**
     * Reference to {@link RecipeCreatorController} to perform actions on the MaterialView.
     */
    private RecipeCreatorController creatorController;

    /**
     * Reference to {@link RecipeComponentController} to perform actions on the MaterialView.
     */
    private RecipeComponentController RCController;

    /**
     * Event that ask to the user a the number of materials to use by a dialog when he double clicks it.
     */
    private EventHandler<MouseEvent> mouseEventAddMaterial = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() == 2){
                addToRecipe();
            }
        }
    });

    /**
     * Event that remove the MaterialView from the recipe to the pool of materials.
     */
    private EventHandler<MouseEvent> mouseEventRemoveMaterial = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() == 2){
                RCController.updateIndicator(-this.nbMaterials);
                this.nbMaterials = 0;
                RCController.getMaterialsContainer().getChildren().remove(this);
                creatorController.getMaterialChooser().getChildren().add(this);
                this.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventRemoveMaterial);
                this.addEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventAddMaterial);
            }
        }
    });

    /**
     * Constructor for an empty MaterialView.
     */
    public MaterialView() {
        super(new Image("/images/backgrounds/BK_empty.png"));
        this.nbMaterials = 0;
    }

    public MaterialView(WritableImage writableImage, Material material) {
        super(writableImage);
        this.material = material;
        this.nbMaterials = 0;

        // Handle mouse clicks action
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventAddMaterial);
    }
    /**
     * Add the double clicked {@link ryrycipe.model.view.MaterialView} in the correspondant RecipeComponent.
     */
    public void addToRecipe() {
        if (RCController != null) {
            // Get the number of materials to used via a dialog
            this.nbMaterials = Integer.parseInt(showMaterialNumberDialog(RCController.getNeededMaterialNb()));


            if (nbMaterials != 0) {
                // Remove the event filter after that the material view get into RecipeComponent
                // to replace with event filter to remove it by double click
                this.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventAddMaterial);
                this.addEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventRemoveMaterial);
                RCController.getMaterialsContainer().getChildren().add(0, this);
                RCController.updateIndicator(nbMaterials);
            }

            LOGGER.info("{} has been added to the {} recipe's component",
                this.getMaterial().getDescription(), RCController.getComponentName().getText()
            );
        } else {
            LOGGER.error("Can't find the RecipeComponent for the double clicked material view");
        }
    }

    /**
     * Show the dialog allowing the user to select a number of materials.
     *
     * @param amount Number of remaining {@link Material}s needed for a recipe component.
     * @return Number of {@link Material}s that the use chose to composed the recipe.
     */
    private String showMaterialNumberDialog(int amount) {
        try {
            // Retrieve dialog's fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/MaterialNumberDialog.fxml"));
            loader.setResources(ResourceBundle.getBundle("lang", mainApp.getLocale()));
            AnchorPane dialogPane = loader.load();

            // Setup dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(loader.getResources().getString("dialog.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Get its controller
            MaterialNumberDialogController controller = loader.getController();
            controller.setMaterialImage(this);
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



    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String toString() {
        return material.toString();
    }

    public void setCreatorController(RecipeCreatorController creatorController) {
        this.creatorController = creatorController;
    }

    public void setRCController(RecipeComponentController controller) {
        this.RCController = controller;
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
