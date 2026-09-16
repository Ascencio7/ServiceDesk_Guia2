package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.service.ServicioTickets;

@WebServlet("/tickets/eliminar")
public class TicketEliminarServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession sesion = request.getSession(false);
            Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutenticado");
            long id = Long.parseLong(request.getParameter("id"));
            servicio(request).eliminar(usuario, id);
            response.sendRedirect(request.getContextPath() + "/tickets?estado=eliminado");
        } catch (RuntimeException ex) {
            getServletContext().log("Error al eliminar ticket", ex);
            request.setAttribute("mensajeError", "No fue posible eliminar el ticket.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private ServicioTickets servicio(HttpServletRequest request) {
        return (ServicioTickets) request.getServletContext().getAttribute("servicioTickets");
    }
}