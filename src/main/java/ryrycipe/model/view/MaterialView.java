package ryrycipe.model.view;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            // Add the selected MaterialView in the corresponding RecipeComponent
            if(event.getClickCount() == 2 && RCController.getNeededMaterialNb() > 0){
                addToRecipe();
            }
            // Show selected MaterialView stats in function of selected component from the filter in Material Stats tab.
            else if (event.getClickCount() == 1) {
                creatorController.materialStatsContainer.getChildren().clear();
                try {
                    JsonObject stats = this.material.getStats(creatorController.componentCB.getValue().getId());
                    int index = 0; // materialStatsContainer row index
                    for(Map.Entry<String, JsonElement> entry: stats.entrySet()) {
                        creatorController.materialStatsContainer.addRow(index,
                            new Label(entry.getKey()), new ProgressBar(entry.getValue().getAsDouble() / 100),
                            new Label(entry.getValue().getAsString())
                        );
                        index++;
                    }
                } catch (FileNotFoundException e) {
                    LOGGER.error("Could not find the json file with all materials' stats.");
                }
            }
        }
    });

    /**
     * Event that remove the MaterialView from the recipe to the pool of materials, update the
     * RecipeComponent indicator and delete written infos from MaterialView image.
     */
    private EventHandler<MouseEvent> mouseEventRemoveMaterial = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() == 2){
                // Update RecipeComponent's indicator
                RCController.updateIndicator(-this.nbMaterials);

                // Change eventFilter to be able to be added again
                RCController.getMaterialsContainer().getChildren().remove(this);
                creatorController.materialChooser.getChildren().add(this);
                this.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventRemoveMaterial);
                this.addEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseEventAddMaterial);

                // Return to Material View default value
                this.nbMaterials = 0;
                this.setImage(this.material.getImage());
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

    public MaterialView(Image image, Material material) {
        super(image);
        this.material = material;
        this.nbMaterials = 0;
        Tooltip tooltip = new Tooltip(this.material.getDescription());
        Tooltip.install(this, tooltip);

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
                addMaterialInfos();
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

    /**
     * Write material informations like number and name of materials.
     */
    private void addMaterialInfos() {
        // Image of multiply symbol that indicate the number of materials
        Image quantity = new Image("/images/foregrounds/W_quantity.png");

        // Create a canvas to draw all images composing the MaterialView's image
        Canvas canvas = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(this.getImage(), 0, 0);
        graphicsContext.drawImage(quantity, 3, 31);

        // Write the number on the image
        // Get an image of each digits composing the number of chosen materials
        List<Image> numbers = new ArrayList<>();
        for (char digit: String.valueOf(this.nbMaterials).toCharArray()) {
            numbers.add(new Image("/images/foregrounds/Numbers_" + digit + ".png"));
        }

        for (int i=0; i < numbers.size(); i++) {
            graphicsContext.drawImage(numbers.get(i), 9 + (i*5), 31);
        }
        // Write material's name on the image
        List<Image> nameLetters = new ArrayList<>();
        // Remove uppercase and accent from material name to get correspondance with typo images.
        char[] filterName = Normalizer.normalize(
            this.material.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""
        ).toCharArray();
        for (char letter: filterName) {
            nameLetters.add(new Image("/images/foregrounds/typo_" + letter + ".png"));
        }

        for (int i=0; i < nameLetters.size(); i++) {
            graphicsContext.drawImage(nameLetters.get(i), 3 + (i*5), 3);
        }

        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), null);
        this.setImage(snapshot);
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
