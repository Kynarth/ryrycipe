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

    @Override
    public void updateComponentCB(Component component) {
        creatorPaneController.getComponentCB().getSelectionModel().select(component);
    }
}
