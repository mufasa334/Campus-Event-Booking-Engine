package org.example.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.Interpolator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController implements Initializable {

    // --- UI LINKAGE (Team: Do not modify these) ---
    @FXML private AnchorPane sidebarContainer;
    @FXML private AnchorPane contentArea;
    @FXML private TextField eventIdField, eventTitleField, eventCapacityField, specificAttributeField;
    @FXML private ComboBox<String> eventTypeDropdown;
    @FXML private ComboBox<String> bookingUserSelection;
    @FXML private ComboBox<String> bookingEventSelection;
    @FXML private ComboBox<String> waitlistEventSelection;
    @FXML private TextField idField, nameField, emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUserId, colName, colEmail, colType;

    // --- MASTER DATA LISTS (Team: Use these lists for your logic) ---
    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private ObservableList<User> allUsers = FXCollections.observableArrayList();
    private boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Pre-populating data for Phase 1 testing
        if (allUsers.isEmpty()) {
            allUsers.add(new Student("U001", "Alice Smith", "alice@uoguelph.ca"));
        }
        if (allEvents.isEmpty()) {
            allEvents.add(new Workshop("W101", "JavaFX Workshop", LocalDateTime.now(), "THRN 1313", 2, "GUI Design"));
            allEvents.add(new Seminar("S101", "AI Seminar", LocalDateTime.now(), "MACN 105", 50, "Dr. Taylor"));
        }
        setupUserTable();
        fillAllDropdowns();
    }

    // ============================================================
    // TEAM LOGIC SECTION: This is where the OOP "Glue" happens
    // ============================================================

    /**
     * ACTION: "Book" Button Clicked
     * Team: Implement the Booking/Waitlist logic here.
     */
    @FXML
    private void handleCreateBooking() {
        // 1. Get the IDs from the UI
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) return;

        // 2. Use the "Glue" helpers to get the actual OOP Objects
        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        // --- START OOP LOGIC HERE ---
        // Example: if (selectedEvent.isFull()) { waitlist.add(selectedUser); }
        // else { new Booking(selectedUser, selectedEvent); }

        System.out.println("Logic Hook: " + selectedUser.getName() + " is booking " + selectedEvent.getTitle());
    }

    // ↓↓↓ PASTE THE NEW CODE RIGHT HERE ↓↓↓

    /**
     * ACTION: "Cancel Booking" Button Clicked
     * Team: Write logic to cancel the user's booking for the selected event.
     * IMPORTANT: If a confirmed booking is cancelled, you MUST promote the first person on the waitlist!
     */
    @FXML
    private void handleCancelBooking() {
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) {
            System.out.println("UI Warning: Select a User and Event to cancel.");
            return;
        }

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        // --- TEAM: WRITE CANCEL BOOKING LOGIC HERE ---
        // Example: selectedEvent.getManager().cancelBooking(selectedUser);

        System.out.println("Logic Hook: Cancelling booking for " + selectedUser.getName() + " at " + selectedEvent.getTitle());
    }

    /**
     * ACTION: "Cancel Event" Button Clicked
     * Team: Write logic to mark the event as CANCELLED.
     * IMPORTANT: You must also cancel all bookings for this event and clear the waitlist.
     */
    @FXML
    private void handleCancelEvent() {
        // Using the waitlist dropdown to select which event to cancel for now
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) {
            System.out.println("UI Warning: Select an Event to cancel.");
            return;
        }

        Event selectedEvent = findEventById(eventId);

        // --- TEAM: WRITE CANCEL EVENT LOGIC HERE ---
        // Example: selectedEvent.setStatus(EventStatus.CANCELLED);

        System.out.println("Logic Hook: Shutting down entire event -> " + selectedEvent.getTitle());
    }

    /**
     * ACTION: "View Waitlist" Button Clicked
     * Team: Write logic to retrieve the waitlist for the selected event and print/display it.
     */
    @FXML
    private void handleViewWaitlist() {
        // Grabs the ID from your Waitlist UI dropdown
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) {
            System.out.println("UI Warning: Select an Event to view its waitlist.");
            return;
        }

        Event selectedEvent = findEventById(eventId);

        // --- TEAM: WRITE VIEW WAITLIST LOGIC HERE ---
        // Example: User[] currentWaitlist = selectedEvent.getManager().getWaitlist();
        // Print them to the console so we know the order is correct.

        System.out.println("Logic Hook: Fetching waitlist for " + selectedEvent.getTitle());
    }

    // ↑↑↑ PASTE ENDS HERE ↑↑↑

    /**
     * GLUE HELPER: Finds a User object in the master list using an ID string.
     * Use this in your logic so you don't have to touch the list directly.
     */
    public User findUserById(String id) {
        return allUsers.stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }


    /**
     * GLUE HELPER: Finds an Event object in the master list using an ID string.
     */
    public Event findEventById(String id) {
        return allEvents.stream()
                .filter(e -> e.getEventId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ============================================================
    // JAVAFX SYSTEM METHODS (Team: No need to touch anything below)
    // ============================================================

    private void setupUserTable() {
        if (userTable != null && colUserId != null) {
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colType.setCellValueFactory(new PropertyValueFactory<>("userType"));
            userTable.setItems(allUsers);
        }
    }

    private void fillAllDropdowns() {
        if (roleComboBox != null) roleComboBox.setItems(FXCollections.observableArrayList("Student", "Staff", "Guest"));
        if (eventTypeDropdown != null) eventTypeDropdown.setItems(FXCollections.observableArrayList("Workshop", "Seminar", "Concert"));
        if (bookingUserSelection != null) {
            bookingUserSelection.setItems(FXCollections.observableArrayList(allUsers.stream().map(User::getUserId).collect(Collectors.toList())));
        }
        ObservableList<String> eventIds = FXCollections.observableArrayList(allEvents.stream().map(Event::getEventId).collect(Collectors.toList()));
        if (bookingEventSelection != null) bookingEventSelection.setItems(eventIds);
        if (waitlistEventSelection != null) waitlistEventSelection.setItems(eventIds);
    }

    @FXML
    private void AddUser() {
        System.out.println("--- Add User Button Clicked ---");

        // Check if the fields actually exist
        if (idField == null || roleComboBox == null) {
            System.out.println("CRITICAL ERROR: idField or roleComboBox is NULL. Injection failed!");
            return;
        }

        String id = idField.getText();
        String name = nameField.getText();
        String type = roleComboBox.getValue();

        System.out.println("Data entered: ID=" + id + ", Type=" + type);

        // If you haven't selected a role, this line stops the method!
        if (id == null || type == null || id.isEmpty()) {
            System.out.println("Warning: ID or Role is empty. Stopping.");
            return;
        }

        User newUser = type.equals("Student") ? new Student(id, name, emailField.getText()) :
                type.equals("Staff") ? new Staff(id, name, emailField.getText()) :
                        new Guest(id, name, emailField.getText());

        allUsers.add(newUser);
        System.out.println("Success: User added to list. Total users: " + allUsers.size());

        fillAllDropdowns();
        idField.clear(); nameField.clear(); emailField.clear();
    }

    @FXML
    private void AddEvent() {
        String eId = eventIdField.getText();
        String type = eventTypeDropdown.getValue();
        if (eId == null || type == null) return;
        try {
            int cap = Integer.parseInt(eventCapacityField.getText());
            Event newEvent = type.equals("Workshop") ? new Workshop(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText()) :
                    type.equals("Seminar") ? new Seminar(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText()) :
                            new Concert(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText());
            allEvents.add(newEvent);
            fillAllDropdowns();
            eventIdField.clear(); eventTitleField.clear(); eventCapacityField.clear(); specificAttributeField.clear();
        } catch (Exception e) { System.out.println("Error adding event"); }
    }

    @FXML public void showUserManagement() { switchView("user-management.fxml"); }
    @FXML public void showEventsManagement() { switchView("events-management.fxml"); }
    @FXML public void showBookingsManagement() { switchView("bookings-management.fxml"); }
    @FXML public void showWaitlistsManagement() { switchView("waitlists-management.fxml"); }

    private void switchView(String fxmlFile) {
        try {
            if (contentArea == null) return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
            Platform.runLater(this::fillAllDropdowns);
            AnchorPane.setTopAnchor(view, 0.0); AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0); AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void toggleSidebar() {
        if (sidebarContainer == null) return;
        Timeline timeline = new Timeline();
        double targetWidth = isSidebarVisible ? 0 : 200;
        KeyValue widthValue = new KeyValue(sidebarContainer.prefWidthProperty(), targetWidth, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), widthValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(e -> { if (targetWidth == 0) sidebarContainer.setVisible(false); });
        if (!isSidebarVisible) sidebarContainer.setVisible(true);
        timeline.play();
        isSidebarVisible = !isSidebarVisible;
    }

    @FXML public void closeApplication() { Platform.exit(); System.exit(0); }
}