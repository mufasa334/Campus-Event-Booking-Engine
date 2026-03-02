package org.example.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox; // Important: Added this!
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Just ONE class line that implements Initializable
public class HelloController implements Initializable {

    @FXML
    private AnchorPane sidebarContainer;

    @FXML
    private Node contentArea;

    @FXML
    private ComboBox<String> roleComboBox; // Matches your fx:id in Scene Builder

    private boolean isExpanded = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This fills the dropdown with the roles required for Phase 1 [cite: 11, 137-138]
        if (roleComboBox != null) {
            ObservableList<String> roles = FXCollections.observableArrayList("Student", "Staff", "Guest");
            roleComboBox.setItems(roles);
        }
    }

    @FXML
    private void toggleSidebar() {
        double targetWidth = isExpanded ? 0 : 200;
        Timeline timeline = new Timeline();
        KeyValue widthValue = new KeyValue(sidebarContainer.prefWidthProperty(), targetWidth);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), widthValue);
        timeline.getKeyFrames().add(keyFrame);

        isExpanded = !isExpanded;
        timeline.play();

        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), sidebarContainer);
        slide.setToX(isExpanded ? 0 : -200);
        slide.play();
    }

    @FXML
    private void showUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-management.fxml"));
            Node view = loader.load();

            if (contentArea instanceof AnchorPane) {
                AnchorPane pane = (AnchorPane) contentArea;
                pane.getChildren().setAll(view);

                if (view instanceof Region) {
                    Region region = (Region) view;
                    region.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    region.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    region.setMaxWidth(Double.MAX_VALUE);
                    region.setMaxHeight(Double.MAX_VALUE);
                }

                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void showEventsManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("events-management.fxml"));
            Node view = loader.load();

            if (contentArea instanceof AnchorPane) {
                AnchorPane pane = (AnchorPane) contentArea;
                pane.getChildren().setAll(view);

                // Forces the screen to stretch and fit the middle space perfectly
                if (view instanceof Region) {
                    Region region = (Region) view;
                    region.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    region.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    region.setMaxWidth(Double.MAX_VALUE);
                    region.setMaxHeight(Double.MAX_VALUE);
                }

                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }
            System.out.println("Events Management Loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void closeApplication(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
}