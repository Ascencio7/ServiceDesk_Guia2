/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sv.edu.itca.servicedesk360.service.ServicioRegistro;

/**
 *
 * @author Vladimir Ascencio
 */
@WebServlet("/registro") 
public class RegistroServlet extends HttpServlet { 
 
    @Override 
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws ServletException, IOException { 
        request.getRequestDispatcher("/registro.jsp") 
               .forward(request, response); 
    } 
 
    @Override 
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
            throws ServletException, IOException { 
 
        request.setCharacterEncoding("UTF-8"); 
 
        String nombre = normalizar(request.getParameter("nombre")); 
        String correo = normalizar(request.getParameter("correo")).toLowerCase(); 
        String rol = normalizar(request.getParameter("rol")).toUpperCase(); 
        String clave = valorSeguro(request.getParameter("clave")); 
        String confirmar = valorSeguro(request.getParameter("confirmarClave")); 
 
        ServicioRegistro servicio = (ServicioRegistro) getServletContext()
                .getAttribute("servicioRegistro");
        List<String> errores = servicio.registrar(
                nombre, correo, rol, clave, confirmar);
 
        if (!errores.isEmpty()) { 
            request.setAttribute("mensajeError", String.join(" ", errores)); 
            request.getRequestDispatcher("/registro.jsp") 
                   .forward(request, response); 
            return; 
        } 
 
        HttpSession sesion = request.getSession(); 
        sesion.setAttribute("mensajeFlash", 
                "Cuenta creada correctamente. Ya puede iniciar acceso."); 
 
        response.sendRedirect(request.getContextPath() + "/acceso"); 
    } 
 
    private String normalizar(String valor) { 
        return valor == null ? "" : valor.trim(); 
    } 
 
    private String valorSeguro(String valor) {
        return valor == null ? "" : valor; 
    } 
} 
