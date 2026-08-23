/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Vladimir Ascencio
 */
public class TicketSoporte {
    private final long id;
    private final String titulo;
    private final String descripcion;
    private final Solicitante solicitante;
    private final PrioridadTicket prioridad;
    private EstadoTicket estado;
    private Tecnico tecnicoAsignado;
    private final LocalDateTime fechaCreacion;
    
    public TicketSoporte(long id, String titulo, String descripcion, Solicitante solicitante, PrioridadTicket prioridad){
        if (id <= 0) {
 throw new IllegalArgumentException("El id debe ser positivo.");
 }
 this.id = id;
 this.titulo = obligatorio(titulo, "título");
 this.descripcion = obligatorio(descripcion, "descripción");
 this.solicitante = Objects.requireNonNull(
 solicitante, "El solicitante es obligatorio.");
 this.prioridad = Objects.requireNonNull(
 prioridad, "La prioridad es obligatoria.");
 this.estado = EstadoTicket.ABIERTO;
 this.fechaCreacion = LocalDateTime.now();
 }
 public void asignarTecnico(Tecnico tecnico) {
 Objects.requireNonNull(tecnico, "El técnico es obligatorio.");
 if (!tecnico.puedeAtender(prioridad)) {
 throw new IllegalStateException(
 "El técnico no puede atender esta prioridad.");
 }
 this.tecnicoAsignado = tecnico;
 this.estado = EstadoTicket.ASIGNADO;
 }
 public void cambiarEstado(EstadoTicket nuevoEstado) {
 this.estado = Objects.requireNonNull(
 nuevoEstado, "El estado es obligatorio.");
 }
 public long getId() { return id; }
 public String getTitulo() { return titulo; }
 public String getDescripcion() { return descripcion; }
 public Solicitante getSolicitante() { return solicitante; }
 public PrioridadTicket getPrioridad() { return prioridad; }
 public EstadoTicket getEstado() { return estado; }
 public Tecnico getTecnicoAsignado() { return tecnicoAsignado; }
 public LocalDateTime getFechaCreacion() { return fechaCreacion; }
 private static String obligatorio(String valor, String campo) {
 String limpio = valor == null ? "" : valor.trim();
 if (limpio.isEmpty()) {
 throw new IllegalArgumentException(
 "El campo " + campo + " es obligatorio.");
 }
 return limpio;
 }
}