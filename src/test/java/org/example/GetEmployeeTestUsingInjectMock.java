package org.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import jakarta.ws.rs.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

@QuarkusTest
public class GetEmployeeTestUsingInjectMock {

    @InjectMock
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    EmployeeService employeeService;

    @BeforeEach
    public void setup(){
        when(employeeApi.getEmployee(anyString())).thenAnswer(invocation -> new Employee("Test", invocation.getArgument(0)));
        when(employeeApi.getEmployee("404")).thenThrow(new NotFoundException("Could not find Employee"));
    }

    @Test
    void testThat404GivesNull(){
        var result = employeeService.getEmployee("404");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testThatEmployeeIsReturned(){
        var result = employeeService.getEmployee("1");
        Assertions.assertTrue(result.isPresent());
    }

}
