/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import sv.edu.itca.servicedesk360.model.CuentaUsuario;
import sv.edu.itca.servicedesk360.model.Solicitante;
import sv.edu.itca.servicedesk360.model.Tecnico;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.storage.BuscadorCuentas;
import sv.edu.itca.servicedesk360.storage.RegistradorCuentas;

/**
 *
 * @author Vladimir Ascencio
 */
public class ServicioRegistro {
private final BuscadorCuentas buscador;
 private final RegistradorCuentas registrador;
 private final ValidadorRegistro validador;
 private final AtomicLong secuencia = new AtomicLong(1);
 public ServicioRegistro(BuscadorCuentas buscador,
 RegistradorCuentas registrador,
ValidadorRegistro validador) {
 this.buscador = buscador;
 this.registrador = registrador;
 this.validador = validador;
 }
 public List<String> registrar(String nombre, String correo,
 String rol, String clave,
String confirmacion) {
 List<String> errores = validador.validar(
 nombre, correo, rol, clave, confirmacion);
 String correoNormalizado = correo == null
 ? "" : correo.trim().toLowerCase();
 if (buscador.existeCorreo(correoNormalizado)) {
 errores.add("El correo ya está registrado.");
 }
 if (!errores.isEmpty()) {
 return errores;
 }
 long id = secuencia.getAndIncrement();
 Usuario usuario;
 if ("TECNICO".equalsIgnoreCase(rol)) {
 usuario = new Tecnico(id, nombre, correoNormalizado,
 "Soporte general", 1);
 } else {
 usuario = new Solicitante(id, nombre, correoNormalizado,
 "", "");
 }
 CuentaUsuario cuenta = new CuentaUsuario(
 usuario, SeguridadClave.generarHash(clave));
 registrador.guardar(cuenta);
 return errores;
 }
}
