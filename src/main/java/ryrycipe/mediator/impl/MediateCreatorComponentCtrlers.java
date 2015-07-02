package ryrycipe.mediator.impl;

import ryrycipe.controller.ComponentViewController;
import ryrycipe.controller.CreatorPaneController;
import ryrycipe.mediator.IMediateCreatorComponentCtrlers;
import ryrycipe.model.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class making the link between {@link CreatorPaneController} and {@link ComponentViewController}s.
 */
public class MediateCreatorComponentCtrlers implements IMediateCreatorComponentCtrlers {
    private static MediateCreatorComponentCtrlers ourInstance = new MediateCreatorComponentCtrlers();

    private CreatorPaneController creatorPaneController;
    private List<ComponentViewController> componentViewControllerList = new ArrayList<>();

    public static MediateCreatorComponentCtrlers getInstance() {
        return ourInstance;
    }

    private MediateCreatorComponentCtrlers() {
    }

    @Override
    public void registerCreatorPaneController(CreatorPaneController controller) {
        creatorPaneController = controller;
    }

    @Override
    public void addComponentViewController(ComponentViewController controller) {
        componentViewControllerList.add(controller);
    }

    /**
     * Update {@link CreatorPaneController#componentCB} with {@link Component} from clicked
     * {@link ComponentViewController}.
     *
     * @param component {@link Component} of {@link ComponentViewController}.
     */
    @Override
    public void updateComponentCB(Component component) {
        creatorPaneController.getComponentCB().getSelectionModel().select(component);
        creatorPaneController.displayMaterials();
    }

    /**
     * Set an effect of selection on the owner of the given {@link Component}.
     *
     * @param component {@link Component} from clicked {@link ComponentViewController} selected in
     * {@link CreatorPaneController#componentCB}
     */
    @Override
    public void selectComponentView(Component component) {
        for (ComponentViewController componentViewCtrl: componentViewControllerList) {
            // Add select effect to chosen ComponentView
            if ( componentViewCtrl.getComponent().equals(component)) {
                if (componentViewCtrl.isSelected()) { // Already selected
                    return;
                } else {
                    componentViewCtrl.selected();
                }
            }
            // Remove select effect from previous selection
            else if (componentViewCtrl.isSelected()) {
                componentViewCtrl.unselected();
            }
        }
    }
}
