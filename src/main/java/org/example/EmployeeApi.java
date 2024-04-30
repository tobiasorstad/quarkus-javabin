package org.example;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "api")
@Consumes("application/json")
@Produces("application/json")
public interface EmployeeApi {

    @GET
    @Path("employee/{employeeNumber}")
    Employee getEmployee(@PathParam("employeeNumber") String employeeNumber);

    @POST
    @Path("employee")
    void addEmployee(Employee employee);

}
