package ryrycipe.model;

import javafx.scene.image.Image;

/**
 * Represents a material's faction.
 */
public class Faction {

    private String name;
    private String icon;

    public Faction() {}

    public Faction(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Image getImage() {
        return new Image("/images/backgrounds/" + icon);
    }
}
