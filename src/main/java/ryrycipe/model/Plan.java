package ryrycipe.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

    /**
     * The plan furnish informations about how many {@link Material}s for each of its {@link Component} to create
     * his own recipes.
     */
    public Plan() {}

    /**
     * The plan furnish informations about how many {@link Material}s for each of its {@link Component} to create
     * his own recipes.
     *
     * @param id Plan's id in the database.
     * @param name Plan's name.
     * @param quality Plan's quality (Normal, Medium and High)
     * @param icon Plan's icon.
     * @param category Plan's {@link Category} (weapon, jewel, armor or ammo).
     * @param components {@link Component} composing the plan. (blade, grip, hammer etc ...)
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return Objects.equal(id, plan.id) &&
            Objects.equal(name, plan.name) &&
            Objects.equal(quality, plan.quality) &&
            Objects.equal(icon, plan.icon) &&
            Objects.equal(category, plan.category) &&
            Objects.equal(components, plan.components);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, quality, icon, category, components);
    }

    @Override
    public int compareTo(@NotNull Plan o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("quality", quality)
            .add("icon", icon)
            .add("category", category)
            .add("components", components)
            .toString();
    }
}
