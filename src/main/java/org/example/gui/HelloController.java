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
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.ButtonBar;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//DECLARING UI ELEMENTS
public class HelloController implements Initializable {

    //SIDE BAR AND MAIN PANEL//--------------------------------------------------------

    @FXML private AnchorPane sidebarContainer;
    @FXML private AnchorPane contentArea;

    //USER TAB//----------------------------------------------------------------------------

    //TABLE
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUserId, colName, colEmail, colType;
    //INTERACTABLES
    @FXML private TextField idField, nameField, emailField;
    @FXML private ComboBox<String> roleComboBox;

    //EVENT TAB//-----------------------------------------------------------------------------

    //TABLE
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> colEventId, colEventTitle, colEventType, colEventStatus, colEventDateTime;
    @FXML private TableColumn<Event, Integer> colEventCapacity;
    //INTERACTABLES
    @FXML private TextField eventSearchField;
    @FXML private ComboBox<String> eventTypeFilter;
    @FXML private TextField eventIdField, eventTitleField, eventLocationField, eventCapacityField, specificAttributeField, eventDateTimeField;
    @FXML private ComboBox<String> eventTypeDropdown;

    //BOOKING TAB//----------------------------------------------------------------------

    //TABLE
    @FXML private TextArea notificationArea;
    @FXML private TableView<BookingRecord> bookingTable;
    @FXML private TableColumn<BookingRecord, String> colBookId, colBookUser, colBookEvent, colBookTime, colBookStatus;
    //INTERACTABLES
    @FXML private ComboBox<String> bookingUserSelection;
    @FXML private ComboBox<String> bookingEventSelection;
    @FXML private ComboBox<String> waitlistEventSelection;

    //WAITLISTING TAB//-------------------------------------------------------------------------

    //TABLE
    @FXML private TableView<BookingRecord> waitlistTable;
    @FXML private TableColumn<BookingRecord, String> colWaitUser;
    @FXML private TableColumn<BookingRecord, String> colWaitStatus;

    //OBJECT ARRAYS//-----------------------------------------------------------------------------

    private static final ObservableList<BookingRecord> displayBookings = FXCollections.observableArrayList();
    private static final ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();
    //OTHER
    private static boolean isSidebarVisible = true;

    //---------------------------------------------------------------------------------------------

    //INSTANTIATES ALL ARRAYS  AND SETS UP TABLES
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean isFirstLoad = allEvents.isEmpty();

        //LOADS USER ARRAY FROM DATALOADER
        if (allUsers.isEmpty()) {
            DataLoader.loadUsers(allUsers);
        }

        //LOADS EVENT ARRAY FROM DATALOADER
        if (allEvents.isEmpty()) {
            DataLoader.loadEvents(allEvents);
        }

        //CREATES STORED BOOKING CONNECTIONS BETWEEN USER AND EVENT OBJECTS
        if (isFirstLoad) {
            DataLoader.loadBookings(allUsers, allEvents);
        }

        //TABLE SETUP
        refreshAllEventStatuses();
        setupUserTable();
        setupEventTable();
        refreshALLBookingsTable();
        fillAllDropdowns();

