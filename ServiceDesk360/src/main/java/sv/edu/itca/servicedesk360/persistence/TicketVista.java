package sv.edu.itca.servicedesk360.persistence;

import java.time.LocalDateTime;

public final class TicketVista {
    private final long id;
    private final String titulo;
    private final String prioridad;
    private final String estado;
    private final LocalDateTime fechaCreacion;
    private final String cliente;
    private final String categoria;

    public TicketVista(long id, String titulo, String prioridad, String estado,
            LocalDateTime fechaCreacion, String cliente, String categoria) {
        this.id = id; this.titulo = titulo; this.prioridad = prioridad;
        this.estado = estado; this.fechaCreacion = fechaCreacion;
        this.cliente = cliente; this.categoria = categoria;
    }

    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getCliente() { return cliente; }
    public String getCategoria() { return categoria; }
}