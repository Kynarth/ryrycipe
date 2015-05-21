package ryrycipe.model.representation;

import javafx.scene.image.ImageView;
import ryrycipe.model.Material;

/**
 * Appearance of Material object in the application.
 */
public class MaterialIcon extends ImageView {

    private Material material;

    public MaterialIcon(Material material) {
        super(material.getImage());
        this.material = material;
    }
}
