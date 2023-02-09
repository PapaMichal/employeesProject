package com.example.employeesassignment.database;

public enum CreateTableSql {
    EXPERTISE_FIELDS("CREATE TABLE IF NOT EXISTS ExpertiseFields(id INTEGER PRIMARY KEY AUTOINCREMENT, fieldName TEXT UNIQUE);"),
    EMPLOYEES(
            "CREATE TABLE Employees(" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "  name TEXT NOT NULL, " +
                    "  address TEXT, "+
                    "  education TEXT NOT NULL, " +
                    "  employment_rate INTEGER NOT NULL CHECK(employment_rate > 0), " +
                    "  hourly_pay INTEGER CHECK(hourly_pay > 0), " +
                    "  picture BLOB);"),
    EMPLOYEE_EXPERTISE(
            "CREATE TABLE IF NOT EXISTS EmployeesExpertise (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  expertise_id INTEGER NOT NULL REFERENCES ExpertiseFields(id) ON DELETE CASCADE," +
                    "  employee_id INTEGER NOT NULL REFERENCES Employees(id) ON DELETE CASCADE," +
                    "  UNIQUE(expertise_id, employee_id)" +
                    ");"),
    USERS("CREATE TABLE Users(username text PRIMARY KEY NOT NULL, pass TEXT NOT NULL, email text NOT NULL UNIQUE)");
    private final String sql;

    CreateTableSql(final String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}