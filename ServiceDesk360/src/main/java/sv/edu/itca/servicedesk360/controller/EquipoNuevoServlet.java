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

@WebServlet("/equipos/nuevo")
public class EquipoNuevoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/equipos/nuevo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession(false);
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutenticado");
        String codigo = request.getParameter("codigo");
        String tipo = request.getParameter("tipo");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        try {
            servicio(request).registrarEquipo(usuario, codigo, tipo, marca, modelo);
            response.sendRedirect(request.getContextPath() + "/tickets/nuevo");
        } catch (RuntimeException ex) {
            getServletContext().log("Error al registrar equipo", ex);
            request.setAttribute("mensajeError", ex.getMessage());
            request.setAttribute("codigoAnterior", codigo);
            request.setAttribute("tipoAnterior", tipo);
            request.setAttribute("marcaAnterior", marca);
            request.setAttribute("modeloAnterior", modelo);
            doGet(request, response);
        }
    }

    private ServicioTickets servicio(HttpServletRequest request) {
        return (ServicioTickets) request.getServletContext().getAttribute("servicioTickets");
    }
}