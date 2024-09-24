package example2;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "ticket-api")
public interface TicketApi {

    @POST
    @Path("ticket")
    void createTicket(Employee employee);

}
