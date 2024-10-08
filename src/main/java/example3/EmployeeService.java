package example3;

import common.Employee;
import common.EmployeeApi;
import common.Ticket;
import common.TicketApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    @RestClient
    TicketApi ticketApi;

    @Inject
    Logger logger;

    public Employee addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
            Ticket ticket = ticketApi.createTicket(employee);
            logger.info("Ticket created with id " + ticket.id());
            return employeeApi.addEmployee(employee);
        }
        throw new BadRequestException();
    }

    public boolean isValidEmployee(Employee employee){
        if(employee == null) return false;
        return employee.name != null && employee.employeeNumber != null;
    }

}
