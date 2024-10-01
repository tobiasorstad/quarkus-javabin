package example3;

import common.Employee;
import common.Ticket;
import common.TicketApi;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class Case5ArgumentCaptorTest {

    @InjectMock
    @RestClient
    TicketApi ticketApi;

    @Inject
    EmployeeService employeeService;

    ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);

    @Test
    void testTicketIsCreatedWithTheCorrectEmployee(){
        when(ticketApi.createTicket(any())).thenReturn(new Ticket("1"));
        Employee employee = new Employee("Test", "1");
        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(1)).createTicket(argumentCaptor.capture());
        Assertions.assertEquals("Test", argumentCaptor.getValue().name);
    }


}
