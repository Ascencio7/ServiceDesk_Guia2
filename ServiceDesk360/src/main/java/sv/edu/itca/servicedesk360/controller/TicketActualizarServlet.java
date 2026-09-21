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

@WebServlet("/tickets/actualizar")
public class TicketActualizarServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession sesion = request.getSession(false);
            Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutenticado");
            long id = Long.parseLong(request.getParameter("id"));
            String estado = request.getParameter("estado");
            String esperado = request.getParameter("estadoEsperado");
            servicio(request).actualizarEstadoOptimista(usuario, id, esperado, estado);
            response.sendRedirect(request.getContextPath() + "/tickets?estado=actualizado");
        } catch (RuntimeException ex) {
            getServletContext().log("Error al actualizar ticket", ex);
            request.setAttribute("mensajeError", "No fue posible actualizar el ticket.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private ServicioTickets servicio(HttpServletRequest request) {
        return (ServicioTickets) request.getServletContext().getAttribute("servicioTickets");
    }
}