package sv.edu.itca.servicedesk360.storage;

import sv.edu.itca.servicedesk360.model.TicketSoporte;

public interface RegistradorTickets {
    void guardar(TicketSoporte ticket);
    long siguienteId();
}