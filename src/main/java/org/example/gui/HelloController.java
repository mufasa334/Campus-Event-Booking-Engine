package org.example.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // PHASE 1 DATA STORAGE
    private ObservableList<User> allUsers = FXCollections.observableArrayList();

    // THIS MUST MATCH THE ID IN SCENE BUILDER
    @FXML private AnchorPane contentArea;

    // UI ELEMENTS
    @FXML private TextField idField, nameField, emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUserId, colName, colEmail, colType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Student", "Staff", "Guest"));
        }

        // ADDED EXTRA CHECK: Only setup the table if the table AND columns exist
        if (userTable != null && colUserId != null) {
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colType.setCellValueFactory(new PropertyValueFactory<>("userType"));
            userTable.setItems(allUsers);
        }

        // Load dummy data
        if (allUsers.isEmpty()) {
            allUsers.add(new Student("U001", "Alice Smith", "alice@uoguelph.ca"));
            allUsers.add(new Staff("U002", "Dr. Bob", "bob@uoguelph.ca"));
        }
    }

    @FXML
    private void AddUser() {
        System.out.println("Add User button clicked!");
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String type = roleComboBox.getValue();

        if (id == null || name == null || type == null || id.isEmpty()) return;

        User newUser;
        if (type.equals("Student")) newUser = new Student(id, name, email);
        else if (type.equals("Staff")) newUser = new Staff(id, name, email);
        else newUser = new Guest(id, name, email);

        allUsers.add(newUser);
        idField.clear(); nameField.clear(); emailField.clear();
    }

    // NAVIGATION METHODS
    @FXML private void showUserManagement() {
        System.out.println("Switching to User Management...");
        switchView("user-management.fxml");
    }
    @FXML private void showEventsManagement() {
        System.out.println("Switching to Events...");
        switchView("events-management.fxml");
    }
    @FXML private void showBookingsManagement() {
        System.out.println("Switching to Bookings...");
        switchView("bookings-management.fxml");
    }
    @FXML private void showWaitlistsManagement() {
        System.out.println("Switching to Waitlists...");
        switchView("waitlists-management.fxml");
    }

    private void switchView(String fxmlFile) {
        try {
            // Check if the content area exists
            if (contentArea == null) {
                System.err.println("CRITICAL ERROR: contentArea is null! Check fx:id in Scene Builder.");
                return;
            }
            Node view = new FXMLLoader(getClass().getResource(fxmlFile)).load();
            contentArea.getChildren().setAll(view);

            // Auto-stretch
            AnchorPane.setTopAnchor(view, 0.0); AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0); AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            System.err.println("Could not load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML private void toggleSidebar(javafx.event.Event event) { }

    @FXML private void closeApplication(javafx.event.Event event) {
        javafx.application.Platform.exit();
        System.exit(0);
    }
}