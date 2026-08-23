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
public class CuentaUsuario {
    private final Usuario usuario;
    private final String hashClave;
    
    public CuentaUsuario(Usuario usuario, String hashClave){
        this.usuario = Objects.requireNonNull(usuario, "El usuario es obligatorio.");
        
        if(hashClave == null || hashClave.trim().isEmpty()){
            throw new IllegalArgumentException("El hash es obligatorio.");
        }
        this.hashClave = hashClave;
    }
    
    public Usuario getUsuario(){
        return usuario;
    }
    
    public String getHashClave(){
        return hashClave;
    }
    
}
