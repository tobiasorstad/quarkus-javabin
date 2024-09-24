package example1;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Mock
@ApplicationScoped
@RestClient
public class EmployeeApiFake implements EmployeeApi {

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
