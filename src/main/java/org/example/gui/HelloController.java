package org.example.gui;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent; // MUST BE THIS ONE
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox; // Need this for the sidebar variable
import javafx.util.Duration;


public class HelloController {

@FXML
private VBox sidebar;
    private boolean isExpanded = true;

    @FXML
    private void toggleSidebar() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(sidebar);

        if (isExpanded) {
            // Slide it out of view to the left
            slide.setToX(-150); // Adjust based on your sidebar width
            slide.play();
            isExpanded = false;
        } else {
            // Slide it back to position 0
            slide.setToX(0);
            slide.play();
            isExpanded = true;
        }
    }

    @FXML
    void closeApplication(MouseEvent event) {
        Platform.exit(); // Call it directly
        System.exit(0);
    }
}