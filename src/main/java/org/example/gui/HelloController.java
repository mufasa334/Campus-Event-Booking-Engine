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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.example.gui.User.UserType.GUEST;
import static org.example.gui.User.UserType.STUDENT;
import static org.example.gui.User.UserType.STAFF;


public class HelloController implements Initializable {

    // --- UI LINKAGE (Team: Do not modify these) ---
    // --- BOOKING TABLE LINKAGE ---
    @FXML private TextArea notificationArea;
    @FXML private TableView<BookingRecord> bookingTable;
    @FXML private TableColumn<BookingRecord, String> colBookId, colBookUser, colBookEvent, colBookTime, colBookStatus;
    private static ObservableList<BookingRecord> displayBookings = FXCollections.observableArrayList();
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
    // --- EVENT TABLE LINKAGE ---
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> colEventId, colEventTitle, colEventType, colEventStatus;
    @FXML private TableColumn<Event, Integer> colEventCapacity;

    // --- MASTER DATA LISTS (Team: Use these lists for your logic) ---
    private static ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // We create a flag to check if this is the very first time the app is opening
        boolean isFirstLoad = allEvents.isEmpty();

        // 1. Load Users
        // 1. Load Users
        if (allUsers.isEmpty()) {
            DataLoader.loadUsers(allUsers);
        }

// 2. Load Events
        if (allEvents.isEmpty()) {
            DataLoader.loadEvents(allEvents);
        }

// 3. Load Bookings
        if (isFirstLoad) {
            DataLoader.loadBookings(allUsers, allEvents);
        }

        // 4. Re-link the UI tables
        setupUserTable();
        setupEventTable();
        refreshALLBookingsTable();
        fillAllDropdowns();

        if (bookingTable != null) {
            bookingTable.setItems(displayBookings);
        }
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
        String uId = bookingUserSelection.getValue();
        String eId = bookingEventSelection.getValue();

        if (uId == null || eId == null) return;

        // 1. Pull the EXACT objects from the static lists
        User selectedUser = findUserById(uId);
        Event selectedEvent = findEventById(eId);

        //Limits the number of bookings depending on the UserType
        if (selectedUser.getUserType() == GUEST){
            if(selectedUser.getLimitingInt() >= 1){
                System.out.println("Maximum Bookings Reached");
                return;
            }
        } else if (selectedUser.getUserType() == STUDENT){
            if(selectedUser.getLimitingInt() >= 3){
                System.out.println("Maximum Bookings Reached");
                return;
            }
        } else if (selectedUser.getUserType() == STAFF){
            if(selectedUser.getLimitingInt() >= 5){
                System.out.println("Maximum Bookings Reached");
                return;
            }
        }

