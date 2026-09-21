package sv.edu.itca.servicedesk360.storage;

import java.sql.Connection;

import sv.edu.itca.servicedesk360.model.Seguimiento;

public interface SeguimientoDAO {
    int registrar(Connection conexion, Seguimiento seguimiento);
}