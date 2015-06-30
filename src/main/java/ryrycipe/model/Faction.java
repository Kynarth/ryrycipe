package ryrycipe.model;

import com.google.common.base.Objects;
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

    /**
     * <p>A faction represent one of the 5 country in the game:</p>
     * <ul>
     *     <li>Jungle: Zoraï</li>
     *     <li>Forest: Matis</li>
     *     <li>Desert: Fyros</li>
     *     <li>Lakes: Tryker</li>
     *     <li>Prime root</li>
     * </ul>
     */
    public Faction() {}

    /**
     * <p>A faction represent one of the 5 country in the game:</p>
     * <ul>
     *     <li>Jungle: Zoraï</li>
     *     <li>Forest: Matis</li>
     *     <li>Desert: Fyros</li>
     *     <li>Lakes: Tryker</li>
     *     <li>Prime root</li>
     * </ul>
     *
     * @param name Faction's name like Matis or Fyros.
     * @param icon Each faction is represented by a color: Green -> Matis, Red -> Fyros etc ...
     */
    public Faction(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faction faction = (Faction) o;
        return Objects.equal(name, faction.name) &&
            Objects.equal(icon, faction.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, icon);
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
