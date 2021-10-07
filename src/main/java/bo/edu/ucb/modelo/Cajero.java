/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cajero {
    
    private Banco banco;
    
    public Cajero(Banco banco) {
        this.banco = banco;              
    }
    
    public static List<Object> construirListadoCuentas(Cliente cliente) {
        int tamaño=0;
        String listadoCuentasContenido = "";
        listadoCuentasContenido+=(" Elija una de sus cuentas:\n");
        for ( int i = 0 ; i < cliente.getCuentas().size() ; i ++ ) {
            Cuenta cuenta = cliente.getCuentas().get(i);
            String iteracion=""+(i+1);
            listadoCuentasContenido+=( (iteracion) + ". " + cuenta.getNroCuenta() 
                    + " | Tipo de cuenta: " + cuenta.getTipo()+"\n");
            tamaño++;
        }
        listadoCuentasContenido+="\nSeleccione una opción:";
        List<Object> ListadoCuentas=new ArrayList();
        ListadoCuentas.add(listadoCuentasContenido);
        ListadoCuentas.add(tamaño);
       return ListadoCuentas;
    }
       
}
