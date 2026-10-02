package sv.edu.itca.servicedesk360.service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import sv.edu.itca.servicedesk360.model.Cliente;
import sv.edu.itca.servicedesk360.storage.ClienteDAO;

public class ServicioClientes {
    private static final Pattern CORREO = Pattern.compile("^[^ @]+@[^ @]+[.][^ @]+$");

    private final ClienteDAO clienteDAO;

    public ServicioClientes(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public List<Cliente> listar() {
        return clienteDAO.listar();
    }

    public Cliente buscarActivo(long idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El identificador del cliente no es valido.");
        }
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null || !cliente.isActivo()) {
            throw new IllegalArgumentException("El cliente no existe o esta inactivo.");
        }
        return cliente;
    }

    public void guardar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Los datos del cliente son obligatorios.");
        }
        String nombre = cliente.getNombre() == null ? "" : cliente.getNombre().trim();
        String correo = cliente.getCorreo() == null ? ""
                : cliente.getCorreo().trim().toLowerCase(Locale.ROOT);
        if (nombre.length() < 3 || nombre.length() > 120) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 120 caracteres.");
        }
        if (correo.length() > 160 || !CORREO.matcher(correo).matches()) {
            throw new IllegalArgumentException("Ingrese un correo valido de hasta 160 caracteres.");
        }
        if (cliente.getIdCliente() < 0) {
            throw new IllegalArgumentException("El identificador del cliente no es valido.");
        }
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        if (cliente.getIdCliente() == 0) {
            cliente.setIdCliente(clienteDAO.insertar(cliente));
        } else {
            clienteDAO.actualizar(cliente);
        }
    }

    public void desactivar(long idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El identificador del cliente no es valido.");
        }
        clienteDAO.desactivar(idCliente);
    }
}
