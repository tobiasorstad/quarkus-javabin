package org.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class AddEmployeeUsingMockStaticTest {


    @Inject
    EmployeeService employeeService;

    @Test
    void testAddEmployeeWhenValid(){
        try(var userAccessMock = Mockito.mockStatic(EmployeeUtils.class)) {
            userAccessMock.when(() -> EmployeeUtils.EmployeeIsValid(any())).thenReturn(true);

            Assertions.assertTrue(employeeService.employeeIsValid(new Employee(null, "123")));
        }
    }

    @Test
    void testAddEmployeeWhenInValid(){
        try(var userAccessMock = Mockito.mockStatic(EmployeeUtils.class)) {
            userAccessMock.when(() -> EmployeeUtils.EmployeeIsValid(any())).thenReturn(false);

            Assertions.assertFalse(employeeService.employeeIsValid(new Employee("Test", "123")));
        }
    }

}
