package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.service.ServicioTickets;

@WebServlet("/tickets/nuevo")
public class TicketNuevoServlet extends HttpServlet {

    private ServicioTickets servicio() {
        return (ServicioTickets) getServletContext().getAttribute("servicioTickets");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutenticado");
        request.setAttribute("equipos", servicio().listarEquipos(usuario));
        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutenticado");
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String prioridad = request.getParameter("prioridad");
        Long equipoId = parsearId(request.getParameter("equipoId"));
        try {
            List<String> errores = servicio().crear(usuario, titulo, descripcion, prioridad, equipoId);
            if (!errores.isEmpty()) {
                request.setAttribute("errores", errores);
                request.setAttribute("tituloAnterior", titulo);
                request.setAttribute("descripcionAnterior", descripcion);
                request.setAttribute("prioridadAnterior", prioridad);
                request.setAttribute("equipoAnterior", equipoId);
                request.setAttribute("equipos", servicio().listarEquipos(usuario));
                mostrarFormulario(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/tickets?estado=creado");
        } catch (RuntimeException ex) {
            getServletContext().log("Error al crear ticket", ex);
            request.setAttribute("mensajeError", "No fue posible registrar el ticket.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private Long parsearId(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try { return Long.valueOf(valor); } catch (NumberFormatException ex) { return null; }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/tickets/nuevo.jsp")
                .forward(request, response);
    }
}
