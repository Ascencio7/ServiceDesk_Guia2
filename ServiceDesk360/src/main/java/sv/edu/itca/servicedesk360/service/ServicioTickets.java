package sv.edu.itca.servicedesk360.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.servicedesk360.model.PrioridadTicket;
import sv.edu.itca.servicedesk360.model.Seguimiento;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;
import sv.edu.itca.servicedesk360.model.TicketSoporte;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.persistence.ConexionBD;
import sv.edu.itca.servicedesk360.storage.BuscadorTickets;
import sv.edu.itca.servicedesk360.storage.DirectorioTicketsJDBC;
import sv.edu.itca.servicedesk360.storage.EquipoOpcion;
import sv.edu.itca.servicedesk360.storage.OpcionCatalogo;
import sv.edu.itca.servicedesk360.storage.RegistradorTickets;
import sv.edu.itca.servicedesk360.storage.SeguimientoDAO;
import sv.edu.itca.servicedesk360.storage.TicketDAO;

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

    public List<OpcionCatalogo> listarClientes() {
        return almacenamientoJDBC().listarClientesActivos();
    }

    public List<OpcionCatalogo> listarCategorias() {
        return almacenamientoJDBC().listarCategorias();
    }

    public List<OpcionCatalogo> listarTecnicos() {
        return almacenamientoJDBC().listarTecnicosActivos();
    }

    public List<OpcionCatalogo> listarEquiposPorCliente(long idCliente) {
        return almacenamientoJDBC().listarEquiposPorCliente(idCliente);
    }

    public long registrarTicketConSeguimiento(long idCliente, long idEquipo, long idCategoria,
            long idTecnico, String titulo, String descripcion, String prioridad,
            String detalleInicial) {
        validarRelacionado(idCliente, idEquipo, idCategoria, idTecnico, titulo,
                descripcion, prioridad, detalleInicial);
        DirectorioTicketsJDBC jdbc = almacenamientoJDBC();
        if (!jdbc.existeClienteActivo(idCliente)) throw new IllegalArgumentException("El cliente no existe o esta inactivo.");
        if (!jdbc.perteneceACliente(idEquipo, idCliente)) throw new IllegalArgumentException("El equipo seleccionado no pertenece al cliente.");
        if (!jdbc.existeCategoria(idCategoria)) throw new IllegalArgumentException("La categoria no existe.");
        if (!jdbc.existeTecnicoActivo(idTecnico)) throw new IllegalArgumentException("El tecnico no existe o esta inactivo.");

        try (Connection cn = ConexionBD.abrir()) {
            cn.setAutoCommit(false);
            try {
                long idTicket = ((TicketDAO) jdbc).registrar(cn, idCliente, idEquipo, idCategoria,
                        idTecnico, titulo.trim(), descripcion.trim(), prioridad.toUpperCase());
                Seguimiento seguimiento = new Seguimiento();
                seguimiento.setIdTicket(idTicket);
                seguimiento.setDetalle(detalleInicial.trim());
                if (((SeguimientoDAO) jdbc).registrar(cn, seguimiento) != 1) {
                    throw new IllegalStateException("No se registro el seguimiento inicial.");
                }
                cn.commit();
                return idTicket;
            } catch (Exception ex) {
                try { cn.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw new IllegalStateException("La operacion fue revertida.", ex);
            } finally {
                try { cn.setAutoCommit(true); } catch (SQLException ignored) { }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible completar el registro del ticket.", ex);
        }
    }

    public List<TicketDetalleDTO> listarDetalle(String estado, String prioridad, int pagina, int limite) {
        int paginaSegura = Math.max(1, pagina);
        int limiteSeguro = Math.max(1, limite);
        return ((TicketDAO) almacenamientoJDBC()).listarDetalle(normalizarFiltro(estado),
                normalizarFiltro(prioridad), limiteSeguro, (paginaSegura - 1) * limiteSeguro);
    }

    public int contarDetalle(String estado, String prioridad) {
        return ((TicketDAO) almacenamientoJDBC()).contar(normalizarFiltro(estado), normalizarFiltro(prioridad));
    }

    public void actualizarEstadoOptimista(Usuario usuario, long ticketId, String esperado, String nuevo) {
        comprobarSoporte(usuario);
        validarEstado(nuevo);
        if (!validarEstadoPermitido(esperado)) throw new IllegalArgumentException("Estado esperado no valido.");
        if (!((TicketDAO) almacenamientoJDBC()).actualizarEstado(ticketId, esperado, nuevo)) {
            throw new IllegalStateException("El ticket fue modificado por otra operacion. Recargue la pantalla.");
        }
    }

    private void validarRelacionado(long cliente, long equipo, long categoria, long tecnico,
            String titulo, String descripcion, String prioridad, String detalle) {
        if (cliente <= 0) throw new IllegalArgumentException("Cliente requerido.");
        if (equipo <= 0) throw new IllegalArgumentException("Equipo requerido.");
        if (categoria <= 0) throw new IllegalArgumentException("Categoria requerida.");
        if (tecnico <= 0) throw new IllegalArgumentException("Tecnico requerido.");
        if (titulo == null || titulo.trim().length() < 5 || titulo.trim().length() > 120) throw new IllegalArgumentException("El titulo debe contener entre 5 y 120 caracteres.");
        if (descripcion == null || descripcion.trim().length() < 10) throw new IllegalArgumentException("La descripcion debe contener al menos 10 caracteres.");
        if (!"BAJA".equalsIgnoreCase(prioridad) && !"MEDIA".equalsIgnoreCase(prioridad)
                && !"ALTA".equalsIgnoreCase(prioridad) && !"CRITICA".equalsIgnoreCase(prioridad)) throw new IllegalArgumentException("Prioridad no valida.");
        if (detalle == null || detalle.trim().isEmpty()) throw new IllegalArgumentException("El seguimiento inicial es obligatorio.");
    }

    private String normalizarFiltro(String valor) {
        return valor == null || valor.trim().isEmpty() ? null : valor.trim().toUpperCase();
    }

    private boolean validarEstadoPermitido(String estado) {
        return "ABIERTO".equals(estado) || "ASIGNADO".equals(estado)
                || "EN_PROCESO".equals(estado) || "CERRADO".equals(estado);
    }

    private void validarEstado(String estado) {
        if (!validarEstadoPermitido(estado)) throw new IllegalArgumentException("Estado de ticket no valido.");
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
