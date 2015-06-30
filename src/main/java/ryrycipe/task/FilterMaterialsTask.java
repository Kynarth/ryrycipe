package ryrycipe.task;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Material;
import ryrycipe.model.manager.MaterialManager;
import ryrycipe.model.manager.view.MaterialView;

import java.util.Map;

/**
 * {@link javafx.concurrent.Task} to filter {@link MaterialView}s in function of parameters from
 * {@link ryrycipe.controller.CreatorPaneController#getFilterParameters()}.
 */
public class FilterMaterialsTask extends Task <ObservableList<MaterialView>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FilterMaterialsTask.class.getName());

    private Scene scene;
    private Map<String, String> filterParameters;

    public FilterMaterialsTask(Scene scene, Map<String, String> filterParameters) {
        this.scene = scene;
        this.filterParameters = filterParameters;
    }

    @Override
    protected ObservableList<MaterialView> call() {
        ObservableList<MaterialView> materialViews = FXCollections.observableArrayList();

        for (Material material : MaterialManager.filter(filterParameters)) {
            // Check if task has been cancelled
            if (this.isCancelled()) {
                LOGGER.info("Task: " + this.getTitle() + " has been cancelled.");
                break;
            }

            // Create MaterialView from Material and add it to the materials chooser
            Platform.runLater(() -> {
                // Create MaterialView from current material
                material.setMatQualityLevel(filterParameters.get("qualityLvl"));
                materialViews.add(new MaterialView(material.getImage(), material));

                // Update task
                final ObservableList<MaterialView> partialResult = materialViews;
                updateValue(partialResult);
                updateMessage("Material: " + material.getName() + " has been added.");
            });
        }

        LOGGER.info("Materials displayed.");

        return materialViews;
    }

        /**
     * Change the cursor for waiting one just before the run.
     */
    @Override
    protected void scheduled() {
        super.scheduled();
        scene.setCursor(Cursor.WAIT);
    }

    /**
     * Change the cursor one for default at the end of task.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        scene.setCursor(Cursor.DEFAULT);
    }
}
