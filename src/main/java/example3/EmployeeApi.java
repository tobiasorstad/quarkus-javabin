package example3;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "employee-api")
public interface EmployeeApi {

    @POST
    @Path("employee")
    Employee addEmployee(Employee employee);

}
