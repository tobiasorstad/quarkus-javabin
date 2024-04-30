package org.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    public Employee getEmployee(String employeeNumber) {
        try {
            return employeeApi.getEmployee(employeeNumber);
        } catch (Exception e) {
            return null;
        }
    }
}
