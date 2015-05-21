package ryrycipe.model;

import java.util.List;

/**
 * Represents a material.
 */
public class Material {

    private String id;
    private String description;
    private String category;
    private String type;
    private String icon;
    private String name;
    private String quality;
    private String faction;
    private List<Component> asComponent;

    public Material() {}

    public Material(String id, String description, String category, String type, String quality, String faction,
                    String icon, String name, List<Component> asComponent) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.type = type;
        this.icon = icon;
        this.name = name;
        this.quality = quality;
        this.faction = faction;
        this.asComponent = asComponent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Component> getAsComponent() {
        return asComponent;
    }

    public void setAsComponent(List<Component> asComponent) {
        this.asComponent = asComponent;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }
}
