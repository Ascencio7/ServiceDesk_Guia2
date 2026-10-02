package sv.edu.itca.servicedesk360.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;
import sv.edu.itca.servicedesk360.service.ServicioTickets;

@Named("ticketBean")
@ViewScoped
public class TicketBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<TicketDetalleDTO> tickets = Collections.emptyList();

    @PostConstruct
    public void init() {
        cargar();
    }

    public void cargar() {
        try {
            tickets = servicio().listarDetalle(null, null, 1, 20);
        } catch (IllegalStateException ex) {
            ServletContext contexto = contexto();
            contexto.log("No fue posible cargar el listado JSF de tickets.", ex);
            tickets = Collections.emptyList();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No fue posible cargar los tickets.", null));
        }
    }

    public List<TicketDetalleDTO> getTickets() {
        return tickets;
    }

    private ServicioTickets servicio() {
        return (ServicioTickets) contexto().getAttribute("servicioTickets");
    }

    private ServletContext contexto() {
        return (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
    }
}
