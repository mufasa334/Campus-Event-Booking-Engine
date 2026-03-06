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
    // --- BOOKING TABLE LINKAGE ---
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
        if (allUsers.isEmpty()) {
            DataLoader.loadUsers("src/main/resources/data/users.csv", allUsers);
        }

        // 2. Load Events
        if (allEvents.isEmpty()) {
            DataLoader.loadEvents("src/main/resources/data/events.csv", allEvents);
        }

        // 3. Load Bookings (ONLY on the very first load)
        if (isFirstLoad) {
            DataLoader.loadBookings("src/main/resources/data/bookings.csv", allUsers, allEvents);
        }

        // 4. Re-link the UI tables
        setupUserTable();
        setupEventTable();
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

        if (selectedUser != null && selectedEvent != null) {
            // 2. Add the name to the manager
            selectedEvent.getManager().addUser(selectedUser.getName());

            // 3. Debug to console immediately to confirm it worked
            System.out.println("DEBUG: Booked " + selectedUser.getName() + " for " + selectedEvent.getEventId());
            System.out.println("DEBUG: Current List Size: " + selectedEvent.getManager().UserList.size());

            // 4. Update the UI
            refreshBookingTable(selectedEvent);
        }
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

        if (userId == null || eventId == null) return;

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        // --- THE GLUE ---
        selectedEvent.getManager().cancelBooking(selectedUser.getName());
        System.out.println("Logic Hook: Cancelled booking for " + selectedUser.getName() + " at " + selectedEvent.getTitle());

        // Print the updated list to prove the waitlist promotion worked
        selectedEvent.getManager().bookWaitlistPrint();
        refreshBookingTable(selectedEvent);
    }

    /**
     * ACTION: "Cancel Event" Button Clicked
     * Team: Write logic to mark the event as CANCELLED.
     * IMPORTANT: You must also cancel all bookings for this event and clear the waitlist.
     */
    @FXML
    private void handleCancelEvent() {
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) return;

        Event selectedEvent = findEventById(eventId);

        // --- THE GLUE ---
        selectedEvent.setStatus(Event.EventStatus.CANCELLED);

        // Wipe the teammate's array list clean
        selectedEvent.getManager().UserList.clear();

        System.out.println("Logic Hook: EVENT CANCELLED -> " + selectedEvent.getTitle());
        System.out.println("All bookings and waitlists for this event have been wiped.");
    }

    /**
     * ACTION: "View Waitlist" Button Clicked
     * Team: Write logic to retrieve the waitlist for the selected event and print/display it.
     */
    @FXML
    private void handleViewWaitlist() {
        String eId = waitlistEventSelection.getValue();
        if (eId == null) return;

        // 1. Find the event in the master static list
        Event selectedEvent = findEventById(eId);

        if (selectedEvent != null) {
            // 2. Print the teammate's audit to console
            selectedEvent.getManager().bookWaitlistPrint();

            // 3. Push the data to the UI table
            refreshBookingTable(selectedEvent);
        }
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
                if (bookingTable != null) {
                    bookingTable.setItems(displayBookings);
                }
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



    private void refreshBookingTable(Event selectedEvent) {
        if (bookingTable == null || selectedEvent == null) return;

        // 1. Re-link the columns to the BookingRecord properties
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBookUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookEvent.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colBookTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colBookStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        displayBookings.clear();
        BookingWaitlistingManager mgr = selectedEvent.getManager();

        // 2. Loop through the actual names in the manager
        // We start at 0 and use a simple check to skip any potential nulls
        for (String name : mgr.UserList) {
            if (name != null && !name.trim().isEmpty()) {
                displayBookings.add(new BookingRecord(
                        mgr.getBookingID(),
                        name,
                        mgr.getEventID(),
                        "2026-03-06",
                        mgr.getStatus(name)
                ));
            }
        }

        // 3. Force the TableView to refresh
        bookingTable.setItems(displayBookings);
        bookingTable.refresh();
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