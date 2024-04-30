package org.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class AddEmployeeUsingVerifyTest {

    @InjectMock
    @RestClient
    TicketApi ticketApi;

    @Inject
    EmployeeService employeeService;

    @Test
    void testAddEmployeeCreatesTicket() {
        Employee employee = new Employee("Mr. Test", "123");

        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(1)).createTicket(employee);
    }

    @Test
    void testAddInvalidEmployeeDoesNotCreateTicket() {
        Employee employee = new Employee(null, "123");

        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(0)).createTicket(employee);
    }

}
