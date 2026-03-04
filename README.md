# ENGG*1420 Final Project: Campus Event Booking Engine (Phase 1)

### Project Status
This repository contains our Phase 1 submission for the Campus Event Booking System. The current build demonstrates the core object-oriented architecture required for the project, including inheritance, encapsulation, and our foundational JavaFX graphical interface. 

### Phase 1 Architecture
Instead of a database, Phase 1 currently relies on in-memory collections to manage state while we prove our class logic. Our backend structures include:
* **User Models:** A base `User` class extended by `Student`, `Staff`, and `Guest`.
* **Event Models:** Subclasses for `Workshop`, `Seminar`, and `Concert`, each designed to handle unique data fields like speaker names or age restrictions.
* **Booking State:** Initial groundwork for tracking `Confirmed`, `Waitlisted`, and `Cancelled` reservations.
* **Interface:** A functional JavaFX dashboard that allows admins to navigate between management screens and view user data in a TableView.

---

### Setup and Execution Guide
To evaluate the GUI and Phase 1 logic, please follow these compilation steps:

1. Download or clone this repository to your local machine.
2. Open IntelliJ IDEA and load the inner `Campus-Event-Booking-Engine` directory as your project.
3. Allow Maven a moment to sync and download the required JavaFX libraries from the `pom.xml`.
4. In your project tree, navigate to the main entry point:  
   `src/main/java/org/example/gui/Launcher.java`
5. Run the `main` method inside `Launcher.java`. 

The primary application window will launch, and you can use the sidebar to navigate the Phase 1 UI.
