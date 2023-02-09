package com.example.employeesassignment.database;

public enum Table {
    USERS("Users"),
    EXPERTISE_FIELDS("ExpertiseFields"),
    EMPLOYEES("Employees"),
    EMPLOYEE_EXPERTISE("EmployeeExperience"),
    ;
    private String name;
    Table(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