        if (selectedUser != null && selectedEvent != null) {
            // 2. Add the name to the manager
            selectedEvent.getManager().addUser(selectedUser);

            // 3. Debug to console immediately to confirm it worked
            System.out.println("DEBUG: Booked " + selectedUser.getName() + " for " + selectedEvent.getEventId());
            System.out.println("DEBUG: Current List Size: " + selectedEvent.getManager().UserList.size());

            // 4. Update the UI
            refreshALLBookingsTable();

            //Part of limiting the number of bookings depending on the UserType
            selectedUser.limitingNumberUP();
        }
    }

    @FXML
    private void handleDropdownChange() {
        if (bookingEventSelection != null && bookingEventSelection.getValue() != null) {
            String selectedId = bookingEventSelection.getValue();
            Event selectedEvent = findEventById(selectedId);
            refreshALLBookingsTable();
        }
    }

    @FXML
    private void handleCancelBooking() {
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) return;

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        if (selectedUser != null && selectedEvent != null) {
            //Part of limiting the number of bookings depending on the UserType
            if (selectedEvent.getManager().UserList.contains(selectedUser.getName())) {
                selectedUser.limitingNumberDOWN();
            }

            // 1. Logic Hook: Cancel the booking [cite: 70]
            selectedEvent.getManager().cancelBooking(selectedUser);

            // 2. Promotion Logic: Check if someone was promoted
            // This checks if your teammate's logic promoted someone to "Booked" status
            String message = "Success: Cancelled booking for " + selectedUser.getName() + " at " + selectedEvent.getTitle() + ".";

            // NEW: Promotion Notification
            // We add a generic alert here to let the admin know the waitlist was processed
            Alert promotionAlert = new Alert(Alert.AlertType.INFORMATION);
            promotionAlert.setTitle("System Update");
            promotionAlert.setHeaderText("Waitlist Promotion Processed");
            promotionAlert.setContentText(message + "\n\nNote: The first eligible user on the waitlist (if any) has been automatically promoted to Confirmed.");
            promotionAlert.showAndWait();

            // 3. Update the UI [cite: 90]
            refreshALLBookingsTable();



        }
    }


    @FXML
    private void handleCancelEvent() {
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) return;

        Event selectedEvent = findEventById(eventId);

        // --- THE GLUE ---
        selectedEvent.setStatus(Event.EventStatus.CANCELLED);
        ArrayList<User> removal = new ArrayList<>(selectedEvent.getManager().UserList);

        // Wipe the teammate's array list clean
        for(User user: removal) {
            selectedEvent.getManager().cancelBooking(user);
        }

        System.out.println("Logic Hook: EVENT CANCELLED -> " + selectedEvent.getTitle());
        System.out.println("All bookings and waitlists for this event have been wiped.");
    }


    @FXML
    private void handleViewWaitlist() {
        String eId = waitlistEventSelection.getValue();
        if (eId == null) {
            if (notificationArea != null) notificationArea.setText("SYSTEM: Please select an Event ID first.");
            return;
        }

        Event selectedEvent = findEventById(eId);
        if (selectedEvent != null) {
            StringBuilder rosterInfo = new StringBuilder();
            rosterInfo.append("--- ROSTER FOR: ").append(selectedEvent.getTitle()).append(" ---\n");

            // Requirement: View waitlist in correct order [cite: 83, 126]
            if (selectedEvent.getManager().UserList.isEmpty()) {
                rosterInfo.append("(No confirmed or waitlisted users for this event)");
            } else {
                for (User user : selectedEvent.getManager().UserList) {
                    String status = selectedEvent.getManager().getStatus(user);
                    rosterInfo.append("- ").append(user.getName()).append(" [").append(status).append("]\n");
                }
            }

            if (notificationArea != null) {
                notificationArea.setText(rosterInfo.toString());
            }

            refreshALLBookingsTable();
        }
    }


    public User findUserById(String id) {
        return allUsers.stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }



    public Event findEventById(String id) {
        return allEvents.stream()
                .filter(e -> e.getEventId().equals(id))
                .findFirst()
                .orElse(null);
    }



    private void setupUserTable() {
        if (userTable != null && colUserId != null) {
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colType.setCellValueFactory(new PropertyValueFactory<>("userType"));
            userTable.setItems(allUsers);

            // FIX: Use a MouseClick listener instead of a Selection listener
            userTable.setOnMouseClicked(event -> {
                // Only trigger on a single click and ensure a row is actually selected
                User selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null && event.getClickCount() == 1) {

                    Alert profile = new Alert(Alert.AlertType.INFORMATION);
                    profile.setTitle("User Profile Details");
                    profile.setHeaderText("Identity Summary: " + selectedUser.getName());

                    // This calls the summary logic we wrote earlier
                    profile.setContentText(getUserProfileSummary(selectedUser));

                    profile.showAndWait();

                    // Crucial: Clear selection so it doesn't "ghost" click again
                    userTable.getSelectionModel().clearSelection();
                }
            });
        }
    }

    private void setupEventTable() {
        if (eventTable != null && colEventId != null) {
            colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
            colEventTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colEventCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            colEventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
            colEventStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Because allEvents is an ObservableList, it will auto-update when you click Add!
            eventTable.setItems(allEvents);
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
        String id = idField.getText();
        String name = nameField.getText();
        String type = roleComboBox.getValue();

        // 1. Basic empty check
        if (id == null || type == null || id.isEmpty()) return;

        // 2. THE GUARD (Must be here!)
        if (findUserById(id) != null) {
            System.out.println("Error: User ID '" + id + "' already exists! Blocked.");
            return; // This STOPS the code from reaching the "add" part below
        }

        // 3. Only if the guard passes, we create and add the user
        User newUser = type.equals("Student") ? new Student(id, name, emailField.getText()) :
                type.equals("Staff") ? new Staff(id, name, emailField.getText()) :
                        new Guest(id, name, emailField.getText());

        allUsers.add(newUser); // This adds it to the table
        System.out.println("Success: User added to list. Total users: " + allUsers.size());

        fillAllDropdowns();
        idField.clear(); nameField.clear(); emailField.clear();
    }

    @FXML
    private void AddEvent() {
        String eId = eventIdField.getText();
        String type = eventTypeDropdown.getValue();

        // 1. Basic validation: Ensure ID and Type are selected
        if (eId == null || eId.isEmpty() || type == null) {
            System.out.println("Warning: Event ID and Type are required.");
            return;
        }

        // 2. THE GUARD: Prevent duplicate Event IDs
        if (findEventById(eId) != null) {
            System.out.println("Error: Event ID '" + eId + "' already exists! Blocked.");
            return; // Emergency exit!
        }

        try {
            // 3. Convert capacity string to integer
            int cap = Integer.parseInt(eventCapacityField.getText());

            // 4. Determine which subclass to instantiate
            Event newEvent = type.equals("Workshop") ?
                    new Workshop(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText()) :
                    type.equals("Seminar") ?
                            new Seminar(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText()) :
                            new Concert(eId, eventTitleField.getText(), LocalDateTime.now(), "TBD", cap, specificAttributeField.getText());

            // 5. Add to the Master List (this updates the UI Table automatically)
            allEvents.add(newEvent);

            System.out.println("SUCCESS: " + newEvent.getTitle() + " added to the system.");

            // 6. Refresh dropdowns and wipe fields
            fillAllDropdowns();
            eventIdField.clear();
            eventTitleField.clear();
            eventCapacityField.clear();
            specificAttributeField.clear();

        } catch (NumberFormatException e) {
            System.out.println("Error: Capacity must be a whole number (no letters or spaces).");
        } catch (Exception e) {
            System.out.println("Unexpected Error adding event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML public void showUserManagement() { switchView("user-management.fxml"); }
    @FXML public void showEventsManagement() { switchView("events-management.fxml"); }
    @FXML public void showBookingsManagement() { switchView("bookings-management.fxml"); }
    @FXML public void showWaitlistsManagement() { switchView("waitlists-management.fxml"); }

    private void switchView(String fxmlFile) {
        try {
            if (contentArea == null) return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            // Use 'this' so we keep the SAME controller instance with our STATIC lists
            loader.setController(this);
            Node view = loader.load();

            contentArea.getChildren().setAll(view);

            // CRITICAL: After loading the new FXML, we MUST tell the new TableView
            // to look at our data immediately, otherwise it stays blank.
            Platform.runLater(() -> {
                setupUserTable();
                setupEventTable();

                refreshALLBookingsTable(); // <--- ADD THIS
                fillAllDropdowns();
            });

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

    @FXML
private void handleSaveData() {
    DataSaver.saveAll(allUsers, allEvents);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Save Successful");
    alert.setHeaderText(null);
    alert.setContentText("All data has been saved successfully!\n\nUsers, events, and bookings have been written to the CSV files.");
    alert.showAndWait();

    System.out.println("Manual save triggered by user.");
}

    // This new method loops through EVERY event and prints EVERY booking to the table.
    private void refreshALLBookingsTable() {
        if (bookingTable == null || colBookId == null) return;

        // 1. THIS WAS MISSING: Tell the columns how to read the data!
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBookUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookEvent.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colBookTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colBookStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        displayBookings.clear();

        // 2. Loop through all events in the system
        for (Event event : allEvents) {
            BookingWaitlistingManager mgr = event.getManager();

            // Loop through all users in that specific event
            for (User user : mgr.UserList) {
                if (user != null && !user.getName().trim().isEmpty()) {
                    displayBookings.add(new BookingRecord(
                            mgr.getBookingID(), user.getName(), event.getEventId(), "2026-03-06", mgr.getStatus(user)
                    ));
                }
            }
        }

        // 3. THIS WAS MISSING: Give the full list to the table!
        bookingTable.setItems(displayBookings);
        bookingTable.refresh();
    }

    private String generateUserSummary(User user) {
        StringBuilder summary = new StringBuilder();
        summary.append("--- USER PROFILE: ").append(user.getName()).append(" ---\n");
        summary.append("ID: ").append(user.getUserId()).append(" | Type: ").append(user.getUserType()).append("\n");
        summary.append("Email: ").append(user.getEmail()).append("\n\n");
        summary.append("CURRENT BOOKINGS:\n");

        boolean hasBookings = false;
        for (Event event : allEvents) {
            // Check if user name exists in this event's manager list [cite: 73]
            if (event.getManager().UserList.contains(user)) {
                String status = event.getManager().getStatus(user);
                summary.append("- ").append(event.getTitle())
                        .append(" (").append(event.getEventId()).append(") ")
                        .append("Status: ").append(status).append("\n");
                hasBookings = true;
            }
        }

        if (!hasBookings) summary.append("No active bookings found.");
        return summary.toString();
    }


    private String getUserBookingSummary(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(user.getName()).append("'s Profile ---\n");
        sb.append("User ID: ").append(user.getUserId()).append("\n");
        sb.append("Type: ").append(user.getUserType()).append("\n");
        sb.append("Email: ").append(user.getEmail()).append("\n\n");
        sb.append("BOOKING HISTORY:\n");

        boolean found = false;
        for (Event event : allEvents) {
            BookingWaitlistingManager mgr = event.getManager();
            // Check if the user is in this specific event roster
            if (mgr.UserList.contains(user)) {
                String status = mgr.getStatus(user);
                sb.append("- ").append(event.getTitle())
                        .append(" (").append(event.getEventId()).append(") ")
                        .append("| Status: ").append(status).append("\n");
                found = true;
            }
        }

        if (!found) sb.append("No bookings found for this user.");
        return sb.toString();
    }



    private String getUserProfileSummary(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ").append(user.getUserId()).append("\n"); // [cite: 13, 101]
        sb.append("Email: ").append(user.getEmail()).append("\n"); // [cite: 13, 101]
        sb.append("Type: ").append(user.getUserType()).append("\n\n"); // [cite: 11, 101]
        sb.append("BOOKING HISTORY:\n"); // [cite: 18, 72]

        boolean hasHistory = false;
        for (Event event : allEvents) { // [cite: 41, 113]
            BookingWaitlistingManager mgr = event.getManager();
            if (mgr.UserList.contains(user)) { // [cite: 49]
                String status = mgr.getStatus(user); // [cite: 55, 90]
                sb.append("- ").append(event.getTitle()) // [cite: 24, 72]
                        .append(" (").append(event.getEventId()).append(") ") // [cite: 23]
                        .append("| Status: ").append(status).append("\n"); // [cite: 55, 121]
                hasHistory = true;
            }
        }
        if (!hasHistory) sb.append("No bookings found for this user.");
        return sb.toString();
    }


    /**
     * Tiny Wrapper Class so the JavaFX TableView can read the data.
     */
    public static class BookingRecord {
        private String bookingId, userName, eventId, createdAt, status;

        public BookingRecord(String bookingId, String userName, String eventId, String createdAt, String status) {
            this.bookingId = bookingId; this.userName = userName;
            this.eventId = eventId; this.createdAt = createdAt; this.status = status;
        }
        // The table REQUIRES these exact getters to work
        public String getBookingId() { return bookingId; }
        public String getUserName() { return userName; }
        public String getEventId() { return eventId; }
        public String getCreatedAt() { return createdAt; }
        public String getStatus() { return status; }
    }


}