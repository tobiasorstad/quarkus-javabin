package example3;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Mock
@ApplicationScoped
@RestClient
public class EmployeeApiFake implements EmployeeApi {

    @Override
    public Employee addEmployee(Employee employee) {
        return employee;
    }

}
