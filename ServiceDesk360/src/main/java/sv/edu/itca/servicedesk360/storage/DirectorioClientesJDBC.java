package sv.edu.itca.servicedesk360.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.servicedesk360.model.Cliente;
import sv.edu.itca.servicedesk360.persistence.ConexionBD;

public class DirectorioClientesJDBC implements ClienteDAO {

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT id_cliente, nombre, correo, activo FROM clientes ORDER BY nombre, id_cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
            return clientes;
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible cargar los clientes.", ex);
        }
    }

    @Override
    public Cliente buscarPorId(long idCliente) {
        String sql = "SELECT id_cliente, nombre, correo, activo FROM clientes WHERE id_cliente = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar el cliente.", ex);
        }
    }

    @Override
    public long insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, correo, activo) VALUES (?, ?, TRUE)";
        try (Connection cn = ConexionBD.abrir();
                PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getCorreo());
            if (ps.executeUpdate() != 1) {
                throw new IllegalStateException("No se registro el cliente.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new IllegalStateException("MySQL no devolvio el id del cliente.");
        } catch (SQLException ex) {
            throw traducirError(ex);
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, correo = ? "
                + "WHERE id_cliente = ? AND activo = TRUE";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getCorreo());
            ps.setLong(3, cliente.getIdCliente());
            if (ps.executeUpdate() == 0 && !existeActivo(cn, cliente.getIdCliente())) {
                throw new IllegalStateException("El cliente no existe o esta inactivo.");
            }
        } catch (SQLException ex) {
            throw traducirError(ex);
        }
    }

    @Override
    public void desactivar(long idCliente) {
        String sql = "UPDATE clientes SET activo = FALSE WHERE id_cliente = ? AND activo = TRUE";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            if (ps.executeUpdate() != 1) {
                throw new IllegalStateException("El cliente no existe o ya esta inactivo.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible desactivar el cliente.", ex);
        }
    }

    private boolean existeActivo(Connection cn, long idCliente) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE id_cliente = ? AND activo = TRUE";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getLong("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setActivo(rs.getBoolean("activo"));
        return cliente;
    }

    private IllegalStateException traducirError(SQLException ex) {
        if (ex.getErrorCode() == 1062) {
            return new IllegalStateException("El correo ya pertenece a otro cliente.", ex);
        }
        return new IllegalStateException("No fue posible guardar el cliente.", ex);
    }
}
