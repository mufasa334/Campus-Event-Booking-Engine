package org.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Point to the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // 2. Load it once and set the size to 1000x700 immediately
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);

        // 3. Add your CSS (ensure the path starts with /)
        scene.getStylesheets().add(getClass().getResource("/org/example/gui/styles.css").toExternalForm());

        // 4. Configure and show the stage
        stage.setTitle("Campus Event Management System");
        stage.setScene(scene);
        stage.show();
    }
}