package example2;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class VerifyTest {

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

            employeeService.addEmployee(employee);

            Mockito.verify(ticketApi, Mockito.never()).createTicket(employee);
        }


}
