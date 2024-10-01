package example1;

import common.Employee;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;


@QuarkusTest
class Case1MockClassTest {

    @Inject
    EmployeeService employeeService;

    @Test
    void testThatNotFoundExceptionGivesEmptyResponse(){
        Optional<Employee> result = employeeService.getEmployee("404");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testThatEmployeeIsReturned(){
        Optional<Employee> result = employeeService.getEmployee("1");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Test", result.get().name);
    }

    @Test
    void testAddEmployeeDateIsAdded(){
        Employee result = employeeService.addEmployee(new Employee("test", "1"));

        Assertions.assertEquals(LocalDate.now().toString(), result.dateCreated);
    }
}