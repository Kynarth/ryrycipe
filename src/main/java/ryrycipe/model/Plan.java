package ryrycipe.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represent a craft plan from the game Ryzom.
 *
 * @see <a href="http://ryzom.com">Ryzom</a>
 */
public class Plan implements Comparable<Plan> {

    /**
     * Plan's database id.
     */
    private int id;

    /**
     * Plan's name.
     */
    private String name;

    /**
     * Plan's quality.
     */
    private String quality;

    /**
     * Plan's icon.
     */
    private String icon;

    /**
     * Plan's {@link Category}
     */
    private Category category;

    /**
     * Plan's {@link Component}s that defines the craft's plan recipe.
     */
    private List<Component> components;

    public Plan() {}

    public Plan(int id, String name, String quality, String icon, Category category, List<Component>components) {
        this.id = id;
        this.name = name;
        this.quality = quality;
        this.icon = icon;
        this.category = category;
        this.components = components;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String toString() {
        return category.getCategory() + "/" + name;
    }

    @Override
    public int compareTo(@NotNull Plan o) {
        return this.toString().compareTo(o.toString());
    }
}