        //FILLS BOOKING TABLE WITH DATA
        if (bookingTable != null) {
            bookingTable.setItems(displayBookings);
        }
    }

    // =========================================================
    // BOOKING LIMIT + STATUS LOGIC
    // =========================================================

    //FINDS THE AMOUNT OF CONFIRMED BOOKINGS THIS USER CURRENTLY HAS
    //PRECONDITION:
    //              USER TYPE OBJECT - USER TO CHECK
    //POSTCONDITION:
    //              INTEGER - NUMBER OF CURRENT BOOKINGS
    private int getConfirmedBookingCount(User user) {
        int count = 0;

        //GO THROUGH ALL EVENTS AND IF THEY CONTAIN THE USER ADD TO COUNT
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

    //FINDS THE AMOUNT OF CONFIRMED BOOKINGS THIS USER CURRENTLY HAS EXCLUDING ONE EVENT
    //PRECONDITION:
    //              USER TYPE OBJECT - USER TO CHECK
    //              EVENT TYPE OBJECT - EVENT TO IGNORE IN COUNTING
    //POSTCONDITION:
    //              INTEGER - NUMBER OF CURRENT BOOKINGS EXCLUDING THE EVENT
    private int getConfirmedBookingCountExcludingEvent(User user, Event excludedEvent) {
        int count = 0;

        //GO THROUGH ALL EVENTS EXCEPT ONE AND IF THEY CONTAIN THE USER ADD TO COUNT
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

    //CHECKS IF THE EVENT HAS SPACE LEFT
    //PRECONDITION:
    //              EVENT TYPE OBJECT - EVENT TO CHECK
    //POSTCONDITION:
    //              BOOLEAN - RETURNS TRUE IF THERE IS EVENT SPACE
    private boolean eventHasConfirmedSpace(Event event) {
        int confirmedCount = 0;

        //COUNTS NUMBER OF CURRENT BOOKINGS
        for (BookingWaitlistingManager.BookingEntry booking : event.getManager().getBookings()) {
            if (booking.getStatus() == BookingWaitlistingManager.BookingStatus.CONFIRMED) {
                confirmedCount++;
            }
        }

        //CHECKS IF AMOUNT CURRENTLY BOOKED IS UNDER TOTAL CAPACITY
        return confirmedCount < event.getManager().getCapacity();
    }

    //REFRESHES EVENT STATUS FOR ALL EVENTS
    private void refreshAllEventStatuses() {
        for (Event event : allEvents) {
            refreshSingleEventStatuses(event);
        }
    }

    //WAITLISTING UPDATE FOR A SPECIFIC EVENT
    //PRECONDITION:
    //              EVENT TYPE OBJECT - EVENT TO UPDATE BOOKING + WAITLIST
    private void refreshSingleEventStatuses(Event event) {

        //GET BOOKING MANAGER FROM EVENT
        BookingWaitlistingManager manager = event.getManager();

        //GO THROUGH MANAGER AND TURN ALL NON-CANCELLED BOOKINGS TO WAITLISTED
        for (BookingWaitlistingManager.BookingEntry booking : manager.getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.WAITLISTED);
            }
        }

        //SORTS ALL NON-CANCELLED BOOKINGS IN ORDER OF CREATION TIME
        java.util.List<BookingWaitlistingManager.BookingEntry> activeBookings =
                manager.getBookings().stream()
                        .filter(b -> b.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED)
                        .sorted(java.util.Comparator.comparing(BookingWaitlistingManager.BookingEntry::getCreatedAt))
                        .toList();

        int confirmedCountForEvent = 0;

        //LOOPS THROUGH ALL BOOKINGS
        for (BookingWaitlistingManager.BookingEntry booking : activeBookings) {

            //CHECKS HOW MANY OTHER BOOKINGS THIS USER CURRENTLY HAS
            User user = booking.getUser();
            int userConfirmedElsewhere = getConfirmedBookingCountExcludingEvent(user, event);

            //IF EVENT IS NOT FULL AND PERSON HAS BOOKING CAPACITY, CONFIRM THEIR BOOKING
            if (confirmedCountForEvent < manager.getCapacity()
                    && userConfirmedElsewhere < user.getMaxConfirmedBookings()) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
                confirmedCountForEvent++;

            //ELSE KEEP THEM ON WAITLISTED
            } else {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.WAITLISTED);
            }
        }
    }

    // =========================================================
    // BOOKING ACTIONS
    // =========================================================

    //CREATES A BOOKING THROUGH THE UI (** "BOOK EVENT" BUTTON **)
    @FXML
    private void handleCreateBooking() {

        //FINDS USER AND EVENT SELECTED IN THE DROP-DOWN MENUS
        String uId = bookingUserSelection.getValue();
        String eId = bookingEventSelection.getValue();

        if (uId == null || eId == null) return;

        User selectedUser = findUserById(uId);
        Event selectedEvent = findEventById(eId);

        if (selectedUser == null || selectedEvent == null) return;

        //IF THE EVENT IS CANCELLED, IT IS UNBOOKABLE
        if(selectedEvent.getStatus() == Event.EventStatus.CANCELLED) return;

        //CHECKS WHETHER USER IS ALREADY BOOKED
        if (selectedEvent.getManager().containsUser(selectedUser)) {
            System.out.println("User is already booked or waitlisted for this event.");
            return;
        }

        //CHECKS WHETHER EVENT AND USER HAVE ENOUGH SPACE TO BOOK
        int confirmedCount = getConfirmedBookingCount(selectedUser);
        boolean eventHasSpace = eventHasConfirmedSpace(selectedEvent);

        //ONLY BLOCKS IF THERE IS STILL EVENT SPACE BUT USER IS FULL + INFORMATIVE POP-UP
        if (eventHasSpace && confirmedCount >= selectedUser.getMaxConfirmedBookings()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Booking Blocked");
            alert.setHeaderText("Booking Limit Reached");
            alert.setContentText("This " + selectedUser.getUserType() + " cannot exceed their limit of " + selectedUser.getMaxConfirmedBookings() + " active confirmed bookings.");
            alert.showAndWait();
            return;
        }

        //ADDS USER TO BOOKING AND REFRESHES WAITLIST
        selectedEvent.getManager().addUser(selectedUser);
        refreshAllEventStatuses();

        //DEBUG CODE TO TEST IF BOOKING WORKED
        BookingWaitlistingManager.BookingEntry booking =
                selectedEvent.getManager().findBookingByUser(selectedUser);

        if (booking != null) {
            System.out.println("DEBUG: Booked " + selectedUser.getName() +
                    " for " + selectedEvent.getEventId());
            System.out.println("DEBUG: Status = " + booking.getStatus());
        }

        //REFRESH THE TABLE CONTAINING BOOKINGS
        refreshALLBookingsTable();
    }

    //*** UNUSED DELETE METHOD ***
    @FXML
    private void handleDeleteBooking() {

        //FINDS USER AND EVENT SELECTED IN THE DROP-DOWN MENUS
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) return;

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        if (selectedUser == null || selectedEvent == null) return;

        //IF THERE IS NO BOOKING, INFORM USER WITH A POP-UP
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

        //IF BOOKING IS DELETED, INFORM THE USER WITH A POP-UP
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

    //CANCELS BOOKING THROUGH THE UI (** "CANCEL BOOKING" BUTTON **)
    @FXML
    private void handleCancelBooking() {

        //FINDS USER AND EVENT SELECTED IN THE DROP-DOWN MENUS
        String userId = bookingUserSelection.getValue();
        String eventId = bookingEventSelection.getValue();

        if (userId == null || eventId == null) return;

        User selectedUser = findUserById(userId);
        Event selectedEvent = findEventById(eventId);

        if (selectedUser == null || selectedEvent == null) return;

        //IF BOOKING IS CANCELLED, INFORM USER WITH A POP-UP
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
            //IF THERE IS NO BOOKING, INFORM USER WITH A POP-UP
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cancel Failed");
            alert.setHeaderText(null);
            alert.setContentText("No active booking found to cancel.");
            alert.showAndWait();
        }
    }

    //CANCELS AN EVENT - CANCELS ALL BOOKED USERS AND MAKES EVENT UNBOOKABLE (** "CANCEL EVENT" BUTTON **)
    @FXML
    private void handleCancelEvent() {

        //FINDS EVENT SELECTED IN THE DROP-DOWN MENU
        String eventId = waitlistEventSelection.getValue();

        if (eventId == null) return;

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent == null) return;

        //CANCELS EVENT
        selectedEvent.setStatus(Event.EventStatus.CANCELLED);

        //GOES THROUGH ALL BOOKINGS AND CANCELS THEM
        for (BookingWaitlistingManager.BookingEntry booking : selectedEvent.getManager().getBookings()) {
            if (booking.getStatus() != BookingWaitlistingManager.BookingStatus.CANCELLED) {
                booking.setStatus(BookingWaitlistingManager.BookingStatus.CANCELLED);
            }
        }

        //DEBUG CODE
        System.out.println("Logic Hook: EVENT CANCELLED -> " + selectedEvent.getTitle());
        System.out.println("All bookings for this event are now marked as CANCELLED.");

        //REFRESHES WAITLIST AND BOOKING TABLE
        refreshAllEventStatuses();
        refreshALLBookingsTable();

        if (eventTable != null) {
            eventTable.refresh();
        }
    }

    //REFRESHES BOOKING TABLE AFTER USE
    @FXML
    private void handleDropdownChange() {
        if (bookingEventSelection != null && bookingEventSelection.getValue() != null) {
            refreshALLBookingsTable();
        }
    }

    //SHOWS ALL WAITLISTED USERS FOR SELECTED EVENT (** "VIEW WAITLIST" BUTTON **)
    @FXML
    private void handleViewWaitlist() {

        //FINDS EVENT SELECTED IN THE DROP-DOWN MENU
        String eId = waitlistEventSelection.getValue();

        if (eId == null) {
            if (notificationArea != null) {
                notificationArea.setText("SYSTEM: Please select an Event ID first.");
            }
            return;
        }

        Event selectedEvent = findEventById(eId);
        if (selectedEvent == null) return;

        //CODE FOR FIRST WAITLIST MENU
        refreshWaitlistTable(selectedEvent);

        //CODE FOR SECOND WAITLIST MENU (STRINGBUILDER)
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

        //PRINTS THIS IF THERE ARE NO WAITLISTED USERS
        if (!hasWaitlisted) {
            rosterInfo.append("(No users are currently waitlisted for this event)");
        }

        //PRINTS STRINGBUILDER TO SCREEN
        if (notificationArea != null) {
            notificationArea.setText(rosterInfo.toString());
        }
    }

    // =========================================================
    // FINDERS
    // =========================================================

    //RETURNS USER FROM USER ARRAY GIVEN THEIR ID
    //PRECONDITION:
    //              STRING - ID OF USER TO FIND
    //POSTCONDITION:
    //              USER TYPE OBJECT - USER WITH THE GIVEN ID
    public User findUserById(String id) {
        return allUsers.stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //RETURNS EVENT FROM EVENT ARRAY GIVEN ITS ID
    //PRECONDITION:
    //              STRING - ID OF EVENT TO FIND
    //POSTCONDITION:
    //              EVENT TYPE OBJECT - EVENT WITH THE GIVEN ID
    public Event findEventById(String id) {
        return allEvents.stream()
                .filter(e -> e.getEventId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // =========================================================
    // TABLE SETUP
    // =========================================================

    //SETUP FOR THE TABLE ON THE USER TAB
    private void setupUserTable() {

        //SETUP AND POPULATING OF THE TABLE
        if (userTable != null && colUserId != null) {
            colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colType.setCellValueFactory(new PropertyValueFactory<>("userType"));
            userTable.setItems(allUsers);

            //POP-UP INFORMATION ABOUT THE USER ON CLICK
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

    //SETUP FOR THE TABLE ON THE EVENT TAB
    private void setupEventTable() {

        //SETUP AND POPULATING OF THE TABLE
        if (eventTable != null && colEventId != null) {
            colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
            colEventTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colEventCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            colEventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
            colEventStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colEventDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
            eventTable.setItems(allEvents);

            //POP-UP INFORMATION ABOUT THE EVENT ON CLICK + OPTION TO EDIT ON DOUBLE CLICK
            eventTable.setOnMouseClicked(event -> {
                Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
                if (selectedEvent == null) return;

                if (event.getClickCount() == 2) {
                    showEditEventDialog(selectedEvent);
                    eventTable.getSelectionModel().clearSelection();
                } else if (event.getClickCount() == 1) {
                    Event clickedEvent = selectedEvent;
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                if (eventTable.getSelectionModel().getSelectedItem() == clickedEvent) {
                                    Alert profile = new Alert(Alert.AlertType.INFORMATION);
                                    profile.setTitle("Event Profile Details");
                                    profile.setHeaderText("Event Summary: " + clickedEvent.getTitle());
                                    profile.setContentText(getEventProfileSummary(clickedEvent));
                                    profile.showAndWait();
                                    eventTable.getSelectionModel().clearSelection();
                                }
                            });
                        }
                    }, 300);
                }
            });;
        }
    }

    // =========================================================
    // DROPDOWNS
    // =========================================================

    //FILLS ALL DROP-DOWNS WITH THEIR RESPECTIVE OPTIONS
    private void fillAllDropdowns() {

        //SETUP FOR USER TYPE DROPDOWN
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Student", "Staff", "Guest"));
        }

        //SETUP FOR EVENT TYPE DROPDOWN
        if (eventTypeDropdown != null) {
            eventTypeDropdown.setItems(FXCollections.observableArrayList("Workshop", "Seminar", "Concert"));
        }

        //SETUP FOR EVENT TYPE FILTER DROPDOWN
        if (eventTypeFilter != null) {
            eventTypeFilter.setItems(FXCollections.observableArrayList("Workshop", "Seminar", "Concert"));
        }

        //SETUP FOR BOOKING TAB USERS DROPDOWN
        if (bookingUserSelection != null) {
            bookingUserSelection.setItems(
                    FXCollections.observableArrayList(
                            allUsers.stream().map(User::getUserId).collect(Collectors.toList())
                    )
            );
        }

        //SETUP FOR BOOKING TAB AND WAITLISTING TAB EVENTS DROPDOWN
        ObservableList<String> eventIds = FXCollections.observableArrayList(
                allEvents.stream().map(Event::getEventId).collect(Collectors.toList())
        );

        if (bookingEventSelection != null) bookingEventSelection.setItems(eventIds);
        if (waitlistEventSelection != null) waitlistEventSelection.setItems(eventIds);
    }

    // =========================================================
    // USER + EVENT CREATION
    // =========================================================

    //CREATES A USER AND ADDS THEM TO THE ARRAY (** "ADD-USER" BUTTON **)
    @FXML
    private void AddUser() {

        //OBTAIN DATA FROM TEXT BOXES + NECESSARY CHECKS
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

        //CREATES A USER OBJECT OF GIVEN TYPE FROM DATA
        User newUser = type.equals("Student")
                ? new Student(id, name, emailField.getText())
                : type.equals("Staff")
                ? new Staff(id, name, emailField.getText())
                : new Guest(id, name, emailField.getText());

        //ADDS NEW USER TO USER ARRAY
        allUsers.add(newUser);
        System.out.println("Success: User added to list. Total users: " + allUsers.size());

        //REFRESHES TABLE AND TEXT BOXES
        fillAllDropdowns();
        idField.clear();
        nameField.clear();
        emailField.clear();
    }

    //CREATES AN EVENT AND ADDS IT TO THE ARRAY (** "ADD EVENT" BUTTON **)
    @FXML
    private void AddEvent() {

        //OBTAIN DATA FROM TEXT BOXES + NECESSARY CHECKS
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

        //TRY TO CREATE AN EVENT OBJECT IN CASE SOMETHING HAS THE WRONG PARAMETER
        try {

            //FIRST CATCH FOR WHETHER CAPACITY IS A NUMBER
            int cap = Integer.parseInt(eventCapacityField.getText());

            //TRY-CATCH FOR IF LOCALDATETIME IS IN THE CORRECT FORMAT
            java.time.LocalDateTime eventDateTime;
            try {
                eventDateTime = java.time.LocalDateTime.parse(eventDateTimeField.getText());
            } catch (Exception ex) {
                eventDateTime = java.time.LocalDateTime.now();
                System.out.println("Warning: Invalid date format, defaulting to now.");
            }

            //CREATES EVENT OF GIVEN TYPE FROM DATA
            Event newEvent = type.equals("Workshop")
                    ? new Workshop(eId, eventTitleField.getText(), eventDateTime, eventLocationField.getText(), cap, specificAttributeField.getText())
                    : type.equals("Seminar")
                    ? new Seminar(eId, eventTitleField.getText(), eventDateTime, eventLocationField.getText(), cap, specificAttributeField.getText())
                    : new Concert(eId, eventTitleField.getText(), eventDateTime, eventLocationField.getText(), cap, specificAttributeField.getText());

            //ADDS EVENT TO EVENT ARRAY
            allEvents.add(newEvent);
            refreshAllEventStatuses();

            System.out.println("SUCCESS: " + newEvent.getTitle() + " added to the system.");

            //REFRESHES TABLE AND TEXT BOXES
            fillAllDropdowns();
            eventIdField.clear();
            eventTitleField.clear();
            eventLocationField.clear();
            eventCapacityField.clear();
            specificAttributeField.clear();
            eventDateTimeField.clear();

        } catch (NumberFormatException e) {
            System.out.println("Error: Capacity must be a whole number (no letters or spaces).");
        } catch (Exception e) {
            //EXTRA CATCH IN CASE ANYTHING UNEXPECTED HAPPENS
            System.out.println("Unexpected Error adding event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //METHOD FOR UPDATING EVENTS (DOUBLE-CLICK ON EVENT TAB TABLE)
    //PRECONDITION:
    //              EVENT TYPE OBJECT - EVENT TO UPDATE
    private void showEditEventDialog(Event selectedEvent) {

        //SETUP FOR THE UPDATE EVENT POP-UP
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Event");
        dialog.setHeaderText("Editing: " + selectedEvent.getTitle());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField(selectedEvent.getTitle());
        TextField locationField = new TextField(selectedEvent.getLocation());
        TextField capacityField = new TextField(String.valueOf(selectedEvent.getCapacity()));
        TextField dateTimeField = new TextField(selectedEvent.getDateTime().toString());
        TextField specificField = new TextField(selectedEvent.getSpecificAttribute());

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Location:"), locationField,
                new Label("Capacity:"), capacityField,
                new Label("Date/Time (yyyy-MM-ddTHH:mm):"), dateTimeField,
                new Label("Topic/Speaker/Age:"), specificField
        );
        dialog.getDialogPane().setContent(content);

        //IF THE SAVE BUTTON IS PRESSED, ALL THINGS IN THE TEXT BOXES BECOME THE NEW DATA
        dialog.showAndWait().ifPresent(result -> {
            if (result == saveButtonType) {
                try {
                    selectedEvent.setTitle(titleField.getText());
                    selectedEvent.setLocation(locationField.getText());
                    selectedEvent.setCapacity(Integer.parseInt(capacityField.getText()));
                    selectedEvent.setDateTime(java.time.LocalDateTime.parse(dateTimeField.getText()));

                    if (selectedEvent instanceof Workshop) {
                        ((Workshop) selectedEvent).setTopic(specificField.getText());
                    } else if (selectedEvent instanceof Seminar) {
                        ((Seminar) selectedEvent).setSpeakerName(specificField.getText());
                    } else if (selectedEvent instanceof Concert) {
                        ((Concert) selectedEvent).setAgeRestriction(specificField.getText());
                    }

                    setupEventTable();
                    eventTable.refresh();
                    System.out.println("SUCCESS: Event updated -> " + selectedEvent.getTitle());

                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Update Failed");
                    error.setContentText("Invalid input: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    //FILTERS EVENTS (** "SEARCH" BUTTON **)
    @FXML
    private void handleSearchEvents() {

        //OBTAIN DATA FROM TEXT BOXES
        String searchText = eventSearchField.getText().toLowerCase().trim();
        String typeFilter = eventTypeFilter.getValue();

        //CREATE NEW EVENT LIST
        ObservableList<Event> filtered = FXCollections.observableArrayList();

        //SEARCH THROUGH ALL EVENTS FOR ONES THAT MATCH THE CONDITIONS
        for (Event event : allEvents) {
            boolean matchesTitle = searchText.isEmpty() || event.getTitle().toLowerCase().contains(searchText);
            boolean matchesType = typeFilter == null || typeFilter.equals(event.getEventType().toString().charAt(0) + event.getEventType().toString().substring(1).toLowerCase());

            if (matchesTitle && matchesType) {
                //ADD TO NEW LIST IF MATCHING
                filtered.add(event);
            }
        }

        //PUTS THE FILTERED LIST ON THE TABLE
        if (eventTable != null) {
            eventTable.setItems(filtered);
            eventTable.refresh();
        }
    }

    //REFRESHES EVENT TABLE BACK TO NORMAL (** "CLEAR" BUTTON **)
    @FXML
    private void handleClearSearch() {

        //CLEARS TEXT BOX, DROP-DOWN, AND REFRESHES TABLE
        if (eventSearchField != null) eventSearchField.clear();
        if (eventTypeFilter != null) eventTypeFilter.setValue(null);
        if (eventTable != null) {
            eventTable.setItems(allEvents);
            eventTable.refresh();
        }
    }
    // =========================================================
    // VIEW SWITCHING
    // =========================================================

    //RUNS THE BELOW METHOD ON SELECTED FILE WHEN THE TAB IS PRESSED (** SIDE BUTTONS **)
    @FXML public void showUserManagement() { switchView("user-management.fxml"); }
    @FXML public void showEventsManagement() { switchView("events-management.fxml"); }
    @FXML public void showBookingsManagement() { switchView("bookings-management.fxml"); }
    @FXML public void showWaitlistsManagement() { switchView("waitlists-management.fxml"); }

    //SWITCHES MENU TO THE ONE ON THE FILE GIVEN
    //PRECONDITION:
    //              STRING - FXML FILE TO SWITCH TO
    private void switchView(String fxmlFile) {
        try {
            if (contentArea == null) return;

            //LOADS FILE
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

    //HIDES OR REVEALS SIDE BAR (** "MENU" BUTTON **)
    @FXML
    public void toggleSidebar() {
        if (sidebarContainer == null) return;

        //ADJUSTS MENU SIZE AND MAKES SIDE BAR VISIBLE / INVISIBLE
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

    //CLOSES APPLICATION (** "X" BUTTON **)
    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    //SAVES ALL CHANGES (** "SAVE DATA" BUTTON **)
    @FXML
    private void handleSaveData() {

        //SAVE ALL CHANGES INTO THE CORRECT FILE
        DataSaver.saveAll(allUsers, allEvents);

        //POP-UP TO CONFIRM THE SAVE
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

    //REFRESHES BOOKING TABLE
    private void refreshALLBookingsTable() {
        if (bookingTable == null || colBookId == null) return;

        //MAKES NEW COLUMNS WITH SET TITLES
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBookUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colBookEvent.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colBookTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colBookStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //CLEARS DATA
        displayBookings.clear();

        //GET ALL BOOKED USERS FROM ALL EVENTS AND FORMAT ACCORDINGLY
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

        //SET NEW TABLE AS CURRENT TABLE
        bookingTable.setItems(displayBookings);
        bookingTable.refresh();
    }

    //REFRESHES WAITLIST TABLE AND DISPLAYS WAITLIST OF SELECTED EVENT
    //PRECONDITION:
    //              EVENT TYPE OBJECT - EVENT TO DISPLAY WAITLIST OF
    private void refreshWaitlistTable(Event event) {
        if (waitlistTable == null || colWaitUser == null || colWaitStatus == null) return;

        //SET COLUMN TITLES
        colWaitUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colWaitStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        //NEW LIST FOR WAITLISTS
        ObservableList<BookingRecord> waitlistData = FXCollections.observableArrayList();

        //PUTS ALL WAITLISTED USERS ONTO THE NEW LIST
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

        //SETS TABLE TO NEW LIST
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

    //*** UNUSED METHOD TO DISPLAY BOOKING INFO ***
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

    //METHOD TO DISPLAY INFO OF USER ON TABLE-CLICK-POP-UP
    //PRECONDITION:
    //              USER TYPE OBJECT - USER TO GET INFO OF
    //POSTCONDITION:
    //              STRING - STRING OF PERFECTLY FORMATTED INFO FOR POP-UP
    private String getUserProfileSummary(User user) {

        //CREATION OF STRING BUILDER AND ADDITION OF ALL USER ATTRIBUTES
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ").append(user.getUserId()).append("\n");
        sb.append("Email: ").append(user.getEmail()).append("\n");
        sb.append("Type: ").append(user.getUserType()).append("\n\n");
        sb.append("BOOKING HISTORY:\n");

        boolean hasHistory = false;

        //ADDS ALL EVENTS THEY HAVE ANYTHING TO DO WITH AND THE STATUS
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

        //ADD THIS INSTEAD IF ABSOLUTELY NOTHING BOOKED / WAITLISTED
        if (!hasHistory) {
            sb.append("No bookings found for this user.");
        }

        return sb.toString();
    }

    //METHOD TO DISPLAY INFO OF EVENT ON TABLE-CLICK-POP-UP
    //PRECONDITION:
    //              EVENT TYPE OBJECT - EVENT TO GET INFO OF
    //POSTCONDITION:
    //              STRING - STRING OF PERFECTLY FORMATTED INFO FOR POP-UP
    private String getEventProfileSummary(Event event) {

        //CREATION OF STRING BUILDER AND ADDITION OF ALL EVENT ATTRIBUTES
        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ").append(event.getEventId()).append("\n");
        sb.append("Capacity: ").append(event.getCapacity()).append("\n");
        sb.append("Location: ").append(event.getLocation()).append("\n");
        sb.append("Time: ").append(event.getDateTime()).append("\n");
        sb.append("Type: ").append(event.getEventType()).append("\n");

        switch (event.getEventType()) {
            case SEMINAR -> sb.append("Speaker: ").append(event.getSpecificAttribute()).append("\n");
            case CONCERT -> sb.append("Age Restriction: ").append(event.getSpecificAttribute()).append("\n");
            case WORKSHOP -> sb.append("Topic: ").append(event.getSpecificAttribute()).append("\n");
        }

        sb.append("Status: ").append(event.getStatus()).append("\n\n");
        sb.append("CURRENT BOOKINGS:\n");

        //ADDS ALL USERS THAT ARE CURRENTLY BOOKED TO THIS EVENT
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