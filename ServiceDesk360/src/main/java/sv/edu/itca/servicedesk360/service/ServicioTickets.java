package sv.edu.itca.servicedesk360.service;

import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.servicedesk360.model.PrioridadTicket;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.TicketSoporte;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.storage.BuscadorTickets;
import sv.edu.itca.servicedesk360.storage.DirectorioTicketsJDBC;
import sv.edu.itca.servicedesk360.storage.EquipoOpcion;
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
        return crear(usuario, titulo, descripcion, prioridadTexto, null);
        }

        public List<String> crear(Usuario usuario, String titulo, String descripcion,
            String prioridadTexto, Long equipoId) {
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
        ticket.seleccionarEquipo(equipoId);
        registrador.guardar(ticket);
        return errores;
    }

    public List<EquipoOpcion> listarEquipos(Usuario usuario) {
        if (!(usuario instanceof Solicitante) || !(buscador instanceof DirectorioTicketsJDBC)) {
            return new ArrayList<>();
        }
        return ((DirectorioTicketsJDBC) buscador).listarEquipos(usuario.getCorreo());
    }

    public void registrarEquipo(Usuario usuario, String codigo, String tipo,
            String marca, String modelo) {
        if (!(usuario instanceof Solicitante)) {
            throw new IllegalStateException("Solo un solicitante puede registrar equipos.");
        }
        if (codigo == null || codigo.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo y tipo son obligatorios.");
        }
        if (!(registrador instanceof DirectorioTicketsJDBC)) {
            throw new IllegalStateException("El almacenamiento JDBC no esta configurado.");
        }
        ((DirectorioTicketsJDBC) registrador).registrarEquipo(
                usuario.getNombreCompleto(), usuario.getCorreo(), codigo.trim(), tipo.trim(),
                limpiar(marca), limpiar(modelo));
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public List<TicketSoporte> listar() {
        return buscador.listarTodos();
    }

    public List<TicketSoporte> listarPara(Usuario usuario) {
        List<TicketSoporte> tickets = listar();
        if (!(usuario instanceof Solicitante)) {
            return tickets;
        }
        long solicitanteId = usuario.getId();
        List<TicketSoporte> propios = new ArrayList<>();
        for (TicketSoporte ticket : tickets) {
                if (ticket.getSolicitante().getId() == solicitanteId
                    || ticket.getSolicitante().getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                propios.add(ticket);
            }
        }
        return propios;
    }

    public void actualizarEstado(Usuario usuario, long ticketId, String estado) {
        comprobarSoporte(usuario);
        if (!"ABIERTO".equals(estado) && !"ASIGNADO".equals(estado)
                && !"EN_PROCESO".equals(estado) && !"CERRADO".equals(estado)) {
            throw new IllegalArgumentException("Estado de ticket no valido.");
        }
        almacenamientoJDBC().actualizarEstado(ticketId, estado);
    }

    public void eliminar(Usuario usuario, long ticketId) {
        comprobarSoporte(usuario);
        almacenamientoJDBC().eliminar(ticketId);
    }

    private void comprobarSoporte(Usuario usuario) {
        if (usuario == null || usuario.getRol().name().equals("SOLICITANTE")) {
            throw new IllegalStateException("El solicitante no puede gestionar tickets.");
        }
    }

    private DirectorioTicketsJDBC almacenamientoJDBC() {
        if (!(registrador instanceof DirectorioTicketsJDBC)) {
            throw new IllegalStateException("El almacenamiento JDBC no esta configurado.");
        }
        return (DirectorioTicketsJDBC) registrador;
    }
}
