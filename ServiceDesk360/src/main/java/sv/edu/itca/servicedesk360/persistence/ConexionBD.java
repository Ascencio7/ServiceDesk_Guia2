package sv.edu.itca.servicedesk360.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {
    private static final Properties CONFIG = cargarConfiguracion();

    private ConexionBD() { }

    private static Properties cargarConfiguracion() {
        Path ruta = Paths.get(System.getProperty("user.home"), ".servicedesk360", "db.properties");
        Properties propiedades = new Properties();
        try (InputStream entrada = Files.newInputStream(ruta)) {
            propiedades.load(entrada);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo cargar la configuracion JDBC externa.", ex);
        }
        for (String clave : new String[] {"db.url", "db.user", "db.password"}) {
            if (propiedades.getProperty(clave) == null || propiedades.getProperty(clave).trim().isEmpty()) {
                throw new IllegalStateException("Falta la propiedad JDBC: " + clave);
            }
        }
        return propiedades;
    }

    public static Connection abrir() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontro MySQL Connector/J en el WAR.", ex);
        }
        return DriverManager.getConnection(CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"), CONFIG.getProperty("db.password"));
    }
}