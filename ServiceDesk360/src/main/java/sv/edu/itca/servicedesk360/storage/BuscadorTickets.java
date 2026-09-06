package sv.edu.itca.servicedesk360.storage;

import java.util.List;
import sv.edu.itca.servicedesk360.model.TicketSoporte;

public interface BuscadorTickets {
    List<TicketSoporte> listarTodos();
}