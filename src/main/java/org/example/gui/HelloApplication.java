package org.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class HelloApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Point to the main FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // 2. CRITICAL: Manually set the controller as the "Single Brain" [cite: 200, 207]
        // This ensures the same data lists are used across all FXML sub-views.
        HelloController controller = new HelloController();
        fxmlLoader.setController(controller);

        // 3. Load the root layout AFTER setting the controller
        Parent root = fxmlLoader.load();

        // 4. Style the stage as UNDECORATED for your custom UI
        stage.initStyle(StageStyle.UNDECORATED);

        // 5. Custom window dragging logic
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // 6. Setup Scene with dimensions and CSS
        Scene scene = new Scene(root, 1050, 550);
        String css = this.getClass().getResource("/org/example/gui/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        // 7. Show the stage
        stage.setTitle("Campus Event Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}