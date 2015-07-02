package ryrycipe.mediator;


import ryrycipe.controller.ComponentViewController;
import ryrycipe.controller.CreatorPaneController;
import ryrycipe.model.Component;

/**
 * Interface to link {@link CreatorPaneController} and {@link ComponentViewController}s.
 */
public interface IMediateCreatorComponentCtrlers {
    void registerCreatorPaneController(CreatorPaneController controller);
    void addComponentViewController(ComponentViewController controller);
    void updateComponentCB(Component component);
}
