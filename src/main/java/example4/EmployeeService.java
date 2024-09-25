package example4;

import common.Employee;
import common.EmployeeApi;
import common.Ticket;
import common.TicketApi;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @Inject
    SecurityIdentity securityIdentity;

    // Example 5
    public Employee getEmployeeAuth(String id) {
        if(UserAccess.isAdmin(securityIdentity)){
            return employeeApi.getEmployee(id);
        }
        else throw new ForbiddenException("You don't have access!");
    }

    // Example 6
    public Employee addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
            Ticket ticket = ticketApi.createTicket(employee);
            logger.info("Ticket created with id " + ticket.id());
            return employeeApi.addEmployee(employee);
        }
        return null;
    }

    public boolean isValidEmployee(Employee employee){
        if(employee == null) return false;
        return employee.name != null && employee.employeeNumber != null;
    }

}
