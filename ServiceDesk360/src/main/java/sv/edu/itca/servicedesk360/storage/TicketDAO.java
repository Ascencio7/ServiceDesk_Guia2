package sv.edu.itca.servicedesk360.storage;

import java.sql.Connection;
import java.util.List;

import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;

public interface TicketDAO {
    long registrar(Connection conexion, long idCliente, long idEquipo, long idCategoria,
            long idTecnico, String titulo, String descripcion, String prioridad) ;
    List<TicketDetalleDTO> listarDetalle(String estado, String prioridad, int limite, int offset);
    int contar(String estado, String prioridad);
    boolean actualizarEstado(long idTicket, String estadoEsperado, String nuevoEstado);
}