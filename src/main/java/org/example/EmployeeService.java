package org.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    public Optional<Employee> getEmployee(String employeeNumber) {
        try {
            return Optional.of(employeeApi.getEmployee(employeeNumber));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
           employeeApi.addEmployee(employee);
        }
    }

    private boolean isValidEmployee(Employee employee){
        return employee.name() != null && employee.employeeNumber() != null;
    }
}
