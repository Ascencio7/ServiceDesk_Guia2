/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.storage;

import java.util.Optional;
import sv.edu.itca.servicedesk360.model.CuentaUsuario;

/**
 *
 * @author Vladimir Ascencio
 */
public interface BuscadorCuentas {
    Optional<CuentaUsuario> buscadorPorCorreo(String correo);
    boolean existeCorreo(String correo);
}
