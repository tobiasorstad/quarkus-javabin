package org.example;

public class EmployeeUtils {

    public static boolean EmployeeIsValid(Employee employee){
        return employee.name() != null && employee.employeeNumber() != null;
    }
}
