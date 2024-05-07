package org.example;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@QuarkusTest
class GetEmployeeUsingMockClassTest {

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
    }
}