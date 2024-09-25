package example3;

import common.Employee;
import common.TicketApi;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class Case4FailingTest {

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

}
