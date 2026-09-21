package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.service.ServicioTickets;

@WebServlet("/tickets")
public class TicketListadoServlet extends HttpServlet {

    private ServicioTickets servicio() {
        return (ServicioTickets) getServletContext().getAttribute("servicioTickets");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Usuario usuario = (Usuario) request.getSession(false)
                .getAttribute("usuarioAutenticado");
            String estado = request.getParameter("estadoFiltro");
            String prioridad = request.getParameter("prioridadFiltro");
            int pagina = parsearPagina(request.getParameter("pagina"));
            List<TicketDetalleDTO> detalle = servicio().listarDetalle(estado, prioridad, pagina, 10);
            request.setAttribute("ticketsDetalle", detalle);
            request.setAttribute("totalPaginas", (int) Math.ceil(servicio().contarDetalle(estado, prioridad) / 10.0));
            request.setAttribute("paginaActual", pagina);
            request.setAttribute("estadoFiltro", estado);
            request.setAttribute("prioridadFiltro", prioridad);
            request.setAttribute("tickets", servicio().listarPara(usuario));
            request.setAttribute("vistaPropia", usuario.getRol().name().equals("SOLICITANTE"));
            if ("creado".equals(request.getParameter("estado"))) {
                request.setAttribute("mensajeExito", "Ticket registrado correctamente.");
            } else if ("actualizado".equals(request.getParameter("estado"))) {
                request.setAttribute("mensajeExito", "Ticket actualizado correctamente.");
            } else if ("eliminado".equals(request.getParameter("estado"))) {
                request.setAttribute("mensajeExito", "Ticket eliminado correctamente.");
            }
            request.getRequestDispatcher("/WEB-INF/views/tickets/listado.jsp")
                    .forward(request, response);
        } catch (RuntimeException ex) {
            getServletContext().log("Error al listar tickets", ex);
            Throwable causa = ex;
            while (causa.getCause() != null) {
                causa = causa.getCause();
            }
            getServletContext().log("Causa raiz del listado: " + causa.getClass().getName()
                    + ": " + causa.getMessage());
            mostrarError(request, response);
        }
    }

    private int parsearPagina(String valor) {
        try { return Math.max(1, Integer.parseInt(valor == null ? "1" : valor)); }
        catch (NumberFormatException ex) { return 1; }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", "No fue posible cargar los tickets.");
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
