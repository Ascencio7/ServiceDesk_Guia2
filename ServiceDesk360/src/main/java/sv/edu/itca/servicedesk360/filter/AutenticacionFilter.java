package sv.edu.itca.servicedesk360.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/panel", "/tickets", "/tickets/*", "/cerrar-sesion"})
public class AutenticacionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession sesion = httpRequest.getSession(false);
        boolean autenticado = sesion != null
                && sesion.getAttribute("usuarioAutenticado") != null;
        if (!autenticado) {
            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/acceso?estado=sesion");
            return;
        }
        chain.doFilter(request, response);
    }
}