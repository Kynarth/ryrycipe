package ryrycipe.model;

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
import ryrycipe.util.LocaleUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
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
     * Number of Materials contained in a {@link ryrycipe.model.view.MaterialView}.
     */
    private int nbMaterials = 0;

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
     * {@link ryrycipe.controller.RecipeCreatorController#componentCB}
     *
     * @param componentCode {@link Component}'s id
     * @return {@link JsonObject} with all material's stats.
     * @throws IOException Problem with reading the json file containing materials' stats.
     */
    public JsonObject getStats(String componentCode) throws IOException {
        // Get json file with materials' stats
        URL jsonUrl = Material.class.getClassLoader().getResource(
            "json/resource_stats_" + LocaleUtil.getLanguage() + ".json"
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

        return map.get(this.id).get("stats").get(componentCode);
    }

    @Override
    public boolean equals(Object object) {
        if((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        Material material = (Material) object;

        return this.name.equals(material.name) &&
            this.faction.getName().equals(material.faction.getName()) &&
            this.quality.equals(material.quality);
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
