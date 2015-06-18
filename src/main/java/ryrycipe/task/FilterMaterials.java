package ryrycipe.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.model.Material;
import ryrycipe.model.manager.MaterialManager;
import ryrycipe.model.view.MaterialView;

import java.util.Map;

/**
 * {@link javafx.concurrent.Task} to filter {@link MaterialView}s in function of parameters from
 * {@link RecipeCreatorController#getFilterParameters()}.
 */
public class FilterMaterials extends Task<Void> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FilterMaterials.class.getName());

    /**
     * Reference to {@link Ryrycipe}
     */
    private Ryrycipe mainApp;

    /**
     * Reference to {@link RecipeCreatorController} to get access to materials filter's parameters
     * and materials chooser to add {@link MaterialView}s.
     */
    private RecipeCreatorController creatorController;

    public FilterMaterials( Ryrycipe mainApp, RecipeCreatorController creatorController) {
        this.mainApp = mainApp;
        this.creatorController = creatorController;
    }

    @Override
    protected Void call() throws Exception {
        MaterialManager materialManager = new MaterialManager();
        Map<String, String> filterParameters = creatorController.getFilterParameters();

        // Create a MaterialView for each Material issue from filtering
        for (Material material : materialManager.filter(filterParameters)) {
            // Check if task has been cancelled
            if (this.isCancelled()) {
                LOGGER.info("Task: " + this.getTitle() + " has been cancelled.");
                break;
            }

            // Check if the material has already been added to the plan
            if (this.creatorController.getUsedMaterials().contains(material)) {
                continue;
            }

            // Create MaterialView from Material and add it to the materials chooser
            Platform.runLater(() -> {
                // Create MaterialView from current material
                material.setMatQualityLevel(filterParameters.get("qualityLvl"));
                MaterialView materialView = new MaterialView(material.getImage(), material);
                materialView.setRCController(creatorController.getRCController());
                materialView.setCreatorController(creatorController);
                materialView.setMainApp(mainApp);
                creatorController.getMaterialChooser().getChildren().add(materialView);
                updateMessage("Material: " + materialView.getMaterial().getName() + " has been added.");
            });
        }

        LOGGER.info("Materials displayed.");

        return null;
    }

    /**
     * Change the cursor for waiting one just before the run.
     */
    @Override
    protected void scheduled() {
        super.scheduled();
        mainApp.getPrimaryStage().getScene().setCursor(Cursor.WAIT);
    }

    /**
     * Change the cursor one for default at the end of task.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        mainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
    }
}
