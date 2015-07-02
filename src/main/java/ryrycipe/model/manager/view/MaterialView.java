package ryrycipe.model.manager.view;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ryrycipe.model.Material;

/**
 * Visualization of {@link Material} composed of {@link Material#icon}, {@link Material#matQualityLevel}
 * and when chose to be part of the user's recipe, number of them incorporated in it.
 */
public class MaterialView extends ImageView {

    private Material material;

    /**
     * Event that ask to the user a number of materials to use by a dialog when he double clicks it.
     */
    private EventHandler<MouseEvent> addMaterialMouseEvent = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            // Add the selected MaterialView in the corresponding ComponentView
            if (event.getClickCount() == 2) {
                System.out.println("Added !");
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

    public Material getMaterial() {
        return material;
    }
}
