package ryrycipe.model;

import javafx.scene.image.Image;

/**
 * Represents the faction of a {@link Material}.
 *
 * @see Material#faction
 */
public class Faction {

    /**
     * Faction's name
     */
    private String name;

    /**
     * Icon representing the faction's color.
     */
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

    /**
     * Give an {@link Image} from {@link Faction#icon}
     *
     * @return An @{link Image} with a transparent color in function of the {@link Faction}.
     */
    public Image getImage() {
        return new Image("/images/backgrounds/" + icon);
    }
}
