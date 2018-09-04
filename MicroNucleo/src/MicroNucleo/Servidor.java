/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroNucleo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alumnos
 */
public class Servidor implements Runnable {

    private int PORT = 10011;
    public static Mazo mazo = new Mazo();

    public Servidor(int PORT) {
        this.PORT = PORT;
    }
    
    

    @Override
    public void run() {

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        DatagramSocket socket2 = null;

        while (true) {

            try {

                byte inputBuffer[] = new byte[32];
                DatagramPacket datagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
                socket.receive(datagramPacket);

                int portIncoming = datagramPacket.getPort();
                InetAddress address = datagramPacket.getAddress();

                String message = new String(inputBuffer);
                message = message.trim();
                ComandosServidor comandos = new ComandosServidor(inputBuffer, datagramPacket, socket, address, PORT);
                comandos.loop(message);
                

            } catch (Exception ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
