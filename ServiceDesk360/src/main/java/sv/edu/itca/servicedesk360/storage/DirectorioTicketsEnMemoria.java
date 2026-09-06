package sv.edu.itca.servicedesk360.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import sv.edu.itca.servicedesk360.model.TicketSoporte;

public class DirectorioTicketsEnMemoria implements BuscadorTickets, RegistradorTickets {

    private final List<TicketSoporte> tickets = new CopyOnWriteArrayList<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public void guardar(TicketSoporte ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket es obligatorio.");
        }
        tickets.add(ticket);
    }

    @Override
    public long siguienteId() {
        return secuencia.incrementAndGet();
    }

    @Override
    public List<TicketSoporte> listarTodos() {
        List<TicketSoporte> copia = new ArrayList<>(tickets);
        copia.sort(Comparator.comparing(TicketSoporte::getFechaCreacion).reversed());
        return copia;
    }
}
