package example1;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "api")
public interface EmployeeApi {

    @GET
    @Path("employee/{employeeNumber}")
    Employee getEmployee(@PathParam("employeeNumber") String employeeNumber);

    @POST
    @Path("employee")
    Employee addEmployee(Employee employee);

}
