package ryrycipe.model;

import javafx.scene.image.Image;

/**
 * A plan component
 */
public class Component {

    private String id;
    private String name;
    private String icon;
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
     * Return the component image
     *
     * @return Component's image.
     */
    public Image getImage() {
        return new Image("/images/components/" + icon);
    }
}
