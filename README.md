# Campus Event Management Suite 🗓️
**Enterprise-Grade Logic for Academic Logistics at the University of Guelph**

## 📖 Overview
The Campus Event Management Suite is a full-stack Object-Oriented application designed to streamline event scheduling, user registration, and facility management. Built with a focus on strict Model-View-Controller (MVC) design patterns, this system ensures high data integrity and real-time state synchronization across multiple user types.

## ⚡ Core Architecture
* **Modular OOP Design:** Utilizes complex inheritance and polymorphism to manage diverse entities, including specialized User roles (Student, Staff, Guest) and Event types (Seminar, Workshop, Concert).
* **Real-Time Data Binding:** Powered by JavaFX `ObservableList` and `TableView` to guarantee that the UI instantly reflects the current state of the in-memory database.
* **Automated Data Pipeline:** Includes a robust `DataLoader` utility that parses flat-file datasets (CSV) to automatically hydrate the system's class hierarchies upon startup.
* **State Persistence Logic:** Manages complex booking states (Confirmed, Waitlisted, Cancelled) using optimized Java Collections for low-latency retrieval, ensuring data state is preserved across application restarts.

## 🛠️ Tech Stack
* **Language:** Java 21
* **Framework:** JavaFX (GUI & Data Binding)
* **Architecture:** Model-View-Controller (MVC)
* **Dependency Management:** Maven

## 💻 Execution & Setup
1. Clone the repository to your local environment.
2. Open the project in IntelliJ IDEA.
3. Navigate to `src/main/java/org/example/gui/Launcher.java`.
4. Run the `main` method to initialize the dashboard.
   * *Note: The system will automatically load the initial dataset from `users.csv`, `events.csv`, and `bookings.csv` on startup.*

---

## 🧭 TA Evaluation Guide: System Navigation

This application features a modular UI designed around the core system requirements. Please follow these workflows to evaluate the system's logic and rule enforcement.

### 1. User Management
* **Add a User:** Use the dedicated form fields and select the specific User Type (Student, Staff, Guest) from the dropdown to register a new profile.
* **View Profile & History:** Simply **click on any user row** in the main TableView. This action triggers an automatic detailed profile summary popup, which includes their personal details and a complete history of their active and cancelled bookings.

### 2. Event Management & Discovery
* **Create an Event:** Enter the requisite details (Event ID, Title, Date/Time, Location, Capacity) and select the specific Event Type to reveal type-specific attributes (e.g., Topic for Workshops, Speaker for Seminars).
* **Cancel an Event:** Select an event and press "Cancel Event". 
    * *Verification:* The system does not delete the event. It updates the status to **Cancelled**, leaves it visible in the table, automatically cancels all associated bookings, and clears the waitlist.
* **Search & Filter:** Use the search bar to find events by Title, or use the dropdown to filter by Event Type. Click **Clear** to instantly reset the search state and repopulate the full event list.

### 3. Booking Management (⚠️ Important Workflow)
To ensure high data integrity and prevent accidental cancellations, the booking system uses a strict targeting workflow.
* **How to Book/Cancel:** You **must** first select the Target User from the dropdown menu, and then select the Target Event. Once both entities are actively selected, press "Book" or "Cancel Booking". 
* **Note:** Clicking on a row in the booking history table will *not* trigger a cancellation; you must use the dual-selection method.
* *Verification:* The system actively enforces capacity limits (Student: 3, Staff: 5, Guest: 1) and prevents duplicate bookings. Cancelling a "Confirmed" booking will automatically trigger the waitlist promotion logic.

### 4. Waitlist Management
* **View Event Waitlist:** Select a target event and click **View Waitlist**. This opens a dedicated view showing the chronological order of users waiting for a seat.
* **Real-Time Notifications:** If a confirmed booking is cancelled, the UI will instantly process the promotion and issue a clear notification detailing which waitlisted user was successfully promoted to "Confirmed" status.

---
**Phase 2 Readiness:** This submission includes Data Persistence (CSV reading/writing), full OOP inheritance, automatic waitlist promotion, search/filter capabilities, and a comprehensive JUnit test suite.
