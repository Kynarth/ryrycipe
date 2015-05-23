package ryrycipe.model;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.util.List;

/**
 * Represent a material that are ingredient of the {@link Plan}'s recipe.
 */
public class Material {

    /**
     * Material's database id.
     */
    private String id;

    /**
     * A long description composes of all material informations.
     */
    private String description;

    /**
     * First level of classification.
     */
    private String category;

    /**
     * Second level of classification.
     */
    private String type;

    /**
     * Icon representing the material.
     */
    private String icon;

    /**
     * Material's name
     */
    private String name;

    /**
     * Material's quality
     */
    private String quality;

    /**
     * Material's faction.
     */
    private Faction faction;

    /**
     * A {@link List} of {@link Component}s defining in which {@link Component} a material can be used.
     */
    private List<Component> asComponent;

    public Material() {}

    public Material(String id, String description, String category, String type, String quality, Faction faction,
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

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    /**
     * Return an {@link ImageView} composed of {@link Faction#icon} and {@link Material#icon}.
     *
     * @return {@link ImageView}..
     */
    public ImageView getImage() {
        Image overlay = new Image("/images/materials/" + icon);

        // Create a canvas that combines the background with the material icon as overlay
        Canvas canvas = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(this.faction.getImage(), 0, 0);
        graphicsContext.drawImage(overlay, 0, 0);
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), null);
        return new ImageView(snapshot);
    }
}
