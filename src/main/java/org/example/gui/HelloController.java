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

public class HelloController {

    @FXML
    private AnchorPane sidebarContainer;

    @FXML
    private Node contentArea; // Changed from AnchorPane to Node for safety

    private boolean isExpanded = true;

    // --- SIDEBAR LOGIC ---
    @FXML
    private void toggleSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(sidebarContainer);

        if (isExpanded) {
            slide.setToX(-200);
            isExpanded = false;
        } else {
            slide.setToX(0);
            isExpanded = true;
        }
        slide.play();
    }

    // --- VIEW SWITCHER LOGIC ---
    @FXML
    private void showUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-management.fxml"));
            Node view = loader.load();

            // If you used a BorderPane as the root, we set the center.
            // If you used a StackPane/AnchorPane, we add to children.
            if (contentArea instanceof AnchorPane) {
                ((AnchorPane) contentArea).getChildren().setAll(view);
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            }

            System.out.println("User Management Loaded successfully!");
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

