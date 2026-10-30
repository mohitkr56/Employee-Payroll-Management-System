# Employee Payroll Management System

A desktop-based Employee Payroll Management System developed using Core Java, Java Swing, JDBC, and Oracle Database. The application helps manage employee records, generate payroll, calculate salaries, and view payroll reports through a graphical user interface.

## Features

- **Dashboard:** View payroll statistics, employee count, average salary, and recent payroll records.
- **Employee Management:** Add, update, delete, search, and manage employee records.
- **Payroll Management:** Generate employee payroll with salary calculations.
- **Salary Calculation:** Calculate gross salary, deductions, and net salary.
- **Duplicate Prevention:** Prevent duplicate payroll generation for the same employee and month.
- **Payroll History:** View employee payroll records.
- **Reports:** Search, sort, filter, and export payroll records to CSV.
- **Oracle Database Integration:** Store and retrieve application data using JDBC.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Desktop GUI |
| JDBC | Database connectivity |
| Oracle Database | Data storage |
| Oracle JDBC Driver | Oracle connectivity |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control |

## Project Structure

```text
PayrollSystem/
│
├── lib/
│   └── ojdbc11.jar
│
├── src/
│   ├── Main.java
│   ├── MainFrame.java
│   ├── DBConnection.java
│   ├── Employee.java
│   ├── EmployeeDAO.java
│   ├── EmployeePanel.java
│   ├── Payroll.java
│   ├── PayrollDAO.java
│   ├── PayrollPanel.java
│   ├── DashboardDAO.java
│   ├── ReportsPanel.java
│   └── TestConnection.java
│
├── .gitignore
└── README.md
```

## Database Configuration

This project uses Oracle Database.

- Database: Oracle
- Service Name: FREEPDB1
- Schema: PAYROLL

Configure your Oracle database connection in `DBConnection.java` before running the application.

Make sure the required database tables and constraints are created before using the application.

## Installation and Setup

1. Clone the repository:

```bash
git clone https://github.com/mohitkr56/Employee-Payroll-Management-System.git
```

2. Open the project in IntelliJ IDEA.

3. Configure the Oracle Database connection.

4. Add the Oracle JDBC driver (`ojdbc11.jar`) to the project classpath.

5. Verify that Oracle Database is running.

6. Run the application using:

```text
Main.java
```

## Application Modules

- Dashboard
- Employee Management
- Payroll Management
- Payroll Reports

## Developer

**Anand Kumar Saha**

Java | Java Swing | JDBC | Oracle Database

GitHub: https://github.com/mohitkr56

---

*Developed as a Java desktop application project for employee and payroll management.*
