package sv.edu.itca.servicedesk360.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import sv.edu.itca.servicedesk360.model.CuentaUsuario;
import sv.edu.itca.servicedesk360.model.RolUsuario;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.Tecnico;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.persistence.ConexionBD;

public class DirectorioCuentasJDBC implements BuscadorCuentas, RegistradorCuentas {
    @Override
    public Optional<CuentaUsuario> buscadorPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre, correo, hash_clave, rol, activo "
                + "FROM usuarios WHERE correo = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, normalizar(correo));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Usuario usuario = construirUsuario(rs);
                if (!rs.getBoolean("activo")) {
                    usuario.desactivar();
                }
                return Optional.of(new CuentaUsuario(usuario, rs.getString("hash_clave")));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar los usuarios en MySQL.", ex);
        }
    }

    @Override
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, normalizar(correo));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible validar el correo en MySQL.", ex);
        }
    }

    @Override
    public void guardar(CuentaUsuario cuenta) {
        String sql = "INSERT INTO usuarios(nombre, correo, hash_clave, rol, activo) VALUES (?, ?, ?, ?, ?)";
        Usuario usuario = cuenta.getUsuario();
        try (Connection cn = ConexionBD.abrir();
                PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, normalizar(usuario.getCorreo()));
            ps.setString(3, cuenta.getHashClave());
            ps.setString(4, usuario.getRol().name());
            ps.setBoolean(5, usuario.isActivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            if ("23000".equals(ex.getSQLState())) {
                throw new IllegalStateException("El correo ya está registrado.", ex);
            }
            throw new IllegalStateException("No fue posible guardar el usuario en MySQL.", ex);
        }
    }

    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        long id = rs.getLong("id_usuario");
        String nombre = rs.getString("nombre");
        String correo = rs.getString("correo");
        RolUsuario rol = RolUsuario.valueOf(rs.getString("rol"));
        if (rol == RolUsuario.TECNICO) {
            return new Tecnico(id, nombre, correo, "Soporte general", 1);
        }
        return new Solicitante(id, nombre, correo, "", "");
    }

    private String normalizar(String correo) {
        return correo == null ? "" : correo.trim().toLowerCase();
    }
}