package ryrycipe.model;

import javafx.scene.image.Image;

/**
 * Component that compose {@link Plan}s
 *
 * @see Plan#components
 */
public class Component {

    /**
     * Component's database id.
     */
    private String id;

    /**
     * Component's name.
     */
    private String name;

    /**
     * Icon representing the component.
     */
    private String icon;

    /**
     * Amount of materaials needed for the component in the {@link Plan}.
     */
    private int amount;

    public Component() {
        this.name = null;
    }

    public Component(String id, String name, String icon, int amount) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String toString() {
        return name;
    }

    /**
     * Return an {@link Image} from {@link Component#icon}.
     *
     * @return {@link Image}
     */
    public Image getImage() {
        return new Image("/images/components/" + icon);
    }
}
