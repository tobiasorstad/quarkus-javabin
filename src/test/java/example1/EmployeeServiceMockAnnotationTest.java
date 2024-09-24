package example1;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.when;


@QuarkusTest
class EmployeeServiceMockAnnotationTest {

    @InjectMock
    EmployeeApi employeeApi;

    @Inject
    EmployeeService employeeService;

    @Test
    void testThatNotFoundExceptionGivesEmptyResponse(){
        when(employeeApi.getEmployee("404")).thenThrow(new NotFoundException());
        var result = employeeService.getEmployee("404");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testThatEmployeeIsReturned(){
        when(employeeApi.getEmployee("1")).thenReturn(new Employee("Test", "1"));
        var result = employeeService.getEmployee("1");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test", result.get().name);
    }

    @Test
    void testAddEmployeeDateIsAdded(){
        var result = employeeService.addEmployee(new Employee("Test", "1"));
        Assertions.assertEquals(LocalDate.now().toString(), result.dateCreated);
    }
}