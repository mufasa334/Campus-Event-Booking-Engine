package org.example.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private AnchorPane sidebarContainer; // Matches the parent ID in Scene Builder

    private boolean isExpanded = true;

    @FXML
    private void toggleSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(sidebarContainer); // Moves the entire blue strip + buttons

        if (isExpanded) {
            // Adjust '-200' to match the actual width of your sidebarContainer
            slide.setToX(-200);
            isExpanded = false;
        } else {
            slide.setToX(0);
            isExpanded = true;
        }
        slide.play();
    }

    @FXML
    void closeApplication(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
}