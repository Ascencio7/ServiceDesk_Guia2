package sv.edu.itca.servicedesk360.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import sv.edu.itca.servicedesk360.storage.DirectorioCuentasEnMemoria;
import sv.edu.itca.servicedesk360.service.ValidadorRegistro;
import sv.edu.itca.servicedesk360.service.ServicioRegistro;
import sv.edu.itca.servicedesk360.service.ServicioAutenticacion;
import sv.edu.itca.servicedesk360.service.Autenticador;
import sv.edu.itca.servicedesk360.service.ServicioTickets;
import sv.edu.itca.servicedesk360.storage.DirectorioTicketsEnMemoria;

@WebListener
public class AplicacionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // 1. Instanciar el almacenamiento (implementa BuscadorCuentas y RegistradorCuentas)
        DirectorioCuentasEnMemoria directorio = new DirectorioCuentasEnMemoria();
        
        // 2. Instanciar el validador
        ValidadorRegistro validador = new ValidadorRegistro();

        // 3. Instanciar los servicios con sus dependencias
        ServicioRegistro servicioRegistro = new ServicioRegistro(
                directorio,  // BuscadorCuentas
                directorio,  // RegistradorCuentas
                validador    // ValidadorRegistro
        );
        
        Autenticador autenticador = new ServicioAutenticacion(directorio);

        DirectorioTicketsEnMemoria directorioTickets = new DirectorioTicketsEnMemoria();
        ServicioTickets servicioTickets = new ServicioTickets(
            directorioTickets, directorioTickets);

        // 4. Registrar los servicios en el contexto global de la aplicación
        context.setAttribute("servicioRegistro", servicioRegistro);
        context.setAttribute("autenticador", autenticador);
        context.setAttribute("servicioTickets", servicioTickets);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpieza de recursos al apagar la aplicación
    }
}