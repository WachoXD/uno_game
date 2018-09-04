/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroNucleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hades
 */
public class ComandosClientes {
    Cliente cliente;
    DatagramSocket socket;
    int PORT;
    InetAddress address;
    String mensaje = "";
    String ultimoMovimiento = "";
    CartasCliente cartasCliente;
    
    
    /* ESTO ES LO ÚNICO QUE NOS IMPORTA */
    
    
    public void loopRecepcion() throws InterruptedException, IOException{
        if(!mensaje.toLowerCase().contains("fin") && !mensaje.toLowerCase().contains("ok")){
            cartasCliente.agregarCarta(mensaje);
        }
    }

    
    /* ESTO NO NOS IMPORTA */
    public ComandosClientes(DatagramSocket socket, int PORT, InetAddress address, Cliente cliente) {
        this.socket = socket;
        this.PORT = PORT;
        this.address = address;
        this.cliente = cliente;
        cartasCliente = new CartasCliente(cliente);
    }
    
    static boolean escuchando = true;
    public void escuchar() throws IOException, InterruptedException{
        if(escuchando){
            byte[]message = new byte[32];
            message = "ultima".getBytes();

            DatagramPacket packet;
            packet = new DatagramPacket(message, message.length, address, PORT);
            try {
                socket.send(packet);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            message = new byte[32];
            packet = new DatagramPacket(message, message.length, address, PORT);
            packet = new DatagramPacket(message, message.length);
            socket.receive(packet);
            mensaje = new String(packet.getData());
            cliente.accion.setText(mensaje);
            
        }else{
            System.out.println("Escucha detenida");
        }
        Thread.sleep(10);
    }
    
    protected void enviar(String res) throws IOException, InterruptedException{
        escuchando = false;
        System.out.println("Enviado: " + res);
        System.out.println("<mensaje>" + mensaje + "</mensaje>");
        byte [] inputBuffer = new byte[32];
        inputBuffer = res.getBytes();
        DatagramPacket packet;
        packet = new DatagramPacket(inputBuffer, inputBuffer.length, address, PORT);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte [] message = new byte[32];
        packet = new DatagramPacket(message, message.length, address, PORT);
        message = new byte[32];
        socket.receive(packet);
        mensaje = new String(packet.getData());
        System.out.println("Mensaje recibido <mensaje>" + mensaje + "</mensaje>");
        loopRecepcion();
        Thread.sleep(100);
        escuchando = true;
    }
    
    
}
