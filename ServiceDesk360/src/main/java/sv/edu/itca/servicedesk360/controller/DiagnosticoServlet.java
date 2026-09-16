package sv.edu.itca.servicedesk360.controller;

/**
 *
 * @author Vladimir Ascencio
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sv.edu.itca.servicedesk360.persistence.ConexionBD;
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
    HttpSession sesion = request.getSession(false);
    boolean autenticado = sesion != null
        && sesion.getAttribute("usuarioAutenticado") != null;
    String destino = contexto + (autenticado ? "/panel" : "/acceso");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<title>Diagnóstico | ServiceDesk 360</title>");
            out.println("<style>");
            out.println(":root{--ink:#17324d;--muted:#6b7c8f;--line:#dce7ec;--teal:#087f8c;--teal-dark:#075c69;--ok:#16845b;--warning:#ad4b27}");
            out.println("*{box-sizing:border-box}body{margin:0;min-height:100vh;font-family:'Segoe UI',Arial,sans-serif;color:var(--ink);background:linear-gradient(135deg,#e8f1f1,#f8f4ed);padding:clamp(22px,6vw,72px) 18px}");
            out.println("main{max-width:820px;margin:auto;background:#fff;border:1px solid rgba(23,50,77,.08);border-radius:18px;box-shadow:0 20px 55px rgba(23,50,77,.14);overflow:hidden}");
            out.println(".hero{padding:30px 34px 26px;background:linear-gradient(120deg,var(--teal-dark),var(--teal));color:#fff}.eyebrow{margin:0 0 10px;text-transform:uppercase;letter-spacing:.12em;font-size:11px;font-weight:700;color:#bdebed}.hero h1{margin:0;font-size:clamp(27px,5vw,40px);line-height:1.1}.hero p:last-child{margin:12px 0 0;color:#d9f3f2;font-size:15px}");
            out.println(".body{padding:28px 34px 30px}.section-title{margin:0 0 14px;font-size:14px;text-transform:uppercase;letter-spacing:.08em;color:var(--muted)}.grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:12px;margin:0 0 26px;padding:0}.item{padding:15px 17px;border:1px solid var(--line);border-radius:11px;background:#fff}.item dt{margin:0 0 5px;color:var(--muted);font-size:12px;font-weight:700;text-transform:uppercase;letter-spacing:.05em}.item dd{margin:0;font-size:16px;font-weight:600;overflow-wrap:anywhere}");
            out.println(".status{border:1px solid #b9e4d1;border-radius:13px;background:#effaf4;padding:18px 20px;margin-bottom:26px}.status-label{display:flex;align-items:center;gap:9px;color:var(--ok);font-weight:700;font-size:16px}.dot{width:10px;height:10px;border-radius:50%;background:#20a66f;box-shadow:0 0 0 5px #d4f2e2}.status p{margin:9px 0 0;color:#356052;font-size:14px}.config{margin-top:16px;padding-top:16px;border-top:1px solid #cfe9db;color:#356052;font-size:13px;line-height:1.65}.config strong{color:var(--ink)}");
            out.println(".footer{display:flex;justify-content:space-between;align-items:center;gap:16px;border-top:1px solid var(--line);padding-top:22px}.footer small{color:var(--muted)}.back{display:inline-block;padding:10px 16px;border-radius:8px;background:var(--teal);color:#fff;text-decoration:none;font-weight:700;font-size:14px}.back:hover{background:var(--teal-dark)}");
            out.println("@media(max-width:560px){.hero,.body{padding-left:22px;padding-right:22px}.grid{grid-template-columns:1fr}.footer{align-items:stretch;flex-direction:column-reverse}.back{text-align:center}}");
            out.println("</style></head><body><main>");
            out.println("<header class='hero'><p class='eyebrow'>ServiceDesk 360 · diagnóstico</p><h1>Estado de la aplicación</h1><p>Información del servidor y disponibilidad de la conexión persistente.</p></header><section class='body'>");
            out.println("<h2 class='section-title'>Entorno de ejecución</h2><dl class='grid'>");
            out.printf("<div class='item'><dt>Método HTTP</dt><dd>%s</dd></div>%n", escapar(metodo));
            out.printf("<div class='item'><dt>Servidor</dt><dd>%s:%d</dd></div>%n", escapar(servidor), puerto);
            out.printf("<div class='item'><dt>Contexto</dt><dd>%s</dd></div>%n", escapar(contexto));
            out.printf("<div class='item'><dt>Versión de Java</dt><dd>%s</dd></div>%n", escapar(javaVersion));
            out.println("</dl>");

            Path archivoConfiguracion = Paths.get(System.getProperty("user.home"), ".servicedesk360", "db.properties");
            out.printf("<div class='config'><strong>Carpeta de configuración:</strong> %s<br><strong>db.properties encontrado:</strong> %s</div>%n", escapar(System.getProperty("user.home")), Files.exists(archivoConfiguracion));
            try (InputStream entrada = Files.newInputStream(archivoConfiguracion)) {
                Properties propiedades = new Properties();
                propiedades.load(entrada);
                out.printf("<div class='config'><strong>Usuario JDBC:</strong> %s</div>%n", escapar(propiedades.getProperty("db.user", "no definido")));
            } catch (Exception ex) {
                out.println("<div class='config'><strong>Configuración JDBC:</strong> no se pudo leer db.properties.</div>");
            }

            try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement("SELECT COUNT(*) FROM tickets"); ResultSet rs = ps.executeQuery()) {
                rs.next();
                out.printf("<section class='status'><div class='status-label'><span class='dot'></span>MySQL conectado</div><p>La conexión persistente está disponible y se encontraron <strong>%d</strong> tickets en la base de datos.</p></section>%n", rs.getLong(1));
            } catch (Exception ex) {
                getServletContext().log("Diagnóstico JDBC fallido", ex);
                out.printf("<section class='status' style='background:#fff4ef;border-color:#f0c8b9'><div class='status-label' style='color:var(--warning)'><span class='dot' style='background:var(--warning);box-shadow:0 0 0 5px #fbe0d5'></span>MySQL no disponible</div><p>%s: %s</p></section>%n", escapar(ex.getClass().getSimpleName()), escapar(ex.getMessage() == null ? "sin detalle" : ex.getMessage()));
            }
            out.printf("<footer class='footer'><small>Diagnóstico generado en tiempo real</small><a class='back' href='%s'>%s</a></footer></section></main></body></html>%n", escapar(destino), autenticado ? "Volver al panel" : "Ir al acceso");
        }
    }

    private String escapar(String valor) {
        return valor.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
