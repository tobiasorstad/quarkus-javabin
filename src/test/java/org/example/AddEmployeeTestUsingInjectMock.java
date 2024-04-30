package org.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AddEmployeeTestUsingInjectMock {

    @InjectMock
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    EmployeeService employeeService;

    @Test
    void testAddEmployee() {
        List<Employee> postedEmployees = new ArrayList<>();
        doAnswer(invocation ->
            postedEmployees.add(invocation.getArgument(0))
        ).when(employeeApi).addEmployee(any());

        Employee employee = new Employee("Mr. Test", "123");

        employeeService.addEmployee(employee);
        Assertions.assertEquals(1, postedEmployees.size());
        Assertions.assertEquals("Mr. Test", postedEmployees.get(0).name());
    }


}
