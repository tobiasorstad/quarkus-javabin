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
            Employee validEmployee = new Employee("Mr. Test", "123");

            employeeService.addEmployee(validEmployee);

            Mockito.verify(ticketApi, Mockito.times(1)).createTicket(validEmployee);
        }

        @Test
        void testAddInvalidEmployeeDoesNotCreateTicket() {
            when(employeeApi.addEmployee(any())).thenAnswer(invocation -> invocation.getArgument(0));
            Employee invalidEmployee = new Employee(null, "123");

            Assertions.assertThrows(BadRequestException.class, () ->employeeService.addEmployee(invalidEmployee));

            Mockito.verify(ticketApi, Mockito.never()).createTicket(invalidEmployee);
        }


}
