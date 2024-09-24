package example1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@QuarkusTest
class EmployeeServiceMockClassTest {

    @Inject
    EmployeeService employeeService;

    @Test
    void testThatNotFoundExceptionGivesEmptyResponse(){
        var result = employeeService.getEmployee("404");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testThatEmployeeIsReturned(){
        var result = employeeService.getEmployee("1");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test", result.get().name);
    }

    @Test
    void testAddEmployeeDateIsAdded(){
        var result = employeeService.getEmployee("1");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(LocalDate.now().toString(), result.get().dateCreated);
    }
}