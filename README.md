# Medicine Schedule and Reminder Manager

A Java Swing desktop application that helps users manage their medicine schedules,
receive timely reminders, and track whether medicines were taken or missed.

## Problem Statement

Many people, especially elderly patients and those on long-term medication, forget
to take their medicines on time or lose track of their dosage schedule. This often
leads to missed doses and health complications. A simple local reminder manager is
needed.

## Objective

Build a complete, offline, desktop-based application that allows users to:

- Schedule medicines with dosage, type, dates, and reminder times
- Manage (add, search, edit, delete) medicines
- Receive pop-up reminders at the scheduled time
- Record each dose as TAKEN or MISSED
- View history and daily statistics

## Features

- **Dashboard** — today's date, today's medicines, upcoming medicines, taken/missed counts
- **Add Medicine** — name, dosage, type, start/end date, reminder time, notes
- **Medicine Management** — JTable view, search, edit, delete (with confirmation)
- **Reminder System** — background scheduler checks every 20 seconds, Swing pop-up
  with TAKEN/MISSED options, duplicate notifications prevented
- **History** — stores medicine name, date, scheduled time, and status
- **Auto-created SQLite database** on first run

## Technologies

- Java 17
- Java Swing (GUI)
- SQLite (embedded database)
- JDBC (via `sqlite-jdbc`)
- Maven (build tool)

## Requirements

- JDK 17 or higher
- Apache Maven 3.6+
- No internet connection needed after dependencies are downloaded
- No paid APIs or cloud services

## Installation

```bash
git clone <your-repo-url>
cd medicine-schedule-reminder-manager
mvn compile
```

## How to Run

```bash
mvn exec:java
```

or after packaging:

```bash
mvn package
java -cp "target/classes;%USERPROFILE%\.m2\repository\org\xerial\sqlite-jdbc\3.46.0.0\sqlite-jdbc-3.46.0.0.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar" com.medicinemanager.Main
```

(On Linux/macOS replace `;` with `:` in the classpath.)

## Build Command

```bash
mvn clean package
```

## Project Structure

```
medicine-schedule-reminder-manager/
├── pom.xml
├── README.md
├── .gitignore
└── src/main/java/com/medicinemanager/
    ├── Main.java
    ├── database/
    │   └── DatabaseManager.java
    ├── model/
    │   ├── Medicine.java
    │   └── HistoryEntry.java
    ├── dao/
    │   ├── MedicineDAO.java
    │   └── HistoryDAO.java
    ├── service/
    │   ├── MedicineService.java
    │   └── ReminderService.java
    ├── ui/
    │   ├── MainFrame.java
    │   ├── DashboardPanel.java
    │   ├── AddMedicinePanel.java
    │   ├── ManageMedicinesPanel.java
    │   └── HistoryPanel.java
    └── util/
        └── DateUtil.java
```

## Database Details

The app creates `medicine_manager.db` in the working directory on startup.

**Table `medicines`**

| Column | Type | Description |
|---|---|---|
| id | INTEGER PK | Auto-increment id |
| name | TEXT | Medicine name |
| dosage | TEXT | e.g. 500mg |
| type | TEXT | Tablet, Syrup, ... |
| start_date | TEXT | yyyy-MM-dd |
| end_date | TEXT | yyyy-MM-dd |
| reminder_time | TEXT | HH:mm |
| notes | TEXT | Optional notes |

**Table `medicine_history`**

| Column | Type | Description |
|---|---|---|
| id | INTEGER PK | Auto-increment id |
| medicine_name | TEXT | Name of medicine |
| date | TEXT | Date of dose |
| scheduled_time | TEXT | Scheduled time |
| status | TEXT | TAKEN or MISSED |

## Future Enhancements

- Multiple reminders per medicine per day
- Email/desktop-notification support
- Medicine inventory/stock tracking
- Export history to CSV/PDF
- Charts for adherence statistics
- User profiles

## Author

Your Name — College Project, 2026
