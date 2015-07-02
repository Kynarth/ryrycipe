package ryrycipe.mediator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.ComponentViewController;
import ryrycipe.controller.CreatorPaneController;
import ryrycipe.mediator.IMediateCreatorComponentCtrlers;
import ryrycipe.model.Component;
import ryrycipe.model.manager.view.MaterialView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class making the link between {@link CreatorPaneController} and {@link ComponentViewController}s.
 */
public class MediateCreatorComponentCtrlers implements IMediateCreatorComponentCtrlers {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediateCreatorComponentCtrlers.class.getName());

    private static MediateCreatorComponentCtrlers ourInstance = new MediateCreatorComponentCtrlers();

    /**
     * Reference to {@link CreatorPaneController}.
     */
    private CreatorPaneController creatorPaneController;

    /**
     * Reference to {@link ComponentViewController}s.
     */
    private List<ComponentViewController> componentViewControllerList = new ArrayList<>();

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

    @Override
    public ComponentViewController getComponentView(Component component) {
        for (ComponentViewController controller : componentViewControllerList) {
            if (controller.getComponent().equals(creatorPaneController.getComponentCB().getValue())) {
                return controller;
            }
        }

        LOGGER.error("Couldn't find component for the selected material.");
        return null;
    }

    /**
     * Check the needed amount of materials to fill the {@link Component#amount}.
     *
     * @return int corresponding to the needed amount of materials to fill the {@link Component#amount}.
     */
    @Override
    public int getNeededAmountOfMaterials() {
        ComponentViewController controller = getComponentView(creatorPaneController.getComponentCB().getValue());

        if (controller != null) {
            return controller.getNeededMaterialNb();
        } else {
            return 0;
        }
    }

    /**
     * Add double clicked {@link MaterialView} in corresponding ComponentView.
     *
     * @param materialView {@link MaterialView} double clicked in {@link CreatorPaneController#materialChooser}.
     */
    @Override
    public void addMaterial(MaterialView materialView) {
        if (materialView.getMaterial().getNbMaterials() != 0) {
            ComponentViewController controller = getComponentView(creatorPaneController.getComponentCB().getValue());
            // Remove the event filter after that the material view get into ComponentView
            materialView.setOnMouseClicked(null);
            // Add material view to the corresponding ComponentView
            controller.getMaterialsContainer().getChildren().add(0, materialView);
            // Update ComponentView indicator in consequence
            controller.updateIndicator(materialView.getMaterial().getNbMaterials());
            // Write material's informations on its image
            materialView.addMaterialInfos();

            LOGGER.info("{} has been added to the {} recipe's component",
                materialView.getMaterial().getDescription(), controller.getComponent().getName()
            );
        }
    }

    public static MediateCreatorComponentCtrlers getInstance() {
        return ourInstance;
    }
}
