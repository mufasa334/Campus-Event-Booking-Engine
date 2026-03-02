package org.example.gui;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;



public class HelloApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException {


        // 1. Point to the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Parent root = fxmlLoader.load();

        stage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // 2. Load it once and set the size to 700x400 immediately
        Scene scene = new Scene(root, 1050, 550);
        String css = this.getClass().getResource("/org/example/gui/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        // 4. Configure and show the stage
        stage.setTitle("Campus Event Management System");
        stage.setScene(scene);
        stage.show();
    }
}