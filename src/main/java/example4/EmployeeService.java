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
    SecurityIdentity securityIdentity;

    public Employee getEmployeeAuth(String id) {
        if(UserAccess.isAdmin(securityIdentity)){
            return employeeApi.getEmployee(id);
        }
        else throw new ForbiddenException("You don't have access!");
    }

}
