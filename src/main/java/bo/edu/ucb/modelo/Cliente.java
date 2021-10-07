/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.modelo;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class Cliente {
    private String nombre;
    private String pinSeguridad;
    private List<Cuenta> cuentas;
    
    public Cliente(String nombre, String pinSeguridad) {
        this.nombre = nombre;
        this.pinSeguridad = pinSeguridad;
        this.cuentas = new ArrayList();
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPinSeguridad() {
        return pinSeguridad;
    }

    public void setPinSeguridad(String pinSeguridad) {
        this.pinSeguridad = pinSeguridad;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }


    
    
    
}