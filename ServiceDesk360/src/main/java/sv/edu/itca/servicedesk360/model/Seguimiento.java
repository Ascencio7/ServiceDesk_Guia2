package sv.edu.itca.servicedesk360.model;

import java.time.LocalDateTime;

public class Seguimiento {
    private long idSeguimiento;
    private long idTicket;
    private String detalle;
    private LocalDateTime fechaRegistro;

    public long getIdSeguimiento() { return idSeguimiento; }
    public void setIdSeguimiento(long idSeguimiento) { this.idSeguimiento = idSeguimiento; }
    public long getIdTicket() { return idTicket; }
    public void setIdTicket(long idTicket) { this.idTicket = idTicket; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}