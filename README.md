# Utilizing the power of Mockito with Quarkus

How often have you wanted to write an integration test for a piece of code that communicates with a lot of external parts, like APIs?
This is a case we as developers often face and something we need to handle. When testing a piece of code that communicates with an API,
we can connect to the actual API during the test, which makes your test vulnerable to downtime or changes in the API.
You could also set up your own temporary API that exists solely for testing purposes. This is a viable solution, but takes some effort.
A third solution, which is the topic of this blogpost, is mocking!

When testing a piece of code we want to isolate its behaviour. To achieve this goal it can be helpful to replace other parts of our code with simulations of their behaviour. 
We could then simulate a class responsible for sending a POST-request to an external API, instead of having to communicate with an actual API.
This way we are able to test the adjacent logic, like the code verifying that the data about to be sent is valid.

Depending on what tests we are running we might want our mocks to behave differently. This tutorial will go through some different approaches to mocking
using Quarkus and Mockito, and talk about some pros and cons with each of them. This tutorial is primarily aimed at developers who are familiar with writing tests in Java, but are new to mocking. 
Experience with Quarkus is helpful.

Feel free to clone the example repository and check out the code for yourself. Information about the needed dependencies can also be found there.
Having cloned the repository all you need is Java and Maven installed to run the tests with "mvn verify".

## The Mock annotation

Quarkus allows us to use the @Mock annotation, which will override a Bean. For example if we have a rest-client that interacts with an external API to fetch employees like this one:

```
@RegisterRestClient(configKey = "api")
public interface EmployeeApi {

    @GET
    @Path("employee/{employeeNumber}")
    Employee getEmployee(@PathParam("employeeNumber") String employeeNumber);

    @POST
    @Path("employee")
    Employee addEmployee(Employee employee);

}
```

We can create a mock implementation, that will override the rest client. It could look something like this:
```
@Mock
@ApplicationScoped
@RestClient
public class EmployeeApiMock implements EmployeeApi {

    @Override
    public Employee getEmployee(String employeeNr) {
        if(employeeNr.equals("404")){
            throw new NotFoundException("Could not find employee");
        }
        return new Employee("Test", employeeNr);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return employee;
    }

}
```

Note that when using the mock annotation, your mock implementation wil override the rest client in all the tests, unless they specifically inject their own mocked version.
If you want your mock to behave differently based on its input parameters, you would have to implement logic for that in the mock, which provides more complexity to the test,
and most importantly that logic affects your test, but is not located in the test itself, making it harder for someone reading your test to understand what is going on.

Now if we have a method in an EmployeeService class that we want to test:
```
@ApplicationScoped
public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    public Optional<Employee> getEmployee(String employeeNumber) {
        try {
            return Optional.of(employeeApi.getEmployee(employeeNumber));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
}
```

We could write some tests like this, and it would test that the NotFoundException given by employeeNumber "404" would result in an empty result.
But why the employee number of "404" should differ from the "1" might not be immediately clear for readers, as this information is contained in the EmployeeApiMock class. 
What we are really trying to test here is that an exception results in an Optional.empty() returned.

```
@QuarkusTest
class GetEmployeeUsingMockClassTest {

    @Inject
    EmployeeService employeeService;

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
```

On the other hand, if we always want the mock-implementation to behave the same way, or if we are testing some other part of the code
not directly tied to the mock, a Mock-class is a simple way to remove the need for an external dependency. 
Setting up a Mock-class requires few lines of code and can be a clean solution if the tests are not directly
tied to the Mock-implementation itself, like the example above.


## @InjectMock

Quarkus allows us to define the mock implementation per test. Instead of creating a separate mock implementation class, we define the mock inside the test class.
Quarkus documentation refers to the above method as the old approach, while this method is described as the new approach.
Additionally, we use argument matchers to define logic based on input parameters. Our mock class and test can both be replaced by the following test.

```
@QuarkusTest
public class GetEmployeeUsingInjectMockTest {

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
```

This setup provides the same result, but with the mock setup inside the test class itself. 
A big benefit of this is that whoever reads the tests can easily see what data is mocked. 
Argument matchers can be used to customize the mock response based on the parameters,
like we do in the test above by returning an employee with the same employeeNumber used in the query.
More information about the powers of argument matchers can be found here: https://www.baeldung.com/mockito-argument-matchers

## Verify

