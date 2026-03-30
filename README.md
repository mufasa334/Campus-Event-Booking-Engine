# Campus Event Management Suite 🗓️
**Enterprise-Grade Logic for Institutional Event Logistics**

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

---

## 🧭 System Administrator Workflow

This application features a modular UI designed around secure administrative operations. Below are the standard operating procedures for navigating the system's core engines.

### 1. User Management
* **Profile Registration:** Use the dedicated form fields and select the specific User Type (Student, Staff, Guest) from the dropdown to register a new profile.
* **Audit Profile & History:** Click on any user row in the main TableView. This action triggers an automated detailed profile summary popup, providing a complete audit trail of their active and historical (cancelled) bookings.

### 2. Event Operations & Discovery
* **Deploy an Event:** Enter the requisite details (Event ID, Title, Date/Time, Location, Capacity) and select the specific Event Type to reveal type-specific attributes (e.g., Topic for Workshops, Speaker for Seminars).
* **Deactivate an Event:** Select an event and press "Cancel Event". 
    * *System Behavior:* To maintain database integrity, the system does not hard-delete the event. It updates the status to **Cancelled**, securely cancels all associated bookings, and purges the waitlist, leaving a complete audit trail.
* **Data Retrieval:** Use the dynamic search bar to find events by Title, or utilize the dropdown to filter by Event Type. Click **Clear** to instantly reset the search state and fetch the full database list.

### 3. Booking Engine (Strict Concurrency Workflow)
To ensure high data integrity and prevent accidental data mutation, the booking engine requires a strict dual-target workflow.
* **Transaction Execution:** The administrator **must** first select the Target User from the dropdown menu, and then select the Target Event. Once both entities are actively verified, press "Book" or "Cancel Booking". 
* *System Behavior:* The engine actively enforces capacity limits and prevents duplicate transactions. Cancelling a "Confirmed" booking automatically triggers the backend promotion algorithm for the waitlist.

### 4. Waitlist Automation
* **Monitor Queue:** Select a target event and click **View Waitlist**. This opens a dedicated view showing the chronological FIFO (First-In-First-Out) order of the queue.
* **Real-Time Processing:** If a confirmed seat becomes available, the UI instantly processes the promotion and issues a system notification detailing the successful status elevation.

---
