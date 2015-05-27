package ryrycipe.model.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.model.Material;

/**
 * Aspect of a {@link ryrycipe.model.Material} in the application.
 */
public class MaterialView extends ImageView {

    /**
     * MaterialView's {@link Material}
     */
    private Material material;

    /**
     * Reference to {@link RecipeCreatorController} to perform actions on the MaterialView.
     */
    private RecipeCreatorController controller;

    private EventHandler<MouseEvent> mouseEventEventHandler = (event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if(event.getClickCount() == 2){
                controller.addMaterialToRecipe(this);
            }
        }
    });

    /**
     * Constructor for an empty MaterialView.
     */
    public MaterialView() {
        super(new Image("/images/backgrounds/BK_empty.png"));
    }

    public MaterialView(WritableImage writableImage, Material material) {
        super(writableImage);
        this.material = material;

        // Handle mouse clicks action
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
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

    public void setController(RecipeCreatorController controller) {
        this.controller = controller;
    }

    public EventHandler<MouseEvent> getMouseEventEventHandler() {
        return mouseEventEventHandler;
    }
}
