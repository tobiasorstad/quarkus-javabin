package org.example;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    SecurityIdentity securityIdentity;

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

    public Employee getEmployeeAuth(String id) {
        if(UserAccess.IsAdmin(securityIdentity)){
            return employeeApi.getEmployee(id);
        }
        else throw new ForbiddenException("You don't have access!");

    }

    public boolean isValidEmployee(Employee employee){
        return employee.name() != null && employee.employeeNumber() != null;
    }





}
