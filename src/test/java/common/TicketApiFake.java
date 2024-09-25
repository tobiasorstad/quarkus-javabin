package common;

import common.Ticket;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Mock
@ApplicationScoped
@RestClient
public class TicketApiFake implements TicketApi {

    @Override
    public Ticket createTicket(Employee employee) {
        return new Ticket("1");
    }
}
