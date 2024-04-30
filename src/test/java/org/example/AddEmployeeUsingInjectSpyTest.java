package org.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AddEmployeeUsingInjectSpyTest {

    @InjectSpy
    EmployeeService employeeService;

    @Test
    void testAddEmployee() {
        Employee employee = new Employee(null, "123");
        when(employeeService.isValidEmployee(employee)).thenReturn(true);

        var result = employeeService.addEmployee(employee);

        Assertions.assertNotNull(result);
        Mockito.verify(employeeService, Mockito.times(1)).isValidEmployee(employee);
    }

    @Test
    void testAddEmployeeDoesNotCallAPIIfInvalid() {
        Employee employee = new Employee(null, "123");
        when(employeeService.isValidEmployee(employee)).thenReturn(false);

        var result = employeeService.addEmployee(employee);

        Assertions.assertNull(result);
        Mockito.verify(employeeService, Mockito.times(1)).isValidEmployee(employee);
    }

}
