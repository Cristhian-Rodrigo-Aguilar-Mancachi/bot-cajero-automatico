/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import bo.edu.ucb.modelo.Banco;
import bo.edu.ucb.modelo.Cajero;
import bo.edu.ucb.modelo.Cliente;
import bo.edu.ucb.modelo.Cuenta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;/**
*
*/
public class Bot extends TelegramLongPollingBot {
    private int nroCuenta=100000;
    private Map estadoUsuario=new HashMap();
    private Map estadoNombre=new HashMap();
    private Map estadoPin=new HashMap();
    private Map opc=new HashMap();
    private Map clie=new HashMap();
    private Map moneda=new HashMap();
    private Map cuentaElegida=new HashMap();
    private Banco bisa = new Banco("BANCO BISA");
    private Banco banco;
    private Cajero cajero = new Cajero(bisa);
    private Cliente cli;
    
    @Override
    public String getBotToken() {
        return "2046823881:AAG_Bniiwk9k8E_z4gu4ZkeVB4k9KTEuHmo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long userID = update.getMessage().getChatId();
        Integer estado = (Integer) estadoUsuario.get(userID);
        if(estado==null){
            estadoUsuario.put(userID,0);
        }
        System.out.println("Llego mensaje: " + update.toString());
        if(update.hasMessage()){
            String UsuarioMensaje=update.getMessage().getText();
            boolean validador=true;
            while(true){
                estado= (Integer) estadoUsuario.get(userID);
                
                switch(estado){
                    case 0://INICIO DE PROGRAMA
                        mandarMensaje("Bienvenido al banco de la fortuna",update);
                        mandarMensaje("He notado que aun no eres cliente, procedamos a registrarte",update);
                        mandarMensaje("¿Cual es tu nombre completo?",update);
                        estadoUsuario.put(userID,1);
                        validador=true;
                        break;
                    case 1://INGRESA EL NOMBRE
                        if(VerificarNumerosEncaracteres(UsuarioMensaje)==false){
                            estadoNombre.put(userID,UsuarioMensaje);
                            mandarMensaje("Por favor elige un PIN de seguridad, este te sera requerido "
                                    + "cada que ingreses al sistema",update);
                            estadoUsuario.put(userID,2);
                            validador=true;
                        }else{
                            mandarMensaje("El nombre ingresado contiene numeros",update);
                            estadoUsuario.put(userID,0);   
                            validador=false;
                        }
                        break;
                    case 2://INGRESA EL PIN Y SE LO REGISTRA
                        estadoPin.put(userID, UsuarioMensaje);
                        mandarMensaje("Te hemos registrado correctamente",update);
                        Cliente cliente = new Cliente((String)estadoNombre.get(userID),(String)estadoPin.get(userID));
                        bisa.agregarCliente(cliente);
                        clie.put(userID,cliente);
                        estadoUsuario.put(userID,3);
                        validador=false;
                        break;
                    case 3://SE SOLICITA EL PIN PARA ENTRAR A SU CUENTA
                        mandarMensaje("Hola de nuevo "+((String) estadoNombre.get(userID)),update);
                        mandarMensaje("Solo por seguridad ¿cual es tu PIN? ",update);
                        estadoUsuario.put(userID,4);
                        validador=true;
                        break;
                    case 4://VERIFICA EL PIN INGRESADO
                        if(UsuarioMensaje.equals((String) estadoPin.get(userID))){
                            estadoUsuario.put(userID,5);
                        }else{
                            mandarMensaje("Lo siento, el codigo es incorrecto",update);
                            estadoUsuario.put(userID,3);
                        }
                        validador=false;
                        break;
                    case 5://MUESTRA EL MENU DE OPCIONES
                        mandarMensaje("Bienvenido",update);
                        mandarMensaje("Elije una opción:"
                                + "\n"
                                + "1. Ver Saldo\n"
                                + "2. Retirar dinero\n"
                                + "3. Depositar dinero\n"
                                + "4. Crear Cuenta\n"
                                + "5. Salir",update);
                        estadoUsuario.put(userID,6);
                        validador=true;
                        break;
                    case 6://INGRESA AL CAJERO CON LA OPCION ESCOGIDA
                        int opcion=Integer.parseInt(UsuarioMensaje);
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            if(opcion>0 && opcion<6){
                                if(opcion==5){
                                    estadoUsuario.put(userID,3);
                                }else if(opcion!=4){    
                                    estadoUsuario.put(userID,7);
                                    opc.put(userID, opcion);
                                    }else{
                                        estadoUsuario.put(userID,8);
                                    }
                            }else{
                                
                                estadoUsuario.put(userID,5);
                            }
                        }else{
                           estadoUsuario.put(userID,5); 
                        }
                        validador=false;
                        break;
                    case 7: //MUESTRA CUENTAS
                        List<Object> ListadoCuentas=new ArrayList();
                        ListadoCuentas=cajero.construirListadoCuentas((Cliente) clie.get(userID));
                        if((Integer)ListadoCuentas.get(1)==0){
                            mandarMensaje("Usted no tiene cuentas, cree una primero", update);
                            validador=false;
                            estadoUsuario.put(userID,5);
                        }else{
                            String contenido=(String)ListadoCuentas.get(0);
                            mandarMensaje(contenido, update);
                            if((Integer) opc.get(userID)==1){//VER SALDO
                                estadoUsuario.put(userID,11);
                            }else{//DEPOSITO Y RETIRO
                                estadoUsuario.put(userID,12);
                            }
                            validador=true;
                        }
                        break;
                    case 8://SELECCIONA LA MONEDA PARA UNA NUEVA CUENTA
                        mandarMensaje("Seleccione la moneda", update);
                        mandarMensaje("1. Dolares\n"
                                    + "2. Bolivianos", update);
                        estadoUsuario.put(userID,9);
                        validador=true;
                        break;
                    case 9://SELECCIONA EL TIPO PARA UNA NUEVA CUENTA
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            moneda.put(userID,Integer.parseInt(UsuarioMensaje));
                            mandarMensaje("Seleccione el tipo de cuenta:", update);
                            mandarMensaje("1. Caja Ahorros\n"
                                        + "2. Cuenta Corriente", update);
                            estadoUsuario.put(userID,10);
                            validador=true;
                        }else{
                           estadoUsuario.put(userID,5);
                           validador=false;
                        }
                        break;
                    case 10://CREA LA CUENTA
                        int tip=Integer.parseInt(UsuarioMensaje);
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            String tipo;nroCuenta++;
                            List<String> monedas=new ArrayList();
                            if((Integer) moneda.get(userID)==1){
                                monedas.add(" dolares ");monedas.add("USD");
                            }else{
                                monedas.add(" bolivianos ");monedas.add("BOB");
                            }if(tip==1){
                                tipo="Caja Ahorros";
                            }else{
                                tipo="Cuenta Corriente";
                            }
                            mandarMensaje("Se le ha creado una cuenta en"+monedas.get(0)+"con saldo cero"
                                    + ", cuyo numero es: "+nroCuenta, update);
                            Cuenta ctaCliente=new Cuenta(monedas.get(1),(""+nroCuenta),tipo,0);
                            Cliente cli=(Cliente) clie.get(userID);
                            cli.agregarCuenta(ctaCliente);
                            estadoUsuario.put(userID,5);
                        }else{
                           estadoUsuario.put(userID,5);
                        }
                        validador=false;
                        break;
                    case 11://MUESTRA EL SALDO DE LA CUENTA SELECCIONADA
                        opcion=Integer.parseInt(UsuarioMensaje);
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            cli=(Cliente) clie.get(userID);
                            Cuenta cuenta = cli.getCuentas().get(opcion - 1);
                            mandarMensaje("Ver saldo"
                                        + "\nCliente: "+cli.getNombre()+
                                        "\nNro Cuenta: "+cuenta.getNroCuenta()+
                                        "\nSaldo: "+cuenta.getMoneda()+
                                        " "+cuenta.getSaldo(), update);
                            estadoUsuario.put(userID,5);
                        }else{
                           estadoUsuario.put(userID,5);
                        }
                        validador=false;
                        break;
                    case 12://SOLICITAR MONTO
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            cuentaElegida.put(userID,Integer.parseInt(UsuarioMensaje));
                            mandarMensaje("Ingrese el monto: ", update);
                            if((Integer)opc.get(userID)==2){//RETIRAR
                                estadoUsuario.put(userID,13);
                            }else{//DEPOSITAR
                                estadoUsuario.put(userID,14);

                            }
                            validador=true;
                        }else{
                           estadoUsuario.put(userID,5);
                           validador=false;
                        }
                        break;
                    case 13://RETIRO 
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            double monto=Integer.parseInt(UsuarioMensaje);
                            cli=(Cliente) clie.get(userID);
                            if(!cli.getCuentas().get((Integer)cuentaElegida.get(userID) -1).retirar(monto)){
                                mandarMensaje("No se pudo realizar el retiro:", update);
                            }
                            estadoUsuario.put(userID,5);
                        }else{
                           estadoUsuario.put(userID,5);
                        }
                        validador=false;
                        break;
                    case 14://DEPOSITO
                        if(VerificarLetrasEncaracteres(UsuarioMensaje)==false){
                            double monto=Integer.parseInt(UsuarioMensaje);
                            cli=(Cliente) clie.get(userID);
                            if(!cli.getCuentas().get((Integer)cuentaElegida.get(userID) -1).depositar(monto)){
                                mandarMensaje("No se pudo realizar el deposito:", update);
                            }
                            estadoUsuario.put(userID,5);
                        }else{
                           estadoUsuario.put(userID,5);
                        }
                        validador=false;
                        break;
                }
                if(validador){
                    break;
                }
            }
        }
    }
    public void mandarMensaje(String mensaje, Update update){
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(mensaje);
        try {
            execute(message); // Envia el mensaje
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
       
    public boolean VerificarNumerosEncaracteres(String c){
        for(int i=0;i<c.length();i++){
            if(Character.isDigit(c.charAt(i))){
                return true;
            }
        }
        return false;
    }
    public boolean VerificarLetrasEncaracteres(String c){
        for( int i = 0; i < c.length(); i++ )
            if( !Character.isDigit( c.charAt( i ) ) )
                return true;
        return false;
    }
    
    @Override
    public String getBotUsername() {
        return "cristhian_atm_bot";
    }
}

    
    
    