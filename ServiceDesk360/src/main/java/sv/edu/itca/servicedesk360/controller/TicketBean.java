package sv.edu.itca.servicedesk360.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

// import javax.faces.view.ViewScoped;
// import javax.inject.Named;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import sv.edu.itca.servicedesk360.model.Tecnico;
import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.service.ServicioTickets;
import sv.edu.itca.servicedesk360.storage.OpcionCatalogo;

@ManagedBean(name = "ticketBean")
@ViewScoped
public class TicketBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<TicketDetalleDTO> tickets = Collections.emptyList();
    private List<TicketDetalleDTO> ticketsFiltrados;

    private List<OpcionCatalogo> clientesDisponibles = Collections.emptyList();
    private List<OpcionCatalogo> equiposDisponibles = Collections.emptyList();
    private List<OpcionCatalogo> categoriasDisponibles = Collections.emptyList();
    private List<OpcionCatalogo> tecnicosDisponibles = Collections.emptyList();

    private boolean editando;
    private Long idTicketSeleccionado;
    private Long clienteSeleccionadoId;
    private Long equipoSeleccionadoId;
    private Long categoriaSeleccionadaId;
    private Long tecnicoSeleccionadoId;
    private String titulo;
    private String descripcion;
    private String prioridadSeleccionada;
    private String detalleInicial;
    private String estadoSeleccionado;

    @PostConstruct
    public void init() {
        cargar();
        cargarCatalogos();
        abrirNuevo();
    }

    public void cargar() {
        try {
            tickets = servicio().listarDetalle(null, null, 1, 1000);
        } catch (IllegalStateException ex) {
            ServletContext contexto = contexto();
            contexto.log("No fue posible cargar el listado JSF de tickets.", ex);
            tickets = Collections.emptyList();
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No fue posible cargar los tickets.");
        }
    }

    public void cargarCatalogos() {
        try {
            clientesDisponibles = servicio().listarClientes();
            categoriasDisponibles = servicio().listarCategorias();
            tecnicosDisponibles = servicio().listarTecnicos();
        } catch (Exception ex) {
            contexto().log("Error al cargar catálogos en TicketBean.", ex);
        }
    }

    public void onClienteChange() {
        if (clienteSeleccionadoId != null && clienteSeleccionadoId > 0) {
            try {
                equiposDisponibles = servicio().listarEquiposPorCliente(clienteSeleccionadoId);
            } catch (Exception ex) {
                equiposDisponibles = Collections.emptyList();
            }
        } else {
            equiposDisponibles = Collections.emptyList();
        }
        equipoSeleccionadoId = null;
    }

    public void abrirNuevo() {
        this.editando = false;
        this.idTicketSeleccionado = null;
        this.clienteSeleccionadoId = null;
        this.equipoSeleccionadoId = null;
        this.equiposDisponibles = Collections.emptyList();
        this.categoriaSeleccionadaId = null;
        this.tecnicoSeleccionadoId = null;
        this.titulo = "";
        this.descripcion = "";
        this.prioridadSeleccionada = "MEDIA";
        this.detalleInicial = "";
        this.estadoSeleccionado = "ABIERTO";
    }

    public void abrirEditar(TicketDetalleDTO t) {
        if (t == null) return;
        this.editando = true;
        this.idTicketSeleccionado = t.getIdTicket();
        this.clienteSeleccionadoId = t.getIdCliente();
        if (clienteSeleccionadoId != null && clienteSeleccionadoId > 0) {
            try {
                this.equiposDisponibles = servicio().listarEquiposPorCliente(clienteSeleccionadoId);
            } catch (Exception ex) {
                this.equiposDisponibles = Collections.emptyList();
            }
        } else {
            this.equiposDisponibles = Collections.emptyList();
        }
        this.equipoSeleccionadoId = t.getIdEquipo();
        this.categoriaSeleccionadaId = t.getIdCategoria();
        this.tecnicoSeleccionadoId = t.getIdTecnico();
        this.titulo = t.getTitulo();
        this.descripcion = t.getDescripcion();
        this.prioridadSeleccionada = t.getPrioridad();
        this.estadoSeleccionado = t.getEstado();
        this.detalleInicial = "";
    }

    public void guardarTicket() {
        try {
            if (clienteSeleccionadoId == null || clienteSeleccionadoId <= 0) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "Debe seleccionar un cliente.");
                return;
            }
            if (equipoSeleccionadoId == null || equipoSeleccionadoId <= 0) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "Debe seleccionar un equipo.");
                return;
            }
            if (categoriaSeleccionadaId == null || categoriaSeleccionadaId <= 0) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "Debe seleccionar una categoría.");
                return;
            }
            if (tecnicoSeleccionadoId == null || tecnicoSeleccionadoId <= 0) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "Debe seleccionar un técnico.");
                return;
            }
            if (titulo == null || titulo.trim().length() < 5) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "El título debe contener al menos 5 caracteres.");
                return;
            }
            if (descripcion == null || descripcion.trim().length() < 10) {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "La descripción debe contener al menos 10 caracteres.");
                return;
            }

            if (!editando) {
                if (detalleInicial == null || detalleInicial.trim().isEmpty()) {
                    agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", "El seguimiento inicial es obligatorio.");
                    return;
                }
                long nuevoId = servicio().registrarTicketConSeguimiento(
                        clienteSeleccionadoId,
                        equipoSeleccionadoId,
                        categoriaSeleccionadaId,
                        tecnicoSeleccionadoId,
                        titulo,
                        descripcion,
                        prioridadSeleccionada,
                        detalleInicial
                );
                agregarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Ticket #" + nuevoId + " registrado con éxito.");
            } else {
                servicio().actualizarTicket(
                        idTicketSeleccionado,
                        clienteSeleccionadoId,
                        equipoSeleccionadoId,
                        categoriaSeleccionadaId,
                        tecnicoSeleccionadoId,
                        titulo,
                        descripcion,
                        prioridadSeleccionada
                );
                if (estadoSeleccionado != null && !estadoSeleccionado.trim().isEmpty()) {
                    Usuario u = usuarioActual();
                    if (u == null || "SOLICITANTE".equalsIgnoreCase(u.getRol().name())) {
                        u = new Tecnico(0L, "Técnico Soporte", "soporte@servicedesk.com", "General", 3);
                    }
                    try {
                        servicio().actualizarEstado(u, idTicketSeleccionado, estadoSeleccionado);
                    } catch (Exception ex) {
                        contexto().log("No fue posible actualizar estado durante edición", ex);
                    }
                }
                agregarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Ticket #" + idTicketSeleccionado + " actualizado con éxito.");
            }

            cargar();
            PrimeFaces.current().executeInitScript("PF('dlgTicket').hide();");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Validación", ex.getMessage());
        } catch (Exception ex) {
            contexto().log("Error al procesar ticket", ex);
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error inesperado al procesar el ticket.");
        }
    }

    public void cambiarEstado(TicketDetalleDTO ticket, String nuevoEstado) {
        if (ticket == null || nuevoEstado == null) return;
        try {
            Usuario u = usuarioActual();
            if (u == null || "SOLICITANTE".equalsIgnoreCase(u.getRol().name())) {
                u = new Tecnico(0L, "Técnico Soporte", "soporte@servicedesk.com", "General", 3);
            }
            servicio().actualizarEstado(u, ticket.getIdTicket(), nuevoEstado);
            ticket.setEstado(nuevoEstado);
            cargar();
            agregarMensaje(FacesMessage.SEVERITY_INFO, "Estado actualizado",
                    "El ticket #" + ticket.getIdTicket() + " ahora está " + nuevoEstado + ".");
        } catch (Exception ex) {
            contexto().log("Error al cambiar estado de ticket", ex);
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
        }
    }

    private void agregarMensaje(FacesMessage.Severity severidad, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severidad, resumen, detalle));
    }

    private Usuario usuarioActual() {
        HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        if (sesion != null) {
            return (Usuario) sesion.getAttribute("usuarioAutenticado");
        }
        return null;
    }

    private ServicioTickets servicio() {
        return (ServicioTickets) contexto().getAttribute("servicioTickets");
    }

    private ServletContext contexto() {
        return (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
    }

    // Getters y Setters
    public List<TicketDetalleDTO> getTickets() { return tickets; }
    public void setTickets(List<TicketDetalleDTO> tickets) { this.tickets = tickets; }

    public List<TicketDetalleDTO> getTicketsFiltrados() { return ticketsFiltrados; }
    public void setTicketsFiltrados(List<TicketDetalleDTO> ticketsFiltrados) { this.ticketsFiltrados = ticketsFiltrados; }

    public List<OpcionCatalogo> getClientesDisponibles() { return clientesDisponibles; }
    public List<OpcionCatalogo> getEquiposDisponibles() { return equiposDisponibles; }
    public List<OpcionCatalogo> getCategoriasDisponibles() { return categoriasDisponibles; }
    public List<OpcionCatalogo> getTecnicosDisponibles() { return tecnicosDisponibles; }

    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }

    public Long getIdTicketSeleccionado() { return idTicketSeleccionado; }
    public void setIdTicketSeleccionado(Long idTicketSeleccionado) { this.idTicketSeleccionado = idTicketSeleccionado; }

    public Long getClienteSeleccionadoId() { return clienteSeleccionadoId; }
    public void setClienteSeleccionadoId(Long clienteSeleccionadoId) { this.clienteSeleccionadoId = clienteSeleccionadoId; }

    public Long getEquipoSeleccionadoId() { return equipoSeleccionadoId; }
    public void setEquipoSeleccionadoId(Long equipoSeleccionadoId) { this.equipoSeleccionadoId = equipoSeleccionadoId; }

    public Long getCategoriaSeleccionadaId() { return categoriaSeleccionadaId; }
    public void setCategoriaSeleccionadaId(Long categoriaSeleccionadaId) { this.categoriaSeleccionadaId = categoriaSeleccionadaId; }

    public Long getTecnicoSeleccionadoId() { return tecnicoSeleccionadoId; }
    public void setTecnicoSeleccionadoId(Long tecnicoSeleccionadoId) { this.tecnicoSeleccionadoId = tecnicoSeleccionadoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPrioridadSeleccionada() { return prioridadSeleccionada; }
    public void setPrioridadSeleccionada(String prioridadSeleccionada) { this.prioridadSeleccionada = prioridadSeleccionada; }

    public String getDetalleInicial() { return detalleInicial; }
    public void setDetalleInicial(String detalleInicial) { this.detalleInicial = detalleInicial; }

    public String getEstadoSeleccionado() { return estadoSeleccionado; }
    public void setEstadoSeleccionado(String estadoSeleccionado) { this.estadoSeleccionado = estadoSeleccionado; }
}
