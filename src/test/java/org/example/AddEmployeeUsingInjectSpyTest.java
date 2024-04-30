package org.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AddEmployeeUsingInjectSpyTest {

    @InjectMock
    @RestClient
    EmployeeApi employeeApi;

    @InjectSpy
    EmployeeService employeeService;

    @Test
    void testAddEmployee() {
        Employee employee = new Employee(null, "123");
        when(employeeService.isValidEmployee(employee)).thenReturn(true);

        employeeService.addEmployee(employee);

        Mockito.verify(employeeApi, Mockito.times(1)).addEmployee(employee);
        Mockito.verify(employeeService, Mockito.times(1)).isValidEmployee(employee);
    }


}
