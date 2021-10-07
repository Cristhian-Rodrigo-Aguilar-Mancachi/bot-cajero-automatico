/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

/**
 *
 * @author hp
 */
public class Usuario {
    private Long ID;
    private int estado;
    private double[] variables ={0,0};

    public Usuario(Long ID, int estado) {
        this.ID = ID;
        this.estado = estado;
    }

    public Long getID() {
        return ID;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    
    
}
