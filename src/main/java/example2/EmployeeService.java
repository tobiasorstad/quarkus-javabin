package example2;

import common.Employee;
import common.EmployeeApi;
import common.TicketApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    @RestClient
    TicketApi ticketApi;

    public Employee addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
            ticketApi.createTicket(employee);
            return employeeApi.addEmployee(employee);
        }
        throw new BadRequestException();
    }

    public boolean isValidEmployee(Employee employee){
        if(employee == null) return false;
        return employee.name != null && employee.employeeNumber != null;
    }

}
