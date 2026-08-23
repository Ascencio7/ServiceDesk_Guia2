/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.service;
import java.util.Optional;
import sv.edu.itca.servicedesk360.model.Usuario;

/**
 *
 * @author Vladimir Ascencio
 */
public interface Autenticador {
    Optional<Usuario> autenticar(String correo, String clave);
}
