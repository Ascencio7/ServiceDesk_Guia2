/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.model;

/**
 *
 * @author Vladimir Ascencio
 */
public class Tecnico extends Usuario {
    private String especialidad;
    private int nivelSoporte;
    
    public Tecnico(long id, String nombreCompleto, String correo, String especialidad, int nivelSoporte){
        super(id, nombreCompleto, correo, RolUsuario.TECNICO);
        this.especialidad = especialidad == null ? "General" : especialidad.trim();
        cambiarNivelSoporte(nivelSoporte);
    }
    
    public String getEspecialidad(){
        return especialidad;
    }
    
    public int getNivelSoporte(){
        return nivelSoporte;
    }
    
    
    public void cambiarNivelSoporte(int nuevoNivel){
        if(nuevoNivel < 1 || nuevoNivel > 3){
            throw new IllegalArgumentException("El nivel de soporte debe estar entre 1 y 3");
        }
        this.nivelSoporte = nuevoNivel;
    }
    
    public boolean puedeAtender(PrioridadTicket prioridad){
        if(prioridad == PrioridadTicket.CRITICA){
            return nivelSoporte == 3;
        }
        return isActivo();
    }
    
}
