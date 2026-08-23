/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.model;

/**
 *
 * @author Vladimir Ascencio
 */
public class PruebaModelo {
 public static void main(String[] args) {
 Solicitante solicitante = new Solicitante(
 1, "Ana López", "ana@empresa.com",
 "Innovación S.A.", "7000-0000");
 Tecnico tecnico = new Tecnico(
 2, "Carlos Pérez", "carlos@soporte.com",
 "Infraestructura", 3);
 TicketSoporte ticket = new TicketSoporte(
 1, "Servidor sin respuesta",
 "El servicio principal no responde.",
          solicitante, PrioridadTicket.CRITICA);
 ticket.asignarTecnico(tecnico);
 ticket.cambiarEstado(EstadoTicket.EN_PROCESO);
 System.out.println(ticket.getTitulo());
 System.out.println(ticket.getEstado());
 System.out.println(ticket.getTecnicoAsignado().getNombreCompleto());
 }
}