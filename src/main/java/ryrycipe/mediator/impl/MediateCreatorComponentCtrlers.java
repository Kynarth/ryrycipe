package ryrycipe.mediator.impl;

import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.ComponentViewController;
import ryrycipe.controller.CreatorPaneController;
import ryrycipe.exception.NullControllerException;
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

    @Override
    public void clearComponentViewControllers() {
        componentViewControllerList.clear();
    }

    /**
     * Update {@link CreatorPaneController#componentCB} with {@link Component} from clicked
     * {@link ComponentViewController}.
     *
     * @param component {@link Component} of {@link ComponentViewController}.
     */
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

    /**
     * Check the needed amount of materials to fill the {@link Component#amount}.
     *
     * @return int corresponding to the needed amount of materials to fill the {@link Component#amount}.
     */
    public int getNeededAmountOfMaterials() {
        try {
            ComponentViewController controller = getComponentViewController();

            return controller.getNeededMaterialNb();
        } catch (NullControllerException e) {
            LOGGER.error(e.getMessage());
            return 0;
        }
    }

    /**
     * Add double clicked {@link MaterialView} in corresponding ComponentView.
     *
     * @param materialView {@link MaterialView} double clicked in {@link CreatorPaneController#materialChooser}.
     */
    public void addMaterial(MaterialView materialView) {
        if (materialView.getMaterial().getNbMaterials() != 0) {
            try {
                ComponentViewController controller = getComponentViewController();
                // Check for already inserted same material
                if (!checkSameMaterial(materialView, controller)) {
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
            } catch (NullControllerException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public List<ComponentViewController> getComponentViewControllerList() {
        return componentViewControllerList;
    }

    public static MediateCreatorComponentCtrlers getInstance() {
        return ourInstance;
    }

    /**
     * Check if the user try to add the same {@link ryrycipe.model.Material} that he already added in the recipe.
     * If it the case, erase old {@link MaterialView} with new informations.
     *
     * @param materialView {@link MaterialView}.
     * @param controller Current selected {@link ComponentViewController}.
     * @return A boolean with true if same {@link ryrycipe.model.Material} was present otherwise false.
     */
    private boolean checkSameMaterial(MaterialView materialView, ComponentViewController controller) {
        for (Node usedMaterial: controller.getMaterialsContainer().getChildren()) {
            MaterialView tmp = (MaterialView) usedMaterial;
            // Avoid the empty MaterialView
            if (tmp.getMaterial() == null) {
                continue;
            }

            if (tmp.getMaterial().equals(materialView.getMaterial())) {
                tmp.getMaterial().setNbMaterials(
                    tmp.getMaterial().getNbMaterials() + materialView.getMaterial().getNbMaterials()
                );
                // Update ComponentView indicator in consequence
                controller.updateIndicator(materialView.getMaterial().getNbMaterials());
                // Rewrite with new infos
                tmp.addMaterialInfos();

                LOGGER.info("{} has been updated in the {} recipe's component",
                    materialView.getMaterial().getDescription(), controller.getComponent().getName()
                );

                return true;
            }
        }

        return false;
    }

    /**
     * Search {@link ComponentViewController} in the list for the selected {@link Component} in
     * {@link CreatorPaneController#componentCB}.
     *
     * @return {@link ComponentViewController}.
     * @throws NullControllerException when no {@link Component} selected or
     * {@link ComponentViewController} does not exits.
     */
    private ComponentViewController getComponentViewController() throws NullControllerException {
        for (ComponentViewController controller : componentViewControllerList) {
            if (controller.isSelected()) {
                return controller;
            }
        }

        throw new NullControllerException();
    }
}
