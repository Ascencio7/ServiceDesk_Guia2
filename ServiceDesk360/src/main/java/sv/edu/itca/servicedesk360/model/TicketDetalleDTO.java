package sv.edu.itca.servicedesk360.model;

import java.time.LocalDateTime;

public class TicketDetalleDTO {
    private long idTicket;
    private String cliente;
    private String equipo;
    private String categoria;
    private String tecnico;
    private String titulo;
    private String prioridad;
    private String estado;
    private LocalDateTime fechaCreacion;

    public long getIdTicket() { return idTicket; }
    public void setIdTicket(long idTicket) { this.idTicket = idTicket; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}