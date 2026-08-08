package sv.edu.itca.servicedesk360.controller;

/**
 *
 * @author Vladimir Ascencio
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(name = "DiagnosticoServlet", urlPatterns = {"/diagnostico"})
public class DiagnosticoServlet extends HttpServlet {
 @Override
 protected void doGet(HttpServletRequest request,
 HttpServletResponse response)
 throws ServletException, IOException {
 response.setContentType("text/html;charset=UTF-8");
 String contexto = request.getContextPath();
 String metodo = request.getMethod();
 String servidor = request.getServerName();
 int puerto = request.getServerPort();
 String javaVersion = System.getProperty("java.version");
 try (PrintWriter out = response.getWriter()) {
 out.println("<!DOCTYPE html>");
 out.println("<html lang='es'>");
 out.println("<head>");
 out.println("<meta charset='UTF-8'>");
 out.println("<title>Diagnóstico - ServiceDesk 360</title>");
 out.println("<style>");
 out.println("body{font-family:Arial;background:#f4f4f4;padding:30px;}");
 out.println("main{max-width:760px;margin:auto;background:white;"
 + "padding:25px;border-radius:10px;}");
 out.println("h1{color:#b33b26;}");
 out.println("dt{font-weight:bold;color:#8a6814;}");
 out.println("dd{margin-bottom:12px;}");
 out.println("a{color:#b33b26;font-weight:bold;}");
 out.println("</style>");
 out.println("</head>");
 out.println("<body><main>");
 out.println("<h1>Diagnóstico de la aplicación</h1>");
 out.println("<dl>");
 out.printf("<dt>Método HTTP</dt><dd>%s</dd>%n", metodo);
 out.printf("<dt>Servidor</dt><dd>%s</dd>%n", servidor);
 out.printf("<dt>Puerto</dt><dd>%d</dd>%n", puerto);
 out.printf("<dt>Contexto</dt><dd>%s</dd>%n", contexto);
 out.printf("<dt>Versión de Java</dt><dd>%s</dd>%n", javaVersion);
 out.println("</dl>");
 out.printf("<a href='%s/'>Volver al inicio</a>%n", contexto);
 out.println("</main></body></html>");
        }
    }
}
