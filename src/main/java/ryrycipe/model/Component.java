package ryrycipe.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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
     * Id of Component's {@link Plan}.
     * {@link Plan#id}.
     */
    private int recipeId;

    /**
     * Component's name.
     */
    private String name;

    /**
     * Icon representing the component.
     */
    private String icon;

    /**
     * Amount of materials needed for the component in the {@link Plan}.
     */
    private int amount;

    /**
     * A component is base element composing a {@link Plan}.
     * It can be filled with different kind of {@link Material} in function of his nature.
     */
    public Component() {
    }

    /**
     * Component not affiliated to a plan.
     *
     * @param id Component's id in the database.
     * @param name Component's name.
     * @param icon Component's icon.
     */
    public Component(String id, String name, String icon) {
        this.icon = icon;
        this.name = name;
        this.id = id;
    }

    /**
     * Component affiliated to a plan.
     *
     * @param id Component's id in the database.
     * @param recipeId Plan's id in the database.
     * @param name Component's name.
     * @param icon Component's icon.
     * @param amount Component's amount to fill it (depends of the chosen plan.).
     */
    public Component(String id, int recipeId, String name, String icon, int amount) {
        this.id = id;
        this.recipeId = recipeId;
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

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
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

    /**
     * Return an {@link Image} from {@link Component#icon}.
     *
     * @return {@link Image}
     */
    public Image getImage() {
        return new Image("/images/components/" + icon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equal(amount, component.amount) &&
            Objects.equal(id, component.id) &&
            Objects.equal(name, component.name) &&
            Objects.equal(icon, component.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, icon, amount);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("recipeId", recipeId)
            .add("name", name)
            .add("icon", icon)
            .add("amount", amount)
            .toString();
    }
}
