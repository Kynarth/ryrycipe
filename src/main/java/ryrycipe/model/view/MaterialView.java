package ryrycipe.model.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import ryrycipe.model.Material;

/**
 * Aspect of a {@link ryrycipe.model.Material} in the application.
 */
public class MaterialView extends ImageView {

    private Material material;

    public MaterialView(WritableImage writableImage, Material material) {
        super(writableImage);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
