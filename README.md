# Campus Event Management Suite 🗓️

**Enterprise-Grade Logic for Academic Logistics**

### 📖 Overview

The **Campus Event Management Suite** is a full-stack Object-Oriented application designed to streamline event scheduling, user registration, and facility management. Built with a focus on strict **Model-View-Controller (MVC)** design patterns, this system ensures high data integrity and real-time state synchronization across multiple user types.

### ⚡ Core Architecture

* **Modular OOP Design:** Utilizes complex inheritance and polymorphism to manage diverse entities, including specialized User roles (Student, Staff) and Event types (Seminar, Workshop, Concert).
* **Real-Time Data Binding:** Powered by **JavaFX** `ObservableList` and `TableView` to guarantee that the UI reflects the current state of the in-memory database instantly.
* **Automated Data Pipeline:** Includes a robust `DataLoader` utility that parses flat-file datasets (CSV) to automatically hydrate the system's class hierarchies upon startup.
* **State Persistence Logic:** Manages complex booking states (Confirmed, Waitlisted, Cancelled) using optimized Java Collections for low-latency retrieval.
---
### 🛠️ Tech Stack

* **Language:** Java 21
* **Framework:** JavaFX (GUI & Data Binding)
* **Architecture:** Model-View-Controller (MVC)
* **Dependency Management:** Maven
---
### 💻 Execution & Setup

1. Clone the repository to your local environment.
2. Open the project in **IntelliJ IDEA**.
3. Navigate to `src/main/java/org/example/gui/Launcher.java`.
4. Run the `main` method to initialize the dashboard.
