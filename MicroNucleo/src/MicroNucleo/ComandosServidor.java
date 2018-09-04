/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroNucleo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gimènez
 */
public class ComandosServidor {
    byte[]inputBuffer;
    DatagramPacket datagramPacket;
    DatagramSocket socket;
    InetAddress address;
    int PORT;
    Mazo mazo;

    
    /****** ESTO ES LO ÚNICO QUE NOS IMPORTA****/
    
    protected void loop(String message){//Aqui van los comandos que se ejecutarán repetitivamente
        if (message.equalsIgnoreCase("Tomar")) {
            entregarCarta();
        }else if(message.equalsIgnoreCase("Ultima")){
            enviar(mazo.ultimaJugada);
        }else if(message.toLowerCase().contains("Poner".toLowerCase())){
            setUltimaJugada(PORT + ":" +  message.split(" ", 2)[1]);
            enviar("OK");
        }
        else {
        } 
    }
    
    
    
    protected void entregarCarta(){
        int n = 0 + (int)(Math.random() * (((mazo.maximo() - 1) - 0) + 1));
        String carta = mazo.tomarCarta(n);
        if(carta.equals("Fin")){
            setUltimaJugada("Ya no quedan cartas");
        }else{
            setUltimaJugada("Jugadores tomando cartas");
        }
        
        System.out.println("Entregando " + carta);
        enviar(carta);
    }
    
    
    
   /****** ESTO NO NOS IMPORTA ****/
    
    protected void enviar(String res){
        inputBuffer = new byte[32];
        inputBuffer = res.getBytes();
        datagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length, address, PORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException ex) {
            Logger.getLogger(ComandosServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ComandosServidor(byte[] inputBuffer, DatagramPacket datagramPacket, DatagramSocket socket, InetAddress address, int PORT) {
        this.inputBuffer = inputBuffer;
        this.datagramPacket = datagramPacket;
        this.socket = socket;
        this.address = address;
        this.PORT = PORT;
        mazo = Servidor.mazo;
    }
    
    protected void setUltimaJugada(String jugada){
        mazo.ultimaJugada = jugada;
    }
}
