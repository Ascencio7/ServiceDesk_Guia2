/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.model;

import java.util.Objects;

/**
 *
 * @author Vladimir Ascencio
 */


public abstract class Usuario {
 private final long id;
 private String nombreCompleto;
 private final String correo;
 private final RolUsuario rol;
 private boolean activo;
 protected Usuario(long id, String nombreCompleto,
 String correo, RolUsuario rol) {
 if (id <= 0) {
 throw new IllegalArgumentException("El id debe ser positivo.");
 }
 this.id = id;
 this.nombreCompleto = textoObligatorio(nombreCompleto, "nombre");
 this.correo = textoObligatorio(correo, "correo").toLowerCase();
 this.rol = Objects.requireNonNull(rol, "El rol es obligatorio.");
 this.activo = true;
 }
 public long getId() {
 return id;
 }
 public String getNombreCompleto() {
 return nombreCompleto;
 }
 public void cambiarNombre(String nuevoNombre) {
 this.nombreCompleto = textoObligatorio(nuevoNombre, "nombre");
 }
 public String getCorreo() {
 return correo;
 }
 public RolUsuario getRol() {
 return rol;
 }
 public boolean isActivo() {
 return activo;
 }
 public void activar() {
 this.activo = true;
 }
  public void desactivar() {
 this.activo = false;
 }
 private static String textoObligatorio(String valor, String campo) {
 String limpio = valor == null ? "" : valor.trim();
 if (limpio.isEmpty()) {
 throw new IllegalArgumentException(
 "El campo " + campo + " es obligatorio.");
 }
 return limpio;
 }
}