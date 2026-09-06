package sv.edu.itca.servicedesk360.service;

import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.servicedesk360.model.PrioridadTicket;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.TicketSoporte;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.storage.BuscadorTickets;
import sv.edu.itca.servicedesk360.storage.RegistradorTickets;

public class ServicioTickets {

    private final BuscadorTickets buscador;
    private final RegistradorTickets registrador;

    public ServicioTickets(BuscadorTickets buscador, RegistradorTickets registrador) {
        this.buscador = buscador;
        this.registrador = registrador;
    }

    public List<String> crear(Usuario usuario, String titulo, String descripcion,
            String prioridadTexto) {
        List<String> errores = new ArrayList<>();
        String tituloLimpio = titulo == null ? "" : titulo.trim();
        String descripcionLimpia = descripcion == null ? "" : descripcion.trim();
        if (!(usuario instanceof Solicitante)) {
            errores.add("Solo un solicitante puede abrir tickets.");
        }
        if (tituloLimpio.length() < 5) {
            errores.add("El título debe contener al menos 5 caracteres.");
        }
        if (descripcionLimpia.length() < 10) {
            errores.add("La descripción debe contener al menos 10 caracteres.");
        }

        PrioridadTicket prioridad = null;
        try {
            prioridad = PrioridadTicket.valueOf(
                    prioridadTexto == null ? "" : prioridadTexto.toUpperCase());
        } catch (IllegalArgumentException ex) {
            errores.add("Seleccione una prioridad válida.");
        }
        if (!errores.isEmpty()) {
            return errores;
        }

        TicketSoporte ticket = new TicketSoporte(registrador.siguienteId(),
                tituloLimpio, descripcionLimpia, (Solicitante) usuario, prioridad);
        registrador.guardar(ticket);
        return errores;
    }

    public List<TicketSoporte> listar() {
        return buscador.listarTodos();
    }
}
