package sv.edu.itca.servicedesk360.storage;

public interface EquipoDAO {
    boolean perteneceACliente(long idEquipo, long idCliente);
}