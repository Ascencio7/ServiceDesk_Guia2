package sv.edu.itca.servicedesk360.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.servicedesk360.model.EstadoTicket;
import sv.edu.itca.servicedesk360.model.PrioridadTicket;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.TicketSoporte;
import sv.edu.itca.servicedesk360.persistence.ConexionBD;

public class DirectorioTicketsJDBC implements BuscadorTickets, RegistradorTickets {

    public void registrarEquipo(String nombreCliente, String correoCliente,
            String codigo, String tipo, String marca, String modelo) {
        String clienteBuscar = "SELECT id_cliente FROM clientes WHERE correo = ?";
        String clienteInsertar = "INSERT INTO clientes(nombre, correo) VALUES (?, ?)";
        String equipoInsertar = "INSERT INTO equipos(id_cliente, codigo_inventario, tipo, marca, modelo) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = ConexionBD.abrir()) {
            cn.setAutoCommit(false);
            try {
                long clienteId;
                try (PreparedStatement ps = cn.prepareStatement(clienteBuscar)) {
                    ps.setString(1, correoCliente);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            clienteId = rs.getLong(1);
                        } else {
                            try (PreparedStatement insertar = cn.prepareStatement(clienteInsertar, Statement.RETURN_GENERATED_KEYS)) {
                                insertar.setString(1, nombreCliente);
                                insertar.setString(2, correoCliente);
                                insertar.executeUpdate();
                                try (ResultSet keys = insertar.getGeneratedKeys()) {
                                    if (!keys.next()) throw new SQLException("No se genero el id del cliente.");
                                    clienteId = keys.getLong(1);
                                }
                            }
                        }
                    }
                }
                try (PreparedStatement ps = cn.prepareStatement(equipoInsertar)) {
                    ps.setLong(1, clienteId);
                    ps.setString(2, codigo);
                    ps.setString(3, tipo);
                    ps.setString(4, marca);
                    ps.setString(5, modelo);
                    ps.executeUpdate();
                }
                cn.commit();
            } catch (SQLException ex) {
                try { cn.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            }
        } catch (SQLException ex) {
            if ("23000".equals(ex.getSQLState())) {
                throw new IllegalStateException("El codigo de inventario ya existe.", ex);
            }
            throw new IllegalStateException("No fue posible registrar el equipo en MySQL.", ex);
        }
    }

    public List<EquipoOpcion> listarEquipos(String correoCliente) {
        String sql = "SELECT e.id_equipo, e.codigo_inventario, e.tipo, e.marca, e.modelo "
                + "FROM equipos e JOIN clientes c ON c.id_cliente = e.id_cliente "
                + "WHERE c.correo = ? AND e.activo = TRUE ORDER BY e.codigo_inventario";
        List<EquipoOpcion> equipos = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correoCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    String detalle = rs.getString("tipo");
                    if (marca != null && !marca.trim().isEmpty()) detalle += " - " + marca;
                    if (modelo != null && !modelo.trim().isEmpty()) detalle += " " + modelo;
                    equipos.add(new EquipoOpcion(rs.getLong("id_equipo"),
                            rs.getString("codigo_inventario") + " | " + detalle));
                }
            }
            return equipos;
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar los equipos en MySQL.", ex);
        }
    }

    public void actualizarEstado(long ticketId, String estado) {
        String sql = "UPDATE tickets SET estado = ? WHERE id_ticket = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setLong(2, ticketId);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("El ticket no existe.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible actualizar el ticket en MySQL.", ex);
        }
    }

    public void eliminar(long ticketId) {
        String eliminarSeguimientos = "DELETE FROM seguimientos WHERE id_ticket = ?";
        String eliminarTicket = "DELETE FROM tickets WHERE id_ticket = ?";
        try (Connection cn = ConexionBD.abrir()) {
            cn.setAutoCommit(false);
            try (PreparedStatement seguimientos = cn.prepareStatement(eliminarSeguimientos);
                    PreparedStatement ticket = cn.prepareStatement(eliminarTicket)) {
                seguimientos.setLong(1, ticketId);
                seguimientos.executeUpdate();
                ticket.setLong(1, ticketId);
                if (ticket.executeUpdate() == 0) {
                    throw new IllegalArgumentException("El ticket no existe.");
                }
                cn.commit();
            } catch (SQLException | IllegalArgumentException ex) {
                try {
                    cn.rollback();
                } catch (SQLException rollback) {
                    ex.addSuppressed(rollback);
                }
                throw ex;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible eliminar el ticket en MySQL.", ex);
        }
    }

    @Override
    public void guardar(TicketSoporte ticket) {
        String clienteSql = "SELECT id_cliente FROM clientes WHERE correo = ?";
        String insertarClienteSql = "INSERT INTO clientes(nombre, correo) VALUES (?, ?)";
        String categoriaSql = "SELECT id_categoria FROM categorias WHERE nombre = 'Software'";
        String insertarCategoriaSql = "INSERT INTO categorias(nombre) VALUES ('Software')";
        String ticketSql = "INSERT INTO tickets "
            + "(id_cliente, id_equipo, id_categoria, titulo, descripcion, prioridad) VALUES (?, ?, ?, ?, ?, ?)";
        String seguimientoSql = "INSERT INTO seguimientos(id_ticket, detalle) VALUES (?, ?)";

        try (Connection cn = ConexionBD.abrir()) {
            cn.setAutoCommit(false);
            try {
                long clienteId = buscarOcrearId(cn, clienteSql, insertarClienteSql,
                        ticket.getSolicitante().getCorreo(), ticket.getSolicitante().getNombreCompleto());
                Long equipoId = buscarEquipoDelCliente(cn, ticket.getSolicitante().getCorreo(), ticket.getEquipoId());
                long categoriaId = buscarOCrearCategoria(cn, categoriaSql, insertarCategoriaSql);
                long ticketId;
                try (PreparedStatement ps = cn.prepareStatement(ticketSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, clienteId);
                    if (equipoId == null) ps.setNull(2, java.sql.Types.BIGINT); else ps.setLong(2, equipoId);
                    ps.setLong(3, categoriaId);
                    ps.setString(4, ticket.getTitulo());
                    ps.setString(5, ticket.getDescripcion());
                    ps.setString(6, ticket.getPrioridad().name());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("MySQL no devolvio el id del ticket.");
                        }
                        ticketId = keys.getLong(1);
                    }
                }
                try (PreparedStatement ps = cn.prepareStatement(seguimientoSql)) {
                    ps.setLong(1, ticketId);
                    ps.setString(2, "Ticket registrado desde la aplicacion.");
                    ps.executeUpdate();
                }
                cn.commit();
            } catch (SQLException ex) {
                try {
                    cn.rollback();
                } catch (SQLException rollback) {
                    ex.addSuppressed(rollback);
                }
                throw ex;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible guardar el ticket en MySQL.", ex);
        }
    }

    @Override
    public long siguienteId() {
        String sql = "SELECT COALESCE(MAX(id_ticket), 0) + 1 FROM tickets";
        try (Connection cn = ConexionBD.abrir();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar MySQL.", ex);
        }
    }

    @Override
    public List<TicketSoporte> listarTodos() {
        String sql = "SELECT t.id_ticket, t.titulo, t.descripcion, t.prioridad, t.estado, "
                + "c.id_cliente, c.nombre, c.correo FROM tickets t "
                + "JOIN clientes c ON c.id_cliente = t.id_cliente "
                + "ORDER BY t.fecha_creacion DESC";
        List<TicketSoporte> tickets = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitante solicitante = new Solicitante(rs.getLong("id_cliente"),
                        rs.getString("nombre"), rs.getString("correo"), "", "");
                TicketSoporte ticket = new TicketSoporte(rs.getLong("id_ticket"),
                        rs.getString("titulo"), rs.getString("descripcion"), solicitante,
                        PrioridadTicket.valueOf(rs.getString("prioridad")));
                ticket.cambiarEstado(EstadoTicket.valueOf(rs.getString("estado")));
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException | IllegalArgumentException ex) {
            throw new IllegalStateException("No fue posible listar los tickets de MySQL.", ex);
        }
    }

    private long buscarOcrearId(Connection cn, String buscarSql, String insertarSql,
            String correo, String nombre) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(buscarSql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("MySQL no devolvio el id del cliente.");
                }
                return keys.getLong(1);
            }
        }
    }

    private long buscarOCrearCategoria(Connection cn, String buscarSql, String insertarSql)
            throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(buscarSql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        try (PreparedStatement ps = cn.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("MySQL no devolvio el id de la categoria.");
                }
                return keys.getLong(1);
            }
        }
    }

    private Long buscarEquipoDelCliente(Connection cn, String correo, Long equipoId) throws SQLException {
        if (equipoId == null) return null;
        String sql = "SELECT e.id_equipo FROM equipos e JOIN clientes c ON c.id_cliente = e.id_cliente "
                + "WHERE e.id_equipo = ? AND c.correo = ? AND e.activo = TRUE";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, equipoId);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("El equipo seleccionado no pertenece al solicitante.");
                return rs.getLong(1);
            }
        }
    }
}