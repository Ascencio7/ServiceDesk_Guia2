package sv.edu.itca.servicedesk360.storage;

import java.util.List;

import sv.edu.itca.servicedesk360.model.Cliente;

public interface ClienteDAO {
    List<Cliente> listar();
    Cliente buscarPorId(long idCliente);
    long insertar(Cliente cliente);
    void actualizar(Cliente cliente);
    void desactivar(long idCliente);
}
