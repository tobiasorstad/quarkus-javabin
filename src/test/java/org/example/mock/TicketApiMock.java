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


    @Override
    public void createTicket(Employee employee) {
    }
}
