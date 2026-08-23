/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.service;
import java.util.Optional;
import sv.edu.itca.servicedesk360.model.CuentaUsuario;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.storage.BuscadorCuentas;


/**
 *
 * @author Vladimir Ascencio
 */
public class ServicioAutenticacion implements Autenticador {
    private final BuscadorCuentas buscador;
 public ServicioAutenticacion(BuscadorCuentas buscador) {
 this.buscador = buscador;
  }
 @Override
 public Optional<Usuario> autenticar(String correo, String clave) {
 String hashRecibido = SeguridadClave.generarHash(
 clave == null ? "" : clave);
 Optional<CuentaUsuario> cuenta =
 buscador.buscadorPorCorreo(correo);
 if (!cuenta.isPresent()) {
 return Optional.empty();
 }
 CuentaUsuario encontrada = cuenta.get();
 boolean coincide = encontrada.getHashClave().equals(hashRecibido);
 boolean activa = encontrada.getUsuario().isActivo();
 return coincide && activa
 ? Optional.of(encontrada.getUsuario())
 : Optional.empty();
 }
}
