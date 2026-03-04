# ENGG*1420 Final Project: Campus Event Booking Engine (Phase 1)

### Project Status
[cite_start]This repository contains our Phase 1 submission for the Campus Event Booking System [cite: 4-5]. [cite_start]The current build demonstrates the core object-oriented architecture required for the project, including inheritance, encapsulation, and our foundational JavaFX graphical interface [cite: 6, 199-200, 206-207]. 

### Phase 1 Architecture
[cite_start]Instead of a database, Phase 1 currently relies on in-memory collections to manage state while we prove our class logic [cite: 201-203]. Our backend structures include:
* [cite_start]**User Models:** A base `User` class extended by `Student`, `Staff`, and `Guest`[cite: 201].
* [cite_start]**Event Models:** Subclasses for `Workshop`, `Seminar`, and `Concert`, each designed to handle unique data fields like speaker names or age restrictions [cite: 202, 29-32].
* [cite_start]**Booking State:** Initial groundwork for tracking `Confirmed`, `Waitlisted`, and `Cancelled` reservations[cite: 203].
* [cite_start]**Interface:** A functional JavaFX dashboard that allows admins to navigate between management screens and view user data in a TableView [cite: 88-94, 207].

---

### Setup and Execution Guide
[cite_start]To evaluate the GUI and Phase 1 logic, please follow these compilation steps [cite: 208-209]:

1. Download or clone this repository to your local machine.
2. Open IntelliJ IDEA and load the inner `Campus-Event-Booking-Engine` directory as your project.
3. Allow Maven a moment to sync and download the required JavaFX libraries from the `pom.xml`.
4. In your project tree, navigate to the main entry point:  
   `src/main/java/org/example/gui/Launcher.java`
5. Run the `main` method inside `Launcher.java`. 

The primary application window will launch, and you can use the sidebar to navigate the Phase 1 UI.
