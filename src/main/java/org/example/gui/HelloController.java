package org.example.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Added this import
import javafx.scene.Node;      // Added this import
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.IOException;    // Added this import
import javafx.scene.layout.Region;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class HelloController {

    @FXML
    private AnchorPane sidebarContainer;

    @FXML
    private Node contentArea; // Changed from AnchorPane to Node for safety

    private boolean isExpanded = true;

    // --- SIDEBAR LOGIC ---
    // --- SIDEBAR LOGIC ---
    @FXML
    private void toggleSidebar() {
        // We'll animate the width from 200 to 0 (or back)
        double targetWidth = isExpanded ? 0 : 200;

        // Create a smooth timeline animation (0.4 seconds)
        Timeline timeline = new Timeline();

        // This animates the actual width property of the sidebar
        KeyValue widthValue = new KeyValue(sidebarContainer.prefWidthProperty(), targetWidth);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), widthValue);

        timeline.getKeyFrames().add(keyFrame);

        // Toggle the state and play
        isExpanded = !isExpanded;
        timeline.play();

        // Optional: If you still want the "sliding" look, keep the TranslateX too
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), sidebarContainer);
        slide.setToX(isExpanded ? 0 : -200);
        slide.play();
    }

    // Add this import at the very top of HelloController.java

    @FXML
    private void showUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-management.fxml"));
            Node view = loader.load();

            if (contentArea instanceof AnchorPane) {
                AnchorPane pane = (AnchorPane) contentArea;
                pane.getChildren().setAll(view);

                // This forces the "Lego piece" to be elastic regardless of Scene Builder settings
                if (view instanceof Region) {
                    Region region = (Region) view;
                    region.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    region.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    region.setMaxWidth(Double.MAX_VALUE); // No max width limit
                    region.setMaxHeight(Double.MAX_VALUE); // No max height limit
                }

                // Pins the view to the corners of the contentArea
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }
            System.out.println("User Management Loaded - Forced Elasticity Enabled.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- EXIT LOGIC ---
    @FXML
    void closeApplication(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

}

