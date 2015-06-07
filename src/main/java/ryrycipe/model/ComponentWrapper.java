package ryrycipe.model;

import java.util.List;

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
    private List<Material> materials;

    public ComponentWrapper() {}

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}


