package org.example.mock;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.Employee;
import org.example.EmployeeApi;
import org.example.TicketApi;

@Mock
@ApplicationScoped
@RestClient
public class TicketApiMock implements TicketApi {

    /*
        This class is included simply to make sure all the tests run.
        It is never utilized in any test.
     */

    @Override
    public void createTicket(Employee employee) {
    }
}
