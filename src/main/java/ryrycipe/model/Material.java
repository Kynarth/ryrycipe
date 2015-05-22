package ryrycipe.model;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import ryrycipe.util.LocaleUtil;
import ryrycipe.util.PropertiesUtil;

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
    private Faction faction;
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
     * Return an ImageView with the material image
     *
     * @return Material's image as an ImageView.
     */
    public ImageView getImage() {
        /**
         * Check if the material has faction because factions have the same name regardless of the language but not for
         * generic faction. So we have to use english word for generic to load correspoding background.
         */
//        Image background;
//        String generic = PropertiesUtil.loadProperties(
//            this.getClass().getClassLoader().getResource("lang_" + LocaleUtil.getLanguage() + ".properties").getPath()
//            ).getProperty("combobox.faction.generic");
//        if (this.faction.getName().equals(generic)) {
//            background = new Image("/images/backgrounds/BK_generic.png");
//        } else {
//            background = new Image("/images/backgrounds/BK_" + this.faction.getName().toLowerCase() + ".png");
//        }
        Image overlay = new Image("/images/materials/" + icon);

        // Create a canvas that combines the background with the overlay
        Canvas canvas = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(this.faction.getImage(), 0, 0);
        graphicsContext.drawImage(overlay, 0, 0);
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), null);
        return new ImageView(snapshot);
    }
}
