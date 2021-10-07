/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class Banco {
    private String nombre;
    private List<Cliente> clientes;
    
    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList();
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Cliente> getClientes() {
        return this.clientes;
    }
    
    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }
    
    public Cliente buscarClientePorNombre(String nombreCliente, String pin) {
        for ( int i = 0; i < clientes.size(); i++) {
            Cliente cli = clientes.get(i); // Sacando elemento por elemento
            if (cli.getNombre().equals(nombreCliente) && cli.getPinSeguridad().equals(pin)) {
                return cli;
            }
        }
        return null; //TODO Cambiar la funcionalidad por Optional para evitar NullPointerException
    }
}
