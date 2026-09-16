package sv.edu.itca.servicedesk360.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public final class PruebaConexion {
    private PruebaConexion() { }

    public static void main(String[] args) throws Exception {
        try (Connection cn = ConexionBD.abrir()) {
            DatabaseMetaData metadatos = cn.getMetaData();
            System.out.println("Conexion valida: " + cn.isValid(3));
            System.out.println("DBMS: " + metadatos.getDatabaseProductName());
            System.out.println("Version: " + metadatos.getDatabaseProductVersion());
            System.out.println("Driver: " + metadatos.getDriverName());
        }
    }
}