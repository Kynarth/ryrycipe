package ryrycipe.model.wrapper;

import ryrycipe.model.Component;
import ryrycipe.model.Material;

import java.util.Map;

/**
 * Helper class to wrap a {@link ryrycipe.controller.RecipeComponentController}.
 */
public class ComponentWrapper {

    /**
     * Name of the RecipeComponent.
     */
    private Component component;

    /**
     * List of materials within the RecipeComponent.
     */
    private Map<Material, Integer> materials;

    public ComponentWrapper() {}

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Map<Material, Integer> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }
}


