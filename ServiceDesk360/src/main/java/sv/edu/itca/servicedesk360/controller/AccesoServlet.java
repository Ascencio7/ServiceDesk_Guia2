/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vladimir Ascencio
 */
@WebServlet("/acceso") 
public class AccesoServlet extends HttpServlet { 
 
    private static final Pattern CORREO_VALIDO = Pattern.compile( 
        "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"); 
 
    @Override 
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response) 
            throws ServletException, IOException { 
 
        HttpSession sesion = request.getSession(false); 
        if (sesion != null && sesion.getAttribute("mensajeFlash") != null) {
            
        request.setAttribute("mensajeExito", 
                    sesion.getAttribute("mensajeFlash")); 
            sesion.removeAttribute("mensajeFlash"); 
        } 
 
        String estado = request.getParameter("estado"); 
        if ("cerrada".equals(estado)) { 
            request.setAttribute("mensajeExito", 
                    "La sesión fue cerrada correctamente."); 
        } else if ("sesion".equals(estado)) { 
            request.setAttribute("mensajeError", 
                    "Debe iniciar acceso para abrir el panel."); 
        } 
 
        String ultimoUsuario = buscarCookie(request, "ultimoUsuario"); 
        if (CORREO_VALIDO.matcher(ultimoUsuario).matches()) { 
            request.setAttribute("ultimoUsuario", ultimoUsuario); 
        } 
 
        request.getRequestDispatcher("/login.jsp") 
               .forward(request, response); 
    } 
 
    @Override 
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
            throws ServletException, IOException { 
 
        request.setCharacterEncoding("UTF-8"); 
        String correo = normalizar(request.getParameter("correo")).toLowerCase(); 
        String clave = valorSeguro(request.getParameter("clave")); 
 
        Map<String, Map<String, String>> usuarios = obtenerUsuarios(); 
        Map<String, String> datosUsuario = usuarios.get(correo); 
 
        boolean credencialesValidas = datosUsuario != null 
                && datosUsuario.get("hash").equals(generarHash(clave)); 
 
        if (!credencialesValidas) { 
            request.setAttribute("mensajeError", 
                    "Correo o contraseña incorrectos."); 
            request.getRequestDispatcher("/login.jsp") 
                   .forward(request, response); 
            return; 
        } 
 
        HttpSession sesionAnterior = request.getSession(false); 
        if (sesionAnterior != null) { 
            sesionAnterior.invalidate(); 
        } 
 
        HttpSession sesion = request.getSession(true); 
        sesion.setAttribute("usuarioNombre", datosUsuario.get("nombre")); 
        sesion.setAttribute("usuarioCorreo", correo); 
        sesion.setAttribute("usuarioRol", datosUsuario.get("rol")); 
        sesion.setAttribute("usuarioRolDescripcion", 
                describirRol(datosUsuario.get("rol"))); 
        sesion.setMaxInactiveInterval(15 * 60); 
 
        if ("si".equals(request.getParameter("recordar"))) { 
            agregarCookieCorreo(request, response, correo); 
        } else { 
            eliminarCookieCorreo(request, response); 
        } 
 
        response.sendRedirect(request.getContextPath() + "/panel"); 
    } 
 
    private String describirRol(String rol) { 
        switch (rol) { 
            case "SOLICITANTE": 
                return "Solicitante de soporte"; 
            case "TECNICO": 
                return "Técnico de soporte"; 
            default: 
                return "Rol no identificado"; 
        } 
    } 
 
    private void agregarCookieCorreo(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     String correo) { 
        Cookie cookie = new Cookie("ultimoUsuario", correo); 
        cookie.setMaxAge(7 * 24 * 60 * 60); 
        cookie.setHttpOnly(true); 
        cookie.setSecure(request.isSecure()); 
        cookie.setPath(rutaCookie(request)); 
        response.addCookie(cookie); 
    } 
 
    private void eliminarCookieCorreo(HttpServletRequest request, 
                                      HttpServletResponse response) { 
        Cookie cookie = new Cookie("ultimoUsuario", ""); 
        cookie.setMaxAge(0); 
        cookie.setHttpOnly(true); 
        cookie.setSecure(request.isSecure());
        cookie.setPath(rutaCookie(request)); 
        response.addCookie(cookie); 
    } 
 
    private String buscarCookie(HttpServletRequest request, String nombre) { 
        if (request.getCookies() == null) { 
            return ""; 
        } 
        for (Cookie cookie : request.getCookies()) { 
            if (nombre.equals(cookie.getName())) { 
                return cookie.getValue(); 
            } 
        } 
        return ""; 
    } 
 
    private String rutaCookie(HttpServletRequest request) { 
        String contexto = request.getContextPath(); 
        return contexto.isEmpty() ? "/" : contexto; 
    } 
 
    private String normalizar(String valor) { 
        return valor == null ? "" : valor.trim(); 
    } 
 
    private String valorSeguro(String valor) { 
        return valor == null ? "" : valor; 
    } 
 
    private String generarHash(String valor) { 
        try { 
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); 
            byte[] bytes = digest.digest( 
                    valor.getBytes(StandardCharsets.UTF_8)); 
            return Base64.getEncoder().encodeToString(bytes); 
        } catch (NoSuchAlgorithmException ex) { 
            throw new IllegalStateException( 
                    "No fue posible procesar la contraseña.", ex); 
        } 
    } 
 
    @SuppressWarnings("unchecked") 
    private Map<String, Map<String, String>> obtenerUsuarios() { 
        Object existente = getServletContext().getAttribute("usuarios"); 
        if (existente == null) { 
            synchronized (getServletContext()) { 
                existente = getServletContext().getAttribute("usuarios"); 
                if (existente == null) { 
                    existente = new ConcurrentHashMap<String, 
                            Map<String, String>>(); 
                    getServletContext().setAttribute("usuarios", existente); 
                } 
            } 
        } 
        return (Map<String, Map<String, String>>) existente; 
    } 
} 