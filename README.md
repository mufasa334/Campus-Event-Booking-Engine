***

# Campus Event Management Suite 🗓️
**Enterprise-Grade Logic for Institutional Event Logistics**

## 📖 Overview
The Campus Event Management Suite is a full-stack Object-Oriented application designed to streamline event scheduling, user registration, and facility management. Built with a focus on strict Model-View-Controller (MVC) design patterns, this system ensures high data integrity and real-time state synchronization across multiple user types.

## ⚡ Core Architecture
* **Modular OOP Design:** Utilizes complex inheritance and polymorphism to manage diverse entities, including specialized User roles (Student, Staff, Guest) and Event types (Seminar, Workshop, Concert).
* **Real-Time Data Binding:** Powered by JavaFX `ObservableList` and `TableView` to guarantee that the UI instantly reflects the current state of the in-memory database.
* **Full-Cycle Data Persistence:** Features a robust dual-pipeline (`DataLoader` and `DataSaver`) that parses and writes flat-file datasets (CSV) from a dedicated directory, allowing the system to automatically load, save, and restore the complete system state (users, events, and bookings) across program runs.
* **State Persistence Logic:** Manages complex booking states (Confirmed, Waitlisted, Cancelled) using optimized Java Collections for low-latency retrieval and fair waitlist promotion logic.

## 🛠️ Tech Stack
* **Language:** Java 21
* **Framework:** JavaFX (GUI & Data Binding)
* **Architecture:** Model-View-Controller (MVC)
* **Testing:** JUnit (Automated Booking System Test Suite)
* **Dependency Management:** Maven

## 💻 Execution & Setup
1. Clone the repository to your local environment.
2. Open the project in IntelliJ IDEA.
3. Navigate to `src/main/java/org/example/gui/Launcher.java`.
4. Run the `main` method to initialize the dashboard.
5. **To execute the Test Suite:** Navigate to the test directory and run the JUnit test package to validate the 4 core booking rules and capacity logic.

---

## 🧭 System Administrator Workflow
This application features a modular UI designed around secure administrative operations. Below are the standard operating procedures for navigating the system's core engines.

### 1. User Management
* **Profile Registration:** Use the dedicated form fields (equipped with input validation) and select the specific User Type (Student, Staff, Guest) from the dropdown to register a new profile.
* **Audit Profile & History:** Click on any user row in the main TableView. This action triggers an automated detailed profile summary popup, providing a complete audit trail of their active and historical (cancelled) bookings.

### 2. Event Operations & Discovery
* **Deploy an Event:** Enter the requisite details (Event ID, Title, Date/Time, Location, Capacity) and select the specific Event Type to reveal type-specific attributes (e.g., Topic for Workshops, Speaker for Seminars). 
* **Update Event Information:** Double-click on any existing event row to open a dedicated Edit GUI, allowing administrators to modify details (like location or date) while ensuring the event remains in a valid state.
* **Inspect Event Roster:** Single-click an event to trigger a pop-up alert displaying the event's detailed profile, including its type-specific attributes and a roster of confirmed attendees.
* **Deactivate an Event:** Select an event and press "Cancel Event". 
    * *System Behavior:* To maintain database integrity, the system does not hard-delete the event. It updates the status to *Cancelled*, securely cancels all associated bookings, and purges the waitlist, leaving a complete audit trail.
* **Data Retrieval:** Use the dynamic search bar to find events by partial Title, or utilize the dropdown to filter strictly by Event Type. Click **Clear** to instantly reset the search state and fetch the full database list.

### 3. Booking Engine (Strict Concurrency Workflow)
To ensure high data integrity and prevent accidental data mutation, the booking engine requires a strict dual-target workflow.
* **Transaction Execution:** The administrator *must* first select the Target User from the dropdown menu, and then select the Target Event. Once both entities are actively verified, press "Book Event" or "Cancel Booking".
* **Role-Based Policy Enforcement:** The engine actively enforces capacity limits, prevents duplicate transactions, and strictly caps active booking limits based on the user's role (e.g., maximum 3 confirmed bookings for Students).
* **Clear Visual Feedback:** The UI provides immediate JavaFX pop-up alerts to block invalid actions (such as exceeding confirmed-booking limits) and strictly enforces institutional rules.

### 4. Waitlist Automation
* **Monitor Queue:** Navigate to the Waitlist Management view to see a dedicated, filtered list showing the chronological FIFO (First-In-First-Out) order of the waitlisted users.
* **Real-Time Processing:** When a confirmed booking is cancelled, the backend algorithm instantly processes the promotion of the first waitlisted user.
* **System Notifications:** Upon a successful waitlist promotion, the UI triggers a clear pop-up notification detailing which user was elevated to *Confirmed* status, fulfilling clear feedback requirements.
