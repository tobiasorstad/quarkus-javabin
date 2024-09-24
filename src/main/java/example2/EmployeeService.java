package example2;

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

    @Inject
    @RestClient
    TicketApi ticketApi;


    public Optional<Employee> getEmployee(String employeeNumber) {
        try {
            return Optional.of(employeeApi.getEmployee(employeeNumber));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Employee addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
            ticketApi.createTicket(employee);
            return employeeApi.addEmployee(employee);
        }
        return null;
    }

    public boolean isValidEmployee(Employee employee){
        if(employee == null) return false;
        return employee.name != null && employee.employeeNumber != null;
    }

}
