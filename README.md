# 📸 Camera Equipment \& Inventory Hardware System

> A web-based inventory and rental management system for tracking camera bodies, lenses, and accessories — built as a group project for \\\*\\\*CSC 2032: Object-Oriented Programming\\\*\\\*.



\---

## 🎥 Demo Video

Watch a full walkthrough of the system in action:

**▶️** [**System Demonstration Video**](https://drive.google.com/file/d/1aoyZ6A_A-ckAAdsaE2QYak-idLzPZWas/view?usp=sharing)

\---

## 📖 Overview

Camera bodies, lenses, tripods, lighting rigs, and other production gear are expensive, delicate, and easy to lose track of once a studio or rental business owns more than a handful of items. This system solves that problem by letting staff:

* Register and categorize equipment
* Track physical location and checkout status
* Log who has an item, when it's due back, and whether it's overdue
* Record maintenance and repair history
* Generate reports on inventory health

At any moment, the system can answer three core questions:

1. 📍 **Where is this equipment right now, and who has it?**
2. 🔧 **What condition is it in, and when was it last serviced?**
3. ⏰ **Is it overdue for return, or due for scheduled maintenance?**

\---

## ✨ Key Features

|Feature|Description|
|-|-|
|🗂️ Equipment Registration|Add, edit, and categorize camera bodies, lenses, and accessories|
|🔄 Checkout / Check-in Workflow|Track rental transactions, due dates, and late fees|
|🛠️ Maintenance Logging|Record service history and repair costs per item|
|👥 Role-Based Access|Separate permissions for **Admin** and **Staff**|
|📊 Reporting|View overdue items and equipment due for service|
|🔍 Search \& Filter|Quickly locate equipment by type, status, or category|
|💳 Rental Payments|Process payments and calculate late fees|

\---

## 🏗️ System Architecture

The application follows a **layered, three-tier architecture**:

```
┌─────────────────────────────────────────┐
│   Presentation Layer (HTML/CSS/JS/JSP)   │
├─────────────────────────────────────────┤
│  Controller Layer  →  AdminController    │
├─────────────────────────────────────────┤
│  Service Layer  →  EquipmentService /    │
│                     EquipmentServiceImpl │
├─────────────────────────────────────────┤
│  Repository Layer  →  equipmentRepository│
├─────────────────────────────────────────┤
│  Entity Layer  →  Equipment, CameraBody, │
│  CameraLens, Accessories, User, etc.     │
├─────────────────────────────────────────┤
│         MySQL Database                   │
└─────────────────────────────────────────┘
```

* **Frontend:** HTML/CSS/JavaScript pages served via JSP
* **Backend:** Java + Spring Boot, running on Apache Tomcat
* **Database:** MySQL, storing equipment, users, categories, locations, checkout logs, and maintenance records
* **Encapsulation:** Powered by Lombok's `@Data` annotation for automatic getters/setters

\---

## 🧩 Object-Oriented Design

The system's class model was derived directly from the problem domain — physical equipment, the people who use it, where it's stored, and the events that happen to it.

|Class|Responsibility|
|-|-|
|`Equipment` (abstract)|Shared attributes/behaviour for all gear (ID, name, serial number, price, condition)|
|`CameraBody`, `CameraLens`, `Accessories`|Subclasses adding type-specific attributes and overriding rental/depreciation logic|
|`Category`|Groups equipment into types (DSLR, Mirrorless, Prime Lens, Lighting, etc.)|
|`MaintenanceRecord`|A single service/repair event tied to one equipment item|
|`RentalTransaction`|A checkout/check-in transaction record|
|`User` (`Admin`, `Staff`)|Represents system accounts and their permissions|

### Core OOP Principles Applied

* **🔒 Encapsulation** — Fields are kept `private` with safe access via Lombok-generated getters/setters (e.g. `User`, `MaintenanceRecord`)
* **🧬 Inheritance** — `CameraBody`, `CameraLens`, and `Accessories` all extend the abstract `Equipment` superclass
* **🎭 Polymorphism** — Subclass objects are treated generically through `Equipment` references (e.g. `equipmentRepository.save(equipment)`) regardless of concrete type
* **🎁 Abstraction** — Controllers depend only on the `EquipmentService` interface, staying unaware of the underlying implementation in `EquipmentServiceImpl`

\---

## 🗄️ Database Design

Equipment subtypes (`CameraBody`, `CameraLens`, `Accessories`) use **table-per-subclass inheritance**, each sharing a primary key with the parent `equipment` table — directly mirroring the Java class hierarchy.

|Table|Purpose|
|-|-|
|`user`|Admin and staff accounts and roles|
|`category`|Equipment categories (DSLR, Lens, Lighting, etc.)|
|`location`|Physical storage locations|
|`equipment`|Base table for shared equipment attributes|
|`camera\\\_body` / `camera\\\_lens` / `accessories`|Subtype tables with type-specific attributes|
|`maintenance\\\_record`|Service and repair history per item|
|`rental\\\_transaction`|Checkout / check-in transaction history|
|`payment`|Rental payment records|
|`customer`|Customer details for rental transactions|

\---

## 📂 Project Structure

```
src/main/java/com/camrent/
├── controller/          # HTTP request handlers (Admin, Staff)
├── dto/                 # Data transfer objects
├── entity/               # Equipment, CameraBody, CameraLens,
│                         # Accessories, User, MaintenanceRecord, etc.
├── repository/           # Spring Data repositories
├── service/               # Business logic interfaces
│   └── impl/              # Service implementations
└── CameraRentalApplication.java

src/main/resources/
├── templates/            # admin-dashboard.html, login.html, staff-dashboard.html
├── application.properties
└── application-test.properties
```

\---

## 👤 User Roles \& Permissions

|Role|Description|Key Permissions|
|-|-|-|
|**Admin**|Manages the overall system and inventory health|Add/remove equipment, manage user accounts, view all reports|
|**Staff**|Handles day-to-day equipment handling|Check equipment in/out, log maintenance, search inventory|

\---

## 🧪 Testing Plan

|Test Case|Input|Expected Result|
|-|-|-|
|Add equipment with negative price|`purchasePrice = -500`|`IllegalArgumentException` thrown; item not saved|
|Check out an already checked-out item|Item currently held by another staff member|`IllegalStateException` thrown; checkout rejected|
|Check in an overdue item|Item with a past due date|Marked returned and flagged `"returned late"`|
|Search inventory by category|`category = 'Lens'`|Only lens-type equipment returned|
|Calculate depreciation per subtype|List of `CameraBody`, `Lens`, `AccessoryGear`|Each uses its own overridden formula (polymorphism)|

\---

## 🌱 Git Collaboration Workflow

* Shared GitHub repository with `main` and `develop` branches
* Each member works on feature branches (e.g. `feature/checkout-module`) and opens pull requests for review
* Commit messages reference the class/feature being worked on, for clear contribution history

\---

## 🚀 Future Enhancements

* 📡 Barcode / QR-code scanning for faster check-in and check-out
* 📧 Email/SMS notifications for overdue items or upcoming maintenance
* 🌐 Customer-facing booking portal for external rentals
* 📈 Analytics dashboard showing utilization rate per equipment item

\---

## 🎯 Scope

This system covers equipment registration, categorization, location tracking, check-out/check-in workflows, maintenance logging, and basic reporting. **Not included:** barcode/RFID hardware integration (see *Future Enhancements*).

\---

## 👥 Authors

Submitted for **CSC 2032 — Object-Oriented Programming (Group Project)**

|Student ID|Name|
|-|-|
|AS20240593|Rushini Keshani|
|AS20240626|A.R. Bandara|

\---

## 📄 License

This project was developed for academic purposes as part of the CSC 2032 module and is intended for educational use.



