package org.example.gui;

import javafx.animation.Interpolator;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController implements Initializable {

    @FXML private TextArea notificationArea;
    @FXML private TableView<BookingRecord> bookingTable;
    @FXML private TableColumn<BookingRecord, String> colBookId, colBookUser, colBookEvent, colBookTime, colBookStatus;

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

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> colEventId, colEventTitle, colEventType, colEventStatus;
    @FXML private TableColumn<Event, Integer> colEventCapacity;

    @FXML private TableView<BookingRecord> waitlistTable;
    @FXML private TableColumn<BookingRecord, String> colWaitUser;
    @FXML private TableColumn<BookingRecord, String> colWaitStatus;

    private static final ObservableList<BookingRecord> displayBookings = FXCollections.observableArrayList();
    private static final ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean isFirstLoad = allEvents.isEmpty();

        if (allUsers.isEmpty()) {
            DataLoader.loadUsers(allUsers);
        }

        if (allEvents.isEmpty()) {
            DataLoader.loadEvents(allEvents);
        }

        if (isFirstLoad) {
            DataLoader.loadBookings(allUsers, allEvents);
        }

        refreshAllEventStatuses();
        setupUserTable();
        setupEventTable();
        refreshALLBookingsTable();
        fillAllDropdowns();

        if (bookingTable != null) {
            bookingTable.setItems(displayBookings);
        }
    }

    // =========================================================
    // BOOKING LIMIT + STATUS LOGIC
    // =========================================================

    private int getConfirmedBookingCount(User user) {
        int count = 0;

        for (Event event : allEvents) {
            BookingWaitlistingManager.BookingEntry booking =
                    event.getManager().findBookingByUser(user);

            if (booking != null &&
                    booking.getStatus() == BookingWaitlistingManager.BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    private int getConfirmedBookingCountExcludingEvent(User user, Event excludedEvent) {
        int count = 0;

        for (Event event : allEvents) {
            if (event == excludedEvent) continue;

            BookingWaitlistingManager.BookingEntry booking =
                    event.getManager().findBookingByUser(user);

            if (booking != null &&
                    booking.getStatus() == BookingWaitlistingManager.BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    private boolean eventHasConfirmedSpace(Event event) {
        int confirmedCount = 0;

        for (BookingWaitlistingManager.BookingEntry booking : event.getManager().getBookings()) {
            if (booking.getStatus() == BookingWaitlistingManager.BookingStatus.CONFIRMED) {
                confirmedCount++;
            }
        }

        return confirmedCount < event.getManager().getCapacity();
    }

    private void refreshAllEventStatuses() {
        for (Event event : allEvents) {
            refreshSingleEventStatuses(event);
        }
    }

    private void refreshSingleEventStatuses(Event event) {
        BookingWaitlistingManager manager = event.getManager();

        for (BookingWaitlistingManager.BookingEntry booking : manager.getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.WAITLISTED);
            }
        }

        java.util.List<BookingWaitlistingManager.BookingEntry> activeBookings =
                manager.getBookings().stream()
                        .filter(b -> b.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED)
                        .sorted(java.util.Comparator.comparing(BookingWaitlistingManager.BookingEntry::getCreatedAt))
                        .toList();

        int confirmedCountForEvent = 0;

        for (BookingWaitlistingManager.BookingEntry booking : activeBookings) {
            User user = booking.getUser();
            int userConfirmedElsewhere = getConfirmedBookingCountExcludingEvent(user, event);

            if (confirmedCountForEvent < manager.getCapacity()
                    && userConfirmedElsewhere < user.getMaxConfirmedBookings()) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
                confirmedCountForEvent++;
            } else {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.WAITLISTED);
            }
        }
    }

    // =========================================================
    // BOOKING ACTIONS
    // =========================================================

    @FXML
    private void handleCreateBooking() {
        String uId = bookingUserSelection.getValue();
        String eId = bookingEventSelection.getValue();

        if (uId == null || eId == null) return;

        User selectedUser = findUserById(uId);
        Event selectedEvent = findEventById(eId);

        if (selectedUser == null || selectedEvent == null) return;

        if (selectedEvent.getManager().containsUser(selectedUser)) {
            System.out.println("User is already booked or waitlisted for this event.");
            return;
        }

        int confirmedCount = getConfirmedBookingCount(selectedUser);
        boolean eventHasSpace = eventHasConfirmedSpace(selectedEvent);

        // Only block if the user is trying to take a confirmed spot immediately
        if (eventHasSpace && confirmedCount >= selectedUser.getMaxConfirmedBookings()) {
            System.out.println("Maximum Confirmed Bookings Reached");
            return;
        }

        selectedEvent.getManager().addUser(selectedUser);
        refreshAllEventStatuses();

        BookingWaitlistingManager.BookingEntry booking =
                selectedEvent.getManager().findBookingByUser(selectedUser);

        if (booking != null) {
            System.out.println("DEBUG: Booked " + selectedUser.getName() +
                    " for " + selectedEvent.getEventId());
            System.out.println("DEBUG: Status = " + booking.getStatus());
        }

        refreshALLBookingsTable();
    }

    @FXML
    private void handleDeleteBooking() {
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) return;

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        if (selectedUser == null || selectedEvent == null) return;

        BookingWaitlistingManager.BookingEntry booking =
                selectedEvent.getManager().findBookingByUser(selectedUser);

        if (booking == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Failed");
            alert.setHeaderText(null);
            alert.setContentText("Booking not found.");
            alert.showAndWait();
            return;
        }

        boolean deleted = selectedEvent.getManager().deleteBooking(selectedUser);

        if (deleted) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Booking removed permanently.");
            alert.showAndWait();

            refreshAllEventStatuses();
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

        if (selectedUser == null || selectedEvent == null) return;

        boolean cancelled = selectedEvent.getManager().cancelBooking(selectedUser);

        if (cancelled) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Cancelled");
            alert.setHeaderText(null);
            alert.setContentText("Booking status changed to Cancelled.");
            alert.showAndWait();

            refreshAllEventStatuses();
            refreshALLBookingsTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cancel Failed");
            alert.setHeaderText(null);
            alert.setContentText("No active booking found to cancel.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelEvent() {
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) return;

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent == null) return;

        selectedEvent.setStatus(Event.EventStatus.CANCELLED);

        for (BookingWaitlistingManager.BookingEntry booking : selectedEvent.getManager().getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.CANCELLED);
            }
        }

        System.out.println("Logic Hook: EVENT CANCELLED -> " + selectedEvent.getTitle());
        System.out.println("All bookings for this event are now marked as CANCELLED.");

        refreshAllEventStatuses();
        refreshALLBookingsTable();

        if (eventTable != null) {
            eventTable.refresh();
        }
    }

    @FXML
    private void handleDropdownChange() {
        if (bookingEventSelection != null && bookingEventSelection.getValue() != null) {
            refreshALLBookingsTable();
        }
    }

    @FXML
    private void handleViewWaitlist() {
        String eId = waitlistEventSelection.getValue();

        if (eId == null) {
            if (notificationArea != null) {
                notificationArea.setText("SYSTEM: Please select an Event ID first.");
            }
            return;
        }

        Event selectedEvent = findEventById(eId);
        if (selectedEvent == null) return;

        refreshWaitlistTable(selectedEvent);

        StringBuilder rosterInfo = new StringBuilder();
        rosterInfo.append("--- WAITLIST FOR: ")
                .append(selectedEvent.getTitle())
                .append(" ---\n\n");

        boolean hasWaitlisted = false;

        for (BookingWaitlistingManager.BookingEntry booking : selectedEvent.getManager().getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.WAITLISTED) {
                continue;
            }

            User user = booking.getUser();

            rosterInfo.append("- ")
                    .append(user.getUserId())
                    .append(" - ")
                    .append(user.getName())
                    .append(" [")
                    .append(booking.getStatus())
                    .append("]\n");

            hasWaitlisted = true;
        }

        if (!hasWaitlisted) {
            rosterInfo.append("(No users are currently waitlisted for this event)");
        }

        if (notificationArea != null) {
            notificationArea.setText(rosterInfo.toString());
        }
    }

    // =========================================================
    // FINDERS
    // =========================================================

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

    // =========================================================
    // TABLE SETUP
    // =========================================================

    private void setupUserTable() {
        if (userTable != null && colUserId != null) {
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colType.setCellValueFactory(new PropertyValueFactory<>("userType"));
            userTable.setItems(allUsers);

            userTable.setOnMouseClicked(event -> {
                User selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null && event.getClickCount() == 1) {
                    Alert profile = new Alert(Alert.AlertType.INFORMATION);
                    profile.setTitle("User Profile Details");
                    profile.setHeaderText("Identity Summary: " + selectedUser.getName());
                    profile.setContentText(getUserProfileSummary(selectedUser));
                    profile.showAndWait();
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
            eventTable.setItems(allEvents);

            eventTable.setOnMouseClicked(event -> {
                Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
                if (selectedEvent != null && event.getClickCount() == 1) {
                    Alert profile = new Alert(Alert.AlertType.INFORMATION);
                    profile.setTitle("Event Profile Details");
                    profile.setHeaderText("Event Summary: " + selectedEvent.getTitle());
                    profile.setContentText(getEventProfileSummary(selectedEvent));
                    profile.showAndWait();
                    userTable.getSelectionModel().clearSelection();
                }
            });
        }
    }

    // =========================================================
    // DROPDOWNS
    // =========================================================

    private void fillAllDropdowns() {
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Student", "Staff", "Guest"));
        }

        if (eventTypeDropdown != null) {
            eventTypeDropdown.setItems(FXCollections.observableArrayList("Workshop", "Seminar", "Concert"));
        }

        if (bookingUserSelection != null) {
            bookingUserSelection.setItems(
                    FXCollections.observableArrayList(
                            allUsers.stream().map(User::getUserId).collect(Collectors.toList())
                    )
            );
        }

        ObservableList<String> eventIds = FXCollections.observableArrayList(
                allEvents.stream().map(Event::getEventId).collect(Collectors.toList())
        );

        if (bookingEventSelection != null) bookingEventSelection.setItems(eventIds);
        if (waitlistEventSelection != null) waitlistEventSelection.setItems(eventIds);
    }

    // =========================================================
    // USER + EVENT CREATION
    // =========================================================

    @FXML
    private void AddUser() {
        String id = idField.getText();
        String name = nameField.getText();
        String type = roleComboBox.getValue();

        if (id == null || id.isEmpty() || type == null) return;

        String shortId = id.substring(1);
        int countUp = 0;

        if (id.charAt(0) != 'U') {
            System.out.println("The User ID is Invalid needs a starting U");
            return;
        }

        for (int i = 0; i < shortId.length(); i++) {
            for (char j = '0'; j <= '9'; j++) {
                if (shortId.charAt(i) != j) {
                    countUp++;
                }
            }
            if (countUp == 10) {
                System.out.println("The User ID is Invalid needs to only have numbers after the first U");
                return;
            }
            countUp = 0;
        }

        if (!emailField.getText().contains("@")) {
            System.out.println("The Email is Invalid needs a @ symbol");
            return;
        }

        if (findUserById(id) != null) {
            System.out.println("Error: User ID '" + id + "' already exists! Blocked.");
            return;
        }

        User newUser = type.equals("Student")
                ? new Student(id, name, emailField.getText())
                : type.equals("Staff")
                ? new Staff(id, name, emailField.getText())
                : new Guest(id, name, emailField.getText());

        allUsers.add(newUser);
        System.out.println("Success: User added to list. Total users: " + allUsers.size());

        fillAllDropdowns();
        idField.clear();
        nameField.clear();
        emailField.clear();
    }

    @FXML
    private void AddEvent() {
        String eId = eventIdField.getText();
        String type = eventTypeDropdown.getValue();

        if (eId == null || eId.isEmpty() || type == null) {
            System.out.println("Warning: Event ID and Type are required.");
            return;
        }

        String shortEID = eId.substring(1);
        int countUp2 = 0;

        if (eId.charAt(0) != 'E') {
            System.out.println("The Event ID is Invalid needs a starting E");
            return;
        }

        for (int i = 0; i < shortEID.length(); i++) {
            for (char j = '0'; j <= '9'; j++) {
                if (shortEID.charAt(i) != j) {
                    countUp2++;
                }
            }
            if (countUp2 == 10) {
                System.out.println("The Event ID is Invalid needs to only have numbers after the first E");
                return;
            }
            countUp2 = 0;
        }

        if (findEventById(eId) != null) {
            System.out.println("Error: Event ID '" + eId + "' already exists! Blocked.");
            return;
        }

        try {
            int cap = Integer.parseInt(eventCapacityField.getText());

            Event newEvent = type.equals("Workshop")
                    ? new Workshop(eId, eventTitleField.getText(), java.time.LocalDateTime.now(), "TBD", cap, specificAttributeField.getText())
                    : type.equals("Seminar")
                    ? new Seminar(eId, eventTitleField.getText(), java.time.LocalDateTime.now(), "TBD", cap, specificAttributeField.getText())
                    : new Concert(eId, eventTitleField.getText(), java.time.LocalDateTime.now(), "TBD", cap, specificAttributeField.getText());

            allEvents.add(newEvent);
            refreshAllEventStatuses();

            System.out.println("SUCCESS: " + newEvent.getTitle() + " added to the system.");

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

    // =========================================================
    // VIEW SWITCHING
    // =========================================================

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

            Platform.runLater(() -> {
                setupUserTable();
                setupEventTable();
                refreshALLBookingsTable();
                fillAllDropdowns();
            });

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // APP CONTROLS
    // =========================================================

    @FXML
    public void toggleSidebar() {
        if (sidebarContainer == null) return;

        Timeline timeline = new Timeline();
        double targetWidth = isSidebarVisible ? 0 : 200;
        KeyValue widthValue = new KeyValue(sidebarContainer.prefWidthProperty(), targetWidth, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), widthValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(e -> {
            if (targetWidth == 0) sidebarContainer.setVisible(false);
        });

        if (!isSidebarVisible) sidebarContainer.setVisible(true);

        timeline.play();
        isSidebarVisible = !isSidebarVisible;
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

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

    // =========================================================
    // BOOKING / WAITLIST TABLES
    // =========================================================

    private void refreshALLBookingsTable() {
        if (bookingTable == null || colBookId == null) return;

        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBookUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookEvent.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colBookTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colBookStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        displayBookings.clear();

        for (Event event : allEvents) {
            BookingWaitlistingManager mgr = event.getManager();

            for (BookingWaitlistingManager.BookingEntry booking : mgr.getBookings()) {
                User user = booking.getUser();

                displayBookings.add(new BookingRecord(
                        booking.getBookingId(),
                        user.getUserId() + " - " + user.getName(),
                        mgr.getEventID(),
                        booking.getCreatedAt().toString(),
                        booking.getStatus().toString()
                ));
            }
        }

        bookingTable.setItems(displayBookings);
        bookingTable.refresh();
    }

    private void refreshWaitlistTable(Event event) {
        if (waitlistTable == null || colWaitUser == null || colWaitStatus == null) return;

        colWaitUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colWaitStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<BookingRecord> waitlistData = FXCollections.observableArrayList();

        for (BookingWaitlistingManager.BookingEntry booking : event.getManager().getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.WAITLISTED) {
                continue;
            }

            User user = booking.getUser();

            waitlistData.add(new BookingRecord(
                    booking.getBookingId(),
                    user.getUserId() + " - " + user.getName(),
                    event.getEventId(),
                    booking.getCreatedAt().toString(),
                    booking.getStatus().toString()
            ));
        }

        waitlistTable.setItems(waitlistData);
        waitlistTable.refresh();
    }

    // =========================================================
    // PROFILE / SUMMARY HELPERS
    // =========================================================

    private String generateUserSummary(User user) {
        StringBuilder summary = new StringBuilder();
        summary.append("--- USER PROFILE: ").append(user.getName()).append(" ---\n");
        summary.append("ID: ").append(user.getUserId()).append(" | Type: ").append(user.getUserType()).append("\n");
        summary.append("Email: ").append(user.getEmail()).append("\n\n");
        summary.append("CURRENT BOOKINGS:\n");

        boolean hasBookings = false;

        for (Event event : allEvents) {
            if (event.getManager().containsUser(user)) {
                String status = event.getManager().getStatus(user);
                summary.append("- ").append(event.getTitle())
                        .append(" (").append(event.getEventId()).append(") ")
                        .append("Status: ").append(status).append("\n");
                hasBookings = true;
            }
        }

        if (!hasBookings) {
            summary.append("No active bookings found.");
        }

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
            if (mgr.containsUser(user)) {
                String status = mgr.getStatus(user);
                sb.append("- ").append(event.getTitle())
                        .append(" (").append(event.getEventId()).append(") ")
                        .append("| Status: ").append(status).append("\n");
                found = true;
            }
        }

        if (!found) {
            sb.append("No bookings found for this user.");
        }

        return sb.toString();
    }

    private String getUserProfileSummary(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ").append(user.getUserId()).append("\n");
        sb.append("Email: ").append(user.getEmail()).append("\n");
        sb.append("Type: ").append(user.getUserType()).append("\n\n");
        sb.append("BOOKING HISTORY:\n");

        boolean hasHistory = false;

        for (Event event : allEvents) {
            BookingWaitlistingManager mgr = event.getManager();
            if (mgr.containsUser(user)) {
                String status = mgr.getStatus(user);
                sb.append("- ").append(event.getTitle())
                        .append(" (").append(event.getEventId()).append(") ")
                        .append("| Status: ").append(status).append("\n");
                hasHistory = true;
            }
        }

        if (!hasHistory) {
            sb.append("No bookings found for this user.");
        }

        return sb.toString();
    }

    private String getEventProfileSummary(Event event) {

        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ").append(event.getEventId()).append("\n");
        sb.append("Capacity: ").append(event.getCapacity()).append("\n");
        sb.append("Location: ").append(event.getLocation()).append("\n");
        sb.append("Time: ").append(event.getDateTime()).append("\n");

        switch (event.getEventType()) {
            case SEMINAR -> sb.append("Speaker: ").append(event.getSpecificAttribute()).append("\n");
            case CONCERT -> sb.append("Age Restriction: ").append(event.getSpecificAttribute()).append("\n");
            case WORKSHOP -> sb.append("Topic: ").append(event.getSpecificAttribute()).append("\n");
        }

        sb.append("Status: ").append(event.getStatus()).append("\n\n");
        sb.append("CURRENT BOOKINGS:\n");

        List<BookingWaitlistingManager.BookingEntry> bookings = event.getManager().getBookings();

        for(BookingWaitlistingManager.BookingEntry booking : bookings) {

            if(booking.getStatus() == BookingWaitlistingManager.BookingStatus.CONFIRMED)sb.append("- ").append(booking.getUser().getName()).append("\n");
        }

        return sb.toString();
    }

    // =========================================================
    // TABLE RECORD
    // =========================================================

    public static class BookingRecord {
        private final String bookingId;
        private final String userName;
        private final String eventId;
        private final String createdAt;
        private final String status;

        public BookingRecord(String bookingId, String userName, String eventId, String createdAt, String status) {
            this.bookingId = bookingId;
            this.userName = userName;
            this.eventId = eventId;
            this.createdAt = createdAt;
            this.status = status;
        }

        public String getBookingId() {
            return bookingId;
        }

        public String getUserName() {
            return userName;
        }

        public String getEventId() {
            return eventId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getStatus() {
            return status;
        }
    }
}