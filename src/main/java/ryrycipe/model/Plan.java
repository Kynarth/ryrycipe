package ryrycipe.model;

import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a craft plan from Ryzom game.
 */
public class Plan implements Comparable<Plan> {

    private int id;
    private String name;
    private String quality;
    private String icon;
    private Category category;
    private List<Component> components;

    public Plan() {}

    public Plan(int id, String name, String quality, String icon, Category category, ArrayList<Component>components) {
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
    public int compareTo(Plan o) {
        return this.toString().compareTo(o.toString());
    }
}
