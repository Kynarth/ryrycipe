package ryrycipe.model;

import ryrycipe.controller.RecipeComponentController;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Helper class to wrap a {@link ryrycipe.controller.RecipeComponentController}.
 */
public class ComponentWrapper {

    /**
     * Name of the RecipeComponent.
     */
    private RecipeComponentController component;

    /**
     * List of materials within the RecipeComponent.
     */
    private List<Material> materials;

    public ComponentWrapper() {}

    public ComponentWrapper(RecipeComponentController controller, List<Material> materials) {
        this.component = controller;
        this.materials = materials;
    }

    public RecipeComponentController getComponent() {
        return component;
    }

    public void setComponent(RecipeComponentController component) {
        this.component = component;
    }

    @XmlElement(name = "material")
    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}


