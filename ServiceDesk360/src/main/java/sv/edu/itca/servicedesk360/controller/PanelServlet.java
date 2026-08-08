/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vladimir Ascencio
 */
@WebServlet("/panel") 
public class PanelServlet extends HttpServlet { 
 
    @Override 
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
 throws ServletException, IOException { 
 
        HttpSession sesion = request.getSession(false); 
 
        if (sesion == null 
                || sesion.getAttribute("usuarioCorreo") == null) { 
            response.sendRedirect( 
                    request.getContextPath() + "/acceso?estado=sesion"); 
            return; 
        } 
 
        request.getRequestDispatcher("/panel.jsp") 
               .forward(request, response); 
    } 
}
