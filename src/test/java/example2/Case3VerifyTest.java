package example2;

import common.Employee;
import common.EmployeeApi;
import common.TicketApi;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class Case3VerifyTest {

        @InjectMock
        @RestClient
        TicketApi ticketApi;

        @InjectMock
        @RestClient
        EmployeeApi employeeApi;

        @Inject
        EmployeeService employeeService;

        @Test
        void testAddEmployeeCreatesTicket() {
            when(employeeApi.addEmployee(any())).thenAnswer(invocation -> invocation.getArgument(0));
            Employee employee = new Employee("Mr. Test", "123");

            employeeService.addEmployee(employee);

            Mockito.verify(ticketApi, Mockito.times(1)).createTicket(employee);
        }

        @Test
        void testAddInvalidEmployeeDoesNotCreateTicket() {
            when(employeeApi.addEmployee(any())).thenAnswer(invocation -> invocation.getArgument(0));
            Employee employee = new Employee(null, "123");

            Assertions.assertThrows(BadRequestException.class, () ->employeeService.addEmployee(employee));

            Mockito.verify(ticketApi, Mockito.never()).createTicket(employee);
        }


}