Sometimes we might have a piece of code that executes several other methods, like sending post requests to an API, without directly affecting the output of the method you are testing.
Mockito's verify function comes to the rescue. A small note: When writing tests we usually want to try to not couple our tests too tightly to the internal logic of our system.
If we make a change to the internal logic, we end up breaking the tests, even if the system still does what we want it do. Verify takes us somewhat into this territory,
so make sure to only use it when necessary.

When we create new employees in our system, we need to create tickets to notify IT or administrators that a new employee has started.
This ticket is created through a post request to an external API. The EmployeeService would have a method like this:

```
public Employee addEmployee(Employee employee) {
        if(isValidEmployee(employee)){
            ticketApi.createTicket(employee);
            return employeeApi.addEmployee(employee);
        }
        return null;
    }
    
public boolean isValidEmployee(Employee employee){
        return employee.name() != null && employee.employeeNumber() != null;
    }
```

We want to test that whenever we add a valid employee, we also create a ticket by calling the createTicket method of our ticketApi rest-client. This can be achieved by using Mockito's verify-method like in the following test:

```
@QuarkusTest
public class AddEmployeeUsingVerifyTest {

    @InjectMock
    @RestClient
    TicketApi ticketApi;

    @Inject
    EmployeeService employeeService;

    @Test
    void testAddEmployeeCreatesTicket() {
        Employee employee = new Employee("Mr. Test", "123");

        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(1)).createTicket(employee);
    }

    @Test
    void testAddInvalidEmployeeDoesNotCreateTicket() {
        Employee employee = new Employee(null, "123");

        employeeService.addEmployee(employee);

        Mockito.verify(ticketApi, Mockito.times(0)).createTicket(employee);
    }

}
```

## @InjectSpy

Now what if we want to mock a specific method in an injected class, but use the rest as is. This is where @InjectSpy comes in handy.
Imagine that in our EmployeeService class, we want to test its functionality, but mock the isValidEmployee method. We could do so like this: 

``` 
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
```

The functionality of the injected EmployeeService is unchanged, except for the isValidEmployee method,
that we override with the mock implementation. The injected spy is also compatible with verify, as a bonus. 
InjectSpy can be practical if there is a specific method that we do not care about in our test, but we want to test 
the rest of the injected class as is. You probably should not mock your validation methods, but the example above
simply illustrates how to use an injected spy.


## mockStatic

Mockito also allows us to mock static methods. In the example repo we utilize a class with a static method to verify if a Quarkus security identity contains a specific role.
This can be hard to test for, so it could be easier to mock this method. 
If we extend the getEmployee method to also check the user's credential like this:

```
public Employee getEmployeeAuth(String id) {
    if(UserAccess.isAdmin(securityIdentity)){
        return employeeApi.getEmployee(id);
    }
    else throw new ForbiddenException("You don't have access!");
}
```

With the UserAccess class looking like this:
```
public class UserAccess {

    public static boolean isAdmin(SecurityIdentity securityIdentity){
        return securityIdentity.hasRole("EmployeeAdmin");
    }
}
```

We can mock the call to isAdmin with Mockito's mockStatic function, demonstrated in this test:

```
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
            userAccessMock.when(() -> UserAccess.IsAdmin(any())).thenReturn(true);
            var result = employeeService.getEmployeeAuth("1");
            Assertions.assertEquals("Test", result.name());
        }
    }

    @Test
    void testGetEmployeeWhenNotAdmin(){
        try(var userAccessMock = Mockito.mockStatic(UserAccess.class)) {
            userAccessMock.when(() -> UserAccess.IsAdmin(any())).thenReturn(false);
            Assertions.assertThrows(ForbiddenException.class, () -> employeeService.getEmployeeAuth("1"));
        }
    }

}

```

Notice how we set up the mocking of the employeeApi in the same way as before. Mockstatic introduces some more lines of code to your test, 
so consider if what you are testing really needs to be static. Maybe the function could be refactored and tested in another way. 
In any case, mockStatic is there if there is a need to test static functionality.

## Conclusion

To wrap up, Mockito allows us to focus on system behaviour instead of dependencies. This tutorial has highlighted some of Mockito's functionalities that allows for creating efficient tests.
Remember, the ultimate goal is a reliable codebase that's easy to update and maintain. Keep experimenting with Mockito and Quarkus to power up your tests. Happy testing!


