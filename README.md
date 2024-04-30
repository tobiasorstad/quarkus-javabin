# Utilizing the power of Mockito with Quarkus

How often have you wanted to write an integration test for a piece of code that communicates with a lot of external factors?
This is a case we as developers often face, and luckily there is a solution, mocking! Mockito is a tool that lets us mock parts of our code,
and depending on what tests we are running we might want our mocks to behave differently. This tutorial will go through different approaches to mocking,
using Quarkus and Mockito, and talk about some pros and cons with each of them.

This guide comes with an example repository, so feel free to clone it and check it out for yourself. Information about the needed dependencies can also be found there.

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
    void addEmployee(Employee employee);

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
    public void addEmployee(Employee employee) {
    }

}
```

Note that when using the mock annotation, your mock implementation wil override the rest client in all the tests, unless they specifically inject their own mocked version.
If you want your mock to behave differently based on its input parameters, you would have to implement logic for that in the mock, which provides more complexity to the test,
and most importantly that logic affects your test, but is not located in the test it self, making it harder for someone reading your test to understand what is going on.

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


## Using @InjectMock

Quarkus allows us to define the mock implementation per test. Instead of creating a separate mock implementation class, we define the mock inside the test class.
We use argument matchers to define logic based on input parameters. Our mock class and test can both be replaced by the following test.

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
A big benefit of this is that whoever reads the tests can easily see what data is mocked. Using argument matchers allows for customizing the mock based on the parameters. More information about the powers of argument matchers can be found here: https://www.baeldung.com/mockito-argument-matchers

## Using verify to make sure a piece of code is run

The above examples details how to use mockito to customize what your method returns based on the input, but what if you want to execute some different code entirely? Mockitos thenAnswer method comes to the rescue. Imagine your employeeService has a method for adding an employee, and you want to check that the employeeApi's postEmployee method is called with the same object that is passed to the employeeService bean. You could do something like this:

@Test
void testAddEmployee() {
List<Employee> postedEmployees = new ArrayList<>();
when(employeeApi.postEmployee(any())).thenAnswer(
invocation -> {
postedEmployees.add(invocation.getArgument(0));
return Response.OK();
});

        Employee employee = new Employee("Mr. Test");

        var result = employeeService.addEmployee(employee);

	Assertions.assertEquals(200, result.status());
	Assertions.assertEquals(1, postedEmployees.size());
	Assertions.assertEquals("Mr. Test", postedEmployees.get(0).getName();
    }

Here we use the thenAnswer method to fetch the arguments passed to postEmployee and define our own logic. We utilize a custom mock state to check that the object we pass to the employeeService is further passed along to the employeeApi.

mockStatic

Mockito also allows us to mock static methods. An example of this is if you have a method checking the Quarkus security identity of a user to determine if a user is an administrator. This can be hard to test for, so it could be easier to mock this method. If we have defined our method like the following:

public class EmployeeService {

    @Inject
    @RestClient
    EmployeeApi employeeApi;

    @Inject
    SecurityIdentity securityIdentity;

    public Employee getEmployee() {
	if(!UserAccess.isAdmin(securityIdentity)){
	    throw new ForbiddenException("No Access");
	}
        try {
            return employeeApi.getEmployee();
        } catch (Exception e) {
            return null;
        }
    }
}

And the following tests:

@Test
void testEmployeeIsFetchedWhileAdmin(){
try(var userAccessMock = Mockito.mockStatic(UserAccess.class)) {
userAccessMock.when(() -> UserAccess.isAdmin(any())).thenReturn(true);
Assertions.assertEquals(null, employeeService.getEmployee("404"));
}
}

@Test
void testForbiddenWhenNotAdmin(){
try(var userAccessMock = Mockito.mockStatic(UserAccess.class)) {
userAccessMock.when(() -> UserAccess.isAdmin(any())).thenReturn(false);
Assertions.assertThrows(ForbiddenException.class, () -> employeeService.getEmployee("404"));
}
}  	



