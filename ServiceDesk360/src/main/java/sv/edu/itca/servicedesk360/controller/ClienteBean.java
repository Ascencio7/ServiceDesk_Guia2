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

import sv.edu.itca.servicedesk360.model.Cliente;
import sv.edu.itca.servicedesk360.service.ServicioClientes;

@ManagedBean(name = "clienteBean")
@ViewScoped
public class ClienteBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Cliente cliente;
    private List<Cliente> clientes;
    private long clienteId;

    @PostConstruct
    public void init() {
        cliente = new Cliente();
        clientes = Collections.emptyList();
        cargarClientes();
    }

    public void cargarClientes() {
        try {
            clientes = servicio().listar();
        } catch (IllegalStateException ex) {
            registrarError("No fue posible cargar los clientes.", ex);
            clientes = Collections.emptyList();
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "No fue posible cargar los clientes.");
        }
    }

    public String prepararFormulario() {
        if (clienteId <= 0) {
            cliente = new Cliente();
            return null;
        }
        try {
            cliente = servicio().buscarActivo(clienteId);
            return null;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            registrarError("No fue posible abrir el cliente solicitado.", ex);
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                    "No fue posible abrir el cliente solicitado.");
            mantenerMensajesEnRedireccion();
            return "lista?faces-redirect=true";
        }
    }

    public String irNuevo() {
        return "formulario?faces-redirect=true";
    }

    public String editar(Cliente seleccionado) {
        if (seleccionado == null || seleccionado.getIdCliente() <= 0) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Seleccione un cliente valido.");
            return null;
        }
        return "formulario?faces-redirect=true&clienteId=" + seleccionado.getIdCliente();
    }

    public String guardar() {
        try {
            servicio().guardar(cliente);
            agregarMensaje(FacesMessage.SEVERITY_INFO, "Cliente guardado correctamente.");
            mantenerMensajesEnRedireccion();
            return "lista?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            registrarError("No fue posible guardar el cliente.", ex);
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                    ex instanceof IllegalArgumentException ? ex.getMessage()
                    : "No fue posible guardar el cliente. Verifique que el correo no este duplicado.");
            return null;
        }
    }

    public String desactivar(Cliente seleccionado) {
        try {
            if (seleccionado == null) {
                throw new IllegalArgumentException("Seleccione un cliente valido.");
            }
            servicio().desactivar(seleccionado.getIdCliente());
            agregarMensaje(FacesMessage.SEVERITY_INFO, "Cliente desactivado correctamente.");
            mantenerMensajesEnRedireccion();
            return "lista?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            registrarError("No fue posible desactivar el cliente.", ex);
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "No fue posible desactivar el cliente.");
            cargarClientes();
            return null;
        }
    }

    public String volverListado() {
        return "lista?faces-redirect=true";
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    private ServicioClientes servicio() {
        ServletContext contexto = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        return (ServicioClientes) contexto.getAttribute("servicioClientes");
    }

    private void agregarMensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severidad, texto, null));
    }

    private void mantenerMensajesEnRedireccion() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    private void registrarError(String operacion, RuntimeException ex) {
        ServletContext contexto = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        contexto.log(operacion, ex);
    }
}
