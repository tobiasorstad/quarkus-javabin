package org.example;

import io.quarkus.security.ForbiddenException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class GetEmployeeUsingMockStaticTest {

    @Inject
    EmployeeService employeeService;

    @InjectMock
    @RestClient
    EmployeeApi employeeApi;

    @BeforeEach
    public void setup(){
        when(employeeApi.getEmployee(anyString())).thenAnswer(invocation -> new Employee("Test", invocation.getArgument(0)));
    }

    @Test
    void testGetEmployeeWhenAdmin(){
        try(var userAccessMock = Mockito.mockStatic(UserAccess.class)) {
            userAccessMock.when(() -> UserAccess.isAdmin(any())).thenReturn(true);
            var result = employeeService.getEmployeeAuth("1");
            Assertions.assertEquals("Test", result.name());
        }
    }

    @Test
    void testGetEmployeeWhenNotAdmin(){
        try(var userAccessMock = Mockito.mockStatic(UserAccess.class)) {
            userAccessMock.when(() -> UserAccess.isAdmin(any())).thenReturn(false);
            Assertions.assertThrows(ForbiddenException.class, () -> employeeService.getEmployeeAuth("1"));
        }
    }

}
