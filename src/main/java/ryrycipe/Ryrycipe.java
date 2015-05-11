package ryrycipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application.
 */
public class Ryrycipe extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ryrycipe");

        initialize();

        this.primaryStage.show();
    }

    /**
     *
     */
    private void initialize() throws IOException {
        // Load the main layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/RyrycipeView.fxml"));
        BorderPane borderPane = loader.load();

        // Create a scene with the loaded layout
        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Application's launcher
     * @param args Application's parameters
     */
    public static void main(String[] args) {
        launch(args);
    }
}
