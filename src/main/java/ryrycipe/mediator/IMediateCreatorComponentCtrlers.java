package ryrycipe.mediator;


import ryrycipe.controller.ComponentViewController;
import ryrycipe.controller.CreatorPaneController;
import ryrycipe.model.Component;
import ryrycipe.model.manager.view.MaterialView;

/**
 * Interface to link {@link CreatorPaneController} and {@link ComponentViewController}s.
 */
public interface IMediateCreatorComponentCtrlers {
    void registerCreatorPaneController(CreatorPaneController controller);
    void addComponentViewController(ComponentViewController controller);
    void updateComponentCB(Component component);
    void selectComponentView(Component component);
    ComponentViewController getComponentView(Component component);
    int getNeededAmountOfMaterials();
    void addMaterial(MaterialView materialView);
}
