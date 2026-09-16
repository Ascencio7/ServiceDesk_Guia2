package sv.edu.itca.servicedesk360.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioJDBC {
    public ClienteRegistro buscarClientePorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_cliente, nombre, correo, activo FROM clientes WHERE correo = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new ClienteRegistro(rs.getLong("id_cliente"), rs.getString("nombre"),
                        rs.getString("correo"), rs.getBoolean("activo"));
            }
        }
    }

    public long insertarCliente(String nombre, String correo) throws SQLException {
        String sql = "INSERT INTO clientes(nombre, correo) VALUES (?, ?)";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre); ps.setString(2, correo); ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("MySQL no devolvio la clave generada.");
                return claves.getLong(1);
            }
        }
    }

    public List<TicketVista> listarTickets() throws SQLException {
        String sql = "SELECT t.id_ticket, t.titulo, t.prioridad, t.estado, t.fecha_creacion, "
                + "c.nombre AS cliente, cat.nombre AS categoria FROM tickets t "
                + "JOIN clientes c ON c.id_cliente = t.id_cliente "
                + "JOIN categorias cat ON cat.id_categoria = t.id_categoria "
                + "ORDER BY t.fecha_creacion DESC";
        List<TicketVista> salida = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp fecha = rs.getTimestamp("fecha_creacion");
                salida.add(new TicketVista(rs.getLong("id_ticket"), rs.getString("titulo"),
                        rs.getString("prioridad"), rs.getString("estado"), fecha.toLocalDateTime(),
                        rs.getString("cliente"), rs.getString("categoria")));
            }
        }
        return salida;
    }

    public long registrarTicketConSeguimiento(long clienteId, Long equipoId, Long tecnicoId,
            long categoriaId, String titulo, String descripcion, String prioridad, String detalle) throws SQLException {
        String ticketSql = "INSERT INTO tickets (id_cliente, id_equipo, id_tecnico, id_categoria, titulo, descripcion, prioridad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String seguimientoSql = "INSERT INTO seguimientos(id_ticket, detalle) VALUES (?, ?)";
        try (Connection cn = ConexionBD.abrir()) {
            boolean autoCommit = cn.getAutoCommit();
            cn.setAutoCommit(false);
            try {
                long ticketId;
                try (PreparedStatement ps = cn.prepareStatement(ticketSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, clienteId);
                    if (equipoId == null) ps.setNull(2, java.sql.Types.BIGINT); else ps.setLong(2, equipoId);
                    if (tecnicoId == null) ps.setNull(3, java.sql.Types.BIGINT); else ps.setLong(3, tecnicoId);
                    ps.setLong(4, categoriaId); ps.setString(5, titulo); ps.setString(6, descripcion); ps.setString(7, prioridad);
                    ps.executeUpdate();
                    try (ResultSet claves = ps.getGeneratedKeys()) {
                        if (!claves.next()) throw new SQLException("No se genero el id del ticket.");
                        ticketId = claves.getLong(1);
                    }
                }
                try (PreparedStatement ps = cn.prepareStatement(seguimientoSql)) {
                    ps.setLong(1, ticketId); ps.setString(2, detalle); ps.executeUpdate();
                }
                cn.commit(); cn.setAutoCommit(autoCommit); return ticketId;
            } catch (SQLException ex) {
                try { cn.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            }
        }
    }
}