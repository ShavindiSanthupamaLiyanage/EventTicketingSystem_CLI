# Real-Time Event Ticketing System

Welcome to the **Real-Time Event Ticketing System**, a multithreaded Java application designed for managing ticketing operations for events in real time.

## Features

- **Multithreading Support**: Simultaneous operations for vendors and customers.
- **Configurable System**: User-friendly CLI to initialize or load configurations.
- **Ticket Pool Management**: Centralized management of ticket inventory with status tracking.
- **Vendor and Customer Simulation**: Vendors release tickets while customers purchase them.
- **Concurrency Handling**: Ensures smooth operations in a multi-threaded environment.
- **Persistence**: Configuration and operational data are stored in a database and serialized files.

---

## Table of Contents

1. [Getting Started](#getting-started)
2. [How It Works](#how-it-works)
3. [System Configuration](#system-configuration)
4. [Classes and Responsibilities](#classes-and-responsibilities)
5. [Requirements](#requirements)
6. [Running the Application](#running-the-application)
7. [Generating API Documentation](#generating-api-documentation)
8. [Future Enhancements](#future-enhancements)

---

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/your-repo/event-ticketing-system.git
cd event-ticketing-system

```

### How It Works

- **Vendors**:
  Continuously add tickets to the pool at a configurable rate.
  
- **Customers**:
  Simulate users purchasing tickets with specific retrieval intervals.
  
- **Concurrency**:
  Java’s multithreading ensures smooth handling of simultaneous operations.
  
- **Monitoring**:
  Tracks the status of ticket pools to avoid overflows or underflows.

## System Configuration

Before starting the system, initialize the configuration:

1. Choose to **load existing settings** or **initialize a new configuration**.
2. Provide the following inputs:
   - Total number of tickets per vendor.
   - Ticket release rate.
   - Customer retrieval rate.
   - Maximum ticket capacity in the pool.
   - Number of vendors and customers.

Configuration is saved both in the database and as a JSON file for future use.

## Classes and Responsibilities

### Main Classes

- **`Ticket`**:
  Represents individual tickets with unique IDs and vendor associations.

- **`Vendor`**:
  Adds tickets to the pool at a defined rate.

- **`Customer`**:
  Simulates purchasing tickets from the pool.

- **`TicketPool`**:
  Manages ticket inventory and ensures thread-safe operations.

### Utility Classes

- **`SystemConfiguration`**:
  Handles initialization, validation, and persistence of system settings.

- **`LoggerConfiguration`**:
  Provides logging functionality.

- **`DbConfiguration`**:
  Manages database connectivity.

- **`FileConfiguration`**:
  Handles file operations for system configuration.

## Requirements

- **Java**:
  JDK 23 

- **Database**:
  MySQL (for storing configurations).

- **Build Tool**:
  Maven or IntelliJ IDEA for project setup.

## Running the Application

1. **Compile and Run**:
   Use IntelliJ IDEA or a terminal:
   ```bash
   javac -d out src/*.java
   java -cp out TicketingSystem


---

### Generating API Documentation

```markdown
## Generating API Documentation

1. **Use Javadoc**:
   - Run the following command:
     ```bash
     javadoc -d docs src/*.java
     ```
   - This generates HTML documentation in the `docs` folder.

2. **View Documentation**:
   Open `docs/index.html` in your browser.


