package ryrycipe.model;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.util.LanguageUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represent a material that are ingredient of the {@link Plan}'s recipe.
 */
public class Material {

    private final static Logger LOGGER = LoggerFactory.getLogger(Material.class.getName());

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
     * Material's quality level
     */
    private String matQualityLevel = "0";

    /**
     * Material's stats for specific {@link Component}.
     * String corresponding to {@link Component#id}
     */
    private Map<String, JsonObject> stats = new HashMap<>();

    /**
     * Number of Materials contained in a {@link ryrycipe.model.view.MaterialView}.
     */
    private int nbMaterials = 0;

    /**
     * A {@link List} of {@link Component}s defining in which {@link Component} a material can be used.
     */
    private List<Component> asComponent;

    /**
     * Ryzom's material like amber or fang that are base element to craft items.
     */
    public Material() {
    }

    /**
     * Base element of craft plan.
     *
     * @param id Material's id in the database.
     * @param description Little description providing infos like country of origin, quality and type.
     * @param category Define if the material has been extracted by drilling or quartering.
     * @param type Determine if the material belongs to wood, fiber ... type of material.
     * @param quality Material's quality like Basic, Fine, Choice etc ...
     * @param faction Inform about the material's faction like "Matis, Fyros etc ...
     * @param icon Material's icon.
     * @param name Material's name.
     * @param asComponent
     */
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
        this.stats = new HashMap<>();
    }

    /**
     * A return a {@link List} of material's components name
     *
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

        // Draw material's quality level if not null
        if (!this.matQualityLevel.equals("0")) {
            List<Image> numbers = new ArrayList<>();
            for (char digit: this.matQualityLevel.toCharArray()) {
                numbers.add(new Image("/images/foregrounds/Numbers_" + digit + ".png"));
            }

            for (int i=this.matQualityLevel.length() - 1; i >= 0; i--) {
                graphicsContext.drawImage(numbers.get((this.matQualityLevel.length() - 1) - i), 35 - (i*5), 31);
            }
        }

        // Draw material's name on the image
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

    /**
     * Retrieve material's stats in function of selected {@link Component} in
     * {@link ryrycipe.controller.CreatorPaneController#componentCB}
     *
     * @param componentCode {@link Component}'s id
     * @return {@link JsonObject} with all material's stats.
     * @throws IOException Problem with reading the json file containing materials' stats.
     */
    public JsonObject getStats(String componentCode) throws IOException {
        try {
            if (!this.stats.get(componentCode).isJsonNull()) {
                return this.stats.get(componentCode);
            } else {
                LOGGER.warn("Problem with loading stats for the material: {}", this.name);
                return (new JsonObject());
            }

        } catch (NullPointerException e) {
            // Get json file with materials' stats
            URL jsonUrl = Material.class.getClassLoader().getResource(
                "json/resource_stats_" + LanguageUtil.getLanguage() + ".json"
            );
            if (jsonUrl == null) {
                LOGGER.error("Could not find json file with all materials' stats.");
                return null;
            }

            JsonReader jsonReader = new JsonReader(new InputStreamReader(jsonUrl.openStream(), "UTF-8"));

            // Return JsonObject containing stats for the given material and component
            Gson gson = new Gson();
            Type mapOfMapsType = new TypeToken<Map<String, Map<String, Map<String, JsonObject>>>>() {}.getType();
            Map<String, Map<String, Map<String, JsonObject>>> map = gson.fromJson(jsonReader, mapOfMapsType);

            this.stats.put(componentCode, map.get(this.id).get("stats").get(componentCode));
            return map.get(this.id).get("stats").get(componentCode);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equal(nbMaterials, material.nbMaterials) &&
            Objects.equal(id, material.id) &&
            Objects.equal(description, material.description) &&
            Objects.equal(category, material.category) &&
            Objects.equal(type, material.type) &&
            Objects.equal(icon, material.icon) &&
            Objects.equal(name, material.name) &&
            Objects.equal(quality, material.quality) &&
            Objects.equal(faction, material.faction) &&
            Objects.equal(matQualityLevel, material.matQualityLevel) &&
            Objects.equal(stats, material.stats) &&
            Objects.equal(asComponent, material.asComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, description, category, type, icon, name, quality, faction, matQualityLevel, stats, nbMaterials, asComponent);
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

    public String getMatQualityLevel() {
        return matQualityLevel;
    }

    public void setMatQualityLevel(String matQualityLevel) {
        this.matQualityLevel = matQualityLevel;
    }

    public int getNbMaterials() {
        return nbMaterials;
    }

    public void setNbMaterials(int nbMaterials) {
        this.nbMaterials = nbMaterials;
    }
}
