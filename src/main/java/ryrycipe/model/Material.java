package ryrycipe.model;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public String toString() {
        return name;
    }

    /**
     * A return a {@link List} of material's components name
     * @return {@link List} of material's components name
     */
    public List<String> getComponents() {
        return getAsComponent().stream().map(Component::getName).collect(Collectors.toList());
    }

    /**
     * Return an {@link Image} composed of {@link Faction#icon} and {@link Material#icon}.
     *
     * @return {@link Image}
     */
    public Image getImage() {
        Image overlay = new Image("/images/materials/" + icon);

        // Create a canvas that combines the background with the material icon as overlay
        Canvas canvas = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(this.faction.getImage(), 0, 0);
        graphicsContext.drawImage(overlay, 0, 0);

        // Write material's name on the image
        List<Image> nameLetters = new ArrayList<>();
        // Remove uppercase and accent from material name to get correspondance with typo images.
        char[] filterName = Normalizer.normalize(
            this.name.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""
        ).toCharArray();
        for (char letter: filterName) {
            nameLetters.add(new Image("/images/foregrounds/typo_" + letter + ".png"));
        }

        for (int i=0; i < nameLetters.size(); i++) {
            graphicsContext.drawImage(nameLetters.get(i), 3 + (i*5), 3);
        }

        return canvas.snapshot(new SnapshotParameters(), null);
    }
}
