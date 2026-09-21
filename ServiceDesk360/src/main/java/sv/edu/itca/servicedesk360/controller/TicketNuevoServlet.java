package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sv.edu.itca.servicedesk360.service.ServicioTickets;

@WebServlet("/tickets/nuevo")
public class TicketNuevoServlet extends HttpServlet {

    private ServicioTickets servicio() {
        return (ServicioTickets) getServletContext().getAttribute("servicioTickets");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarCatalogos(request);
        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String prioridad = request.getParameter("prioridad");
        Long clienteId = parsearId(request.getParameter("clienteId"));
        Long equipoId = parsearId(request.getParameter("equipoId"));
        Long categoriaId = parsearId(request.getParameter("categoriaId"));
        Long tecnicoId = parsearId(request.getParameter("tecnicoId"));
        String detalleInicial = request.getParameter("detalleInicial");
        try {
            long id = servicio().registrarTicketConSeguimiento(valor(clienteId), valor(equipoId),
                    valor(categoriaId), valor(tecnicoId), titulo, descripcion, prioridad, detalleInicial);
            response.sendRedirect(request.getContextPath() + "/tickets?estado=creado&id=" + id);
        } catch (RuntimeException ex) {
            request.setAttribute("errores", java.util.Collections.singletonList(ex.getMessage()));
            request.setAttribute("tituloAnterior", titulo);
            request.setAttribute("descripcionAnterior", descripcion);
            request.setAttribute("detalleInicialAnterior", detalleInicial);
            request.setAttribute("prioridadAnterior", prioridad);
            request.setAttribute("clienteAnterior", clienteId);
            request.setAttribute("equipoAnterior", equipoId);
            request.setAttribute("categoriaAnterior", categoriaId);
            request.setAttribute("tecnicoAnterior", tecnicoId);
            cargarCatalogos(request);
            mostrarFormulario(request, response);
        }
    }

    private void cargarCatalogos(HttpServletRequest request) {
        request.setAttribute("clientes", servicio().listarClientes());
        request.setAttribute("categorias", servicio().listarCategorias());
        request.setAttribute("tecnicos", servicio().listarTecnicos());
        request.setAttribute("equipos", servicio().listarEquiposPorCliente(0));
    }

    private long valor(Long id) { return id == null ? 0 : id; }

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
