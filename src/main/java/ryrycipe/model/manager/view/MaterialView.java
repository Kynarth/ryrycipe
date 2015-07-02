package ryrycipe.model.manager.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import ryrycipe.controller.MaterialNumberDialogController;
import ryrycipe.mediator.impl.MediateCreatorComponentCtrlers;
import ryrycipe.model.Material;
import ryrycipe.util.LanguageUtil;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Visualization of {@link Material} composed of {@link Material#icon}, {@link Material#matQualityLevel}
 * and when chose to be part of the user's recipe, number of them incorporated in it.
 */
public class MaterialView extends ImageView {

    private final static Logger LOGGER = LoggerFactory.getLogger(MaterialView.class.getName());

    private Material material;

    /**
     * Event that ask to the user a number of materials to use by a dialog when he double clicks it.
     */
    private EventHandler<MouseEvent> addMaterialMouseEvent = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            // Add the selected MaterialView in the corresponding ComponentView
            if (event.getClickCount() == 2) {
                // Show a dialog to make the user chooses an amount of materials to add in the ComponentView
                // Add this amount to the material object within MaterialView.
                try {
                    this.material.setNbMaterials(Integer.valueOf(showMaterialNumberDialog(
                        MediateCreatorComponentCtrlers.getInstance().getNeededAmountOfMaterials()
                    )));
                } catch (NumberFormatException e) {
                    this.material.setNbMaterials(0);
                }

                MediateCreatorComponentCtrlers.getInstance().addMaterial(this);
            }
        }
    });

    public MaterialView(Material material) {
        this.material = material;
    }

    /**
     * Constructor for an empty MaterialView.
     */
    public MaterialView() {
        super(new Image("/images/backgrounds/BK_empty.png"));
    }

    /**
     * MaterialView is a representation of a given material.
     *
     * @param image {@link Material#getImage()}.
     * @param material {@link Material}.
     */
    public MaterialView(Image image, Material material) {
        super(image);
        this.material = material;

        Tooltip tooltip = new Tooltip(this.material.getDescription());
        Tooltip.install(this, tooltip);

        // Set click listener
        this.setOnMouseClicked(addMaterialMouseEvent);
    }

    /**
     * Show the dialog allowing the user to select a number of materials.
     *
     * @param maxAmount Number of remaining {@link Material}s needed for a recipe component.
     * @return Number of {@link Material}s that the use chose to composed the recipe.
     */
    private String showMaterialNumberDialog(int maxAmount) {
        try {
            // Retrieve dialog's fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/MaterialNumberDialog.fxml"));
            loader.setResources(ResourceBundle.getBundle("lang", LanguageUtil.getLocale()));
            AnchorPane dialogPane = loader.load();

            // Setup dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(loader.getResources().getString("dialog.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.getScene().getWindow());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Setup controller
            MaterialNumberDialogController controller = loader.getController();
            controller.setMaterialImage(this.getImage());
            controller.setMaterialAmount(maxAmount);
            controller.setDialogStage(dialogStage);
            controller.getNbMaterialField().requestFocus();

            dialogStage.showAndWait();

            return controller.getNbMaterialField().getText();
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
            return "";
        }
    }

    /**
     * Write material informations like number and name of materials.
     */
    public void addMaterialInfos() {
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
        for (char digit: String.valueOf(this.material.getNbMaterials()).toCharArray()) {
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
}
