package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            request.setAttribute("tickets", servicio().listar());
            if ("creado".equals(request.getParameter("estado"))) {
                request.setAttribute("mensajeExito", "Ticket registrado correctamente.");
            }
            request.getRequestDispatcher("/WEB-INF/views/tickets/listado.jsp")
                    .forward(request, response);
        } catch (RuntimeException ex) {
            getServletContext().log("Error al listar tickets", ex);
            mostrarError(request, response);
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", "No fue posible cargar los tickets.");
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}