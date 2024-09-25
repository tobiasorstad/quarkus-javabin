package example3;

import common.Employee;
import common.TicketApi;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class Case4InjectSpyTest {

    @InjectSpy
    @RestClient
    TicketApi ticketApi;

    @InjectSpy
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

    @Test
    void testAddInvalidEmployeeDoesCreateTicket() {
        when(employeeService.isValidEmployee(any())).thenReturn(true);
        Employee employee = new Employee(null, "123");

        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(1)).createTicket(employee);
    }

}
