package org.example.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class HelloController {

    @FXML
    private StackPane contentArea; // Must match the fx:id of the center area in your FXML

    /**
     * Helper method to swap the center view.
     */
    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Error: Could not load " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    private void onUserManagementClick() {
        System.out.println("Navigating to User Management...");
        // Once we create user-view.fxml, we will uncomment the line below:
        // loadView("user-view.fxml");
    }

    @FXML
    private void onEventManagementClick() {
        System.out.println("Navigating to Event Management...");
        // loadView("event-view.fxml");
    }

    @FXML
    private void onBookingClick() {
        System.out.println("Navigating to Booking Management...");
        // loadView("booking-view.fxml");
    }

    @FXML
    private void onWaitlistClick() {
        System.out.println("Navigating to Waitlist Management...");
        // loadView("waitlist-view.fxml");
    }
}