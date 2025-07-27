# 📚 Library Management Console App

A simple Java console application for managing a library system with support for user roles, borrowing books, and persistent MySQL database storage.

---

## ✅ Features

### 👤 User Roles
- **Admin**
    - Add, edit, or delete books
    - Register users (Admin or Regular)
- **Regular User**
    - View books
    - Borrow and return books

### 💾 Data Persistence
- Data is stored in a MySQL database
- Borrowed books are tracked per user

### 📦 OOP + Java Collections
- `Book`, `User`, `Admin`, `RegularUser` entities
- `List`, `Map`, `Set` usage
- Reusable generic search functions

---

## 🚀 How to Run the App

### 🔧 Prerequisites
- Java 19 or higher
- MySQL server installed and running

### 🛠️ Step-by-Step Setup

1. 🗄️ Create Database Schema

   Run the schema SQL script:

   mysql -u root -p < db_schema.sql

2. ⚙️ Configure DB Connection

   Open `src/storage/config.properties` and set the following:

   url=jdbc:mysql://localhost:3306/library_db
   username=root
   password=your_password

   Make sure the MySQL service is running.

3. (Optional) Load Sample Data

   You can optionally populate the database with test data:

   mysql -u root -p library_db < sample_data.sql

4. ▶️ Run the App

   Compile and run the app from your IDE or terminal:

   javac -d out src/**/*.java
   java -cp out LibraryApp

---

## 📂 Sample User Flow

- Launch app
- Login as `admin` or `regular`
- Admins can manage books and users
- Regular users can borrow and return available books

---

## 🧱 Built With

- Java (OOP, JDBC, Console I/O)
- MySQL
- JDBC
- Collections & Generics

---

## 📌 Notes

- Each book must have a unique title to allow easy lookup.
- Borrowing is only allowed if `available_copies > 0`.
- Returning increases available copies by 1.
- Data is stored permanently in the database.
