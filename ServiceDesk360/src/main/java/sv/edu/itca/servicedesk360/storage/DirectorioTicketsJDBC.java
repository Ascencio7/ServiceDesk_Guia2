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
import sv.edu.itca.servicedesk360.model.Seguimiento;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.TicketDetalleDTO;
import sv.edu.itca.servicedesk360.model.TicketSoporte;
import sv.edu.itca.servicedesk360.persistence.ConexionBD;

public class DirectorioTicketsJDBC implements BuscadorTickets, RegistradorTickets,
        TicketDAO, SeguimientoDAO, EquipoDAO {

    @Override
    public long registrar(Connection cn, long idCliente, long idEquipo, long idCategoria,
            long idTecnico, String titulo, String descripcion, String prioridad) {
        String sql = "INSERT INTO tickets (id_cliente, id_equipo, id_categoria, id_tecnico, "
                + "titulo, descripcion, prioridad, estado, fecha_creacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'ABIERTO', NOW())";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, idCliente);
            ps.setLong(2, idEquipo);
            ps.setLong(3, idCategoria);
            ps.setLong(4, idTecnico);
            ps.setString(5, titulo);
            ps.setString(6, descripcion);
            ps.setString(7, prioridad);
            if (ps.executeUpdate() != 1) throw new IllegalStateException("No se inserto el ticket.");
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            throw new IllegalStateException("MySQL no devolvio el id del ticket.");
        } catch (SQLException ex) {
            throw new IllegalStateException("Error registrando el ticket.", ex);
        }
    }

    @Override
    public int registrar(Connection cn, Seguimiento seguimiento) {
        String sql = "INSERT INTO seguimientos (id_ticket, detalle, fecha_registro) VALUES (?, ?, NOW())";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, seguimiento.getIdTicket());
            ps.setString(2, seguimiento.getDetalle());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Error registrando el seguimiento.", ex);
        }
    }

    @Override
    public boolean perteneceACliente(long idEquipo, long idCliente) {
        String sql = "SELECT 1 FROM equipos WHERE id_equipo = ? AND id_cliente = ? AND activo = TRUE";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idEquipo);
            ps.setLong(2, idCliente);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible validar el equipo.", ex);
        }
    }

    @Override
    public List<TicketDetalleDTO> listarDetalle(String estado, String prioridad, int limite, int offset) {
        String sql = "SELECT t.id_ticket, c.nombre AS cliente, "
                + "COALESCE(CONCAT(e.codigo_inventario, ' - ', e.marca, ' ', e.modelo), 'Sin equipo') AS equipo, "
                + "cat.nombre AS categoria, tec.nombre AS tecnico, t.titulo, t.prioridad, t.estado, t.fecha_creacion "
                + "FROM tickets t JOIN clientes c ON c.id_cliente = t.id_cliente "
                + "LEFT JOIN equipos e ON e.id_equipo = t.id_equipo "
                + "JOIN categorias cat ON cat.id_categoria = t.id_categoria "
                + "LEFT JOIN tecnicos tec ON tec.id_tecnico = t.id_tecnico "
                + "WHERE (? IS NULL OR t.estado = ?) AND (? IS NULL OR t.prioridad = ?) "
                + "ORDER BY t.fecha_creacion DESC LIMIT ? OFFSET ?";
        List<TicketDetalleDTO> resultado = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado); ps.setString(2, estado);
            ps.setString(3, prioridad); ps.setString(4, prioridad);
            ps.setInt(5, Math.max(1, limite)); ps.setInt(6, Math.max(0, offset));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TicketDetalleDTO ticket = new TicketDetalleDTO();
                    ticket.setIdTicket(rs.getLong("id_ticket"));
                    ticket.setCliente(rs.getString("cliente"));
                    ticket.setEquipo(rs.getString("equipo"));
                    ticket.setCategoria(rs.getString("categoria"));
                    ticket.setTecnico(rs.getString("tecnico"));
                    ticket.setTitulo(rs.getString("titulo"));
                    ticket.setPrioridad(rs.getString("prioridad"));
                    ticket.setEstado(rs.getString("estado"));
                    if (rs.getTimestamp("fecha_creacion") != null) {
                        ticket.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                    }
                    resultado.add(ticket);
                }
            }
            return resultado;
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar el detalle de tickets.", ex);
        }
    }

    @Override
    public int contar(String estado, String prioridad) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE (? IS NULL OR estado = ?) AND (? IS NULL OR prioridad = ?)";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado); ps.setString(2, estado);
            ps.setString(3, prioridad); ps.setString(4, prioridad);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible contar los tickets.", ex);
        }
    }

    @Override
    public boolean actualizarEstado(long idTicket, String estadoEsperado, String nuevoEstado) {
        String sql = "UPDATE tickets SET estado = ? WHERE id_ticket = ? AND estado = ?";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado); ps.setLong(2, idTicket); ps.setString(3, estadoEsperado);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible actualizar el estado del ticket.", ex);
        }
    }

    public List<OpcionCatalogo> listarClientesActivos() { return listarOpciones("SELECT id_cliente, nombre FROM clientes WHERE activo = TRUE ORDER BY nombre", "id_cliente"); }
    public List<OpcionCatalogo> listarCategorias() { return listarOpciones("SELECT id_categoria, nombre FROM categorias ORDER BY nombre", "id_categoria"); }
    public List<OpcionCatalogo> listarTecnicosActivos() { return listarOpciones("SELECT id_tecnico, nombre FROM tecnicos WHERE activo = TRUE ORDER BY nombre", "id_tecnico"); }
    public List<OpcionCatalogo> listarEquiposPorCliente(long idCliente) {
        String filtro = idCliente > 0 ? " AND id_cliente = ?" : "";
        return listarOpciones("SELECT id_equipo, CONCAT(codigo_inventario, ' | ', tipo, ' ', COALESCE(marca, ''), ' ', COALESCE(modelo, '')) AS nombre FROM equipos WHERE activo = TRUE" + filtro + " ORDER BY codigo_inventario", "id_equipo", idCliente > 0 ? new long[] {idCliente} : new long[0]);
    }

    public long clienteActivoPorCorreo(String correo) {
        String sql = "SELECT id_cliente FROM clientes WHERE correo = ? AND activo = TRUE";
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getLong(1) : 0; }
        } catch (SQLException ex) { throw new IllegalStateException("No fue posible validar el cliente.", ex); }
    }

    public boolean existeClienteActivo(long id) { return existe("SELECT 1 FROM clientes WHERE id_cliente = ? AND activo = TRUE", id); }
    public boolean existeCategoria(long id) { return existe("SELECT 1 FROM categorias WHERE id_categoria = ?", id); }
    public boolean existeTecnicoActivo(long id) { return existe("SELECT 1 FROM tecnicos WHERE id_tecnico = ? AND activo = TRUE", id); }

    private boolean existe(String sql, long id) {
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { throw new IllegalStateException("No fue posible validar la relacion.", ex); }
    }

    private List<OpcionCatalogo> listarOpciones(String sql, String idColumna, long... parametro) {
        List<OpcionCatalogo> opciones = new ArrayList<>();
        try (Connection cn = ConexionBD.abrir(); PreparedStatement ps = cn.prepareStatement(sql)) {
            if (parametro.length > 0) ps.setLong(1, parametro[0]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) opciones.add(new OpcionCatalogo(rs.getLong(idColumna), rs.getString("nombre")));
            }
            return opciones;
        } catch (SQLException ex) { throw new IllegalStateException("No fue posible cargar el catalogo.", ex); }
    }

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