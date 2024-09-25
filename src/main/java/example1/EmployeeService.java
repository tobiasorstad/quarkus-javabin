package example1;

import common.Employee;
import common.EmployeeApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
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

    public Employee addEmployee(Employee employee) {
        employee.dateCreated = LocalDate.now().toString();
        return employeeApi.addEmployee(employee);
    }

}
