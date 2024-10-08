package example1;

import common.Employee;
import common.EmployeeApi;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@QuarkusTest
class Case2InjectMock {

    @InjectMock
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    EmployeeService employeeService;

    @Test
    void testThatNotFoundExceptionGivesEmptyResponse(){
        when(employeeApi.getEmployee("404")).thenThrow(new NotFoundException());

        Optional<Employee> result = employeeService.getEmployee("404");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testThatEmployeeIsReturned(){
        when(employeeApi.getEmployee("1")).thenReturn(new Employee("Test", "1"));

        Optional<Employee> result = employeeService.getEmployee("1");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test", result.get().name);
    }

    @Test
    void testAddEmployeeDateIsAdded(){
        when(employeeApi.addEmployee(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.addEmployee(new Employee("Test", "1"));
        Assertions.assertEquals("Test", result.name);
        Assertions.assertEquals(LocalDate.now().toString(), result.dateCreated);
    }
}