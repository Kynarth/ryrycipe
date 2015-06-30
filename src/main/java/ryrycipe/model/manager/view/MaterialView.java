package ryrycipe.model.manager.view;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryrycipe.model.Material;

/**
 * Visualization of {@link Material} composed of {@link Material#icon}, {@link Material#matQualityLevel}
 * and when chose to be part of the user's recipe, number of them incorporated in it.
 */
public class MaterialView extends ImageView {

    private Material material;

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
    }

    public Material getMaterial() {
        return material;
    }
}
