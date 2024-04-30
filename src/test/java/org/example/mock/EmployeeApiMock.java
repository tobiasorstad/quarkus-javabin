package org.example.mock;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.Employee;
import org.example.EmployeeApi;

@Mock
@ApplicationScoped
@RestClient
public class EmployeeApiMock implements EmployeeApi {

    @Override
    public Employee getEmployee(String employeeNr) {
        if(employeeNr.equals("404")){
            throw new NotFoundException("Could not find employee");
        }
        return new Employee("Test", employeeNr);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return employee;
    }

}
