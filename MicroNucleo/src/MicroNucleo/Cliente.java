package MicroNucleo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Hades
 *
 */
public class Cliente extends JFrame implements Runnable {
    ComandosClientes comandosClientes;
    boolean activo = true;
    private static int PORT = 0;
    private static String IP_SERVER = "";
    private InetAddress address;
    DatagramSocket socket = null;
    
    JLabel accion;
    public JList<String> misCartas;

    public Cliente(int PORT){
        this.PORT = PORT;
        setBounds(0, 0, 800, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        accion = new JLabel();
        accion.setBounds(10, 10, 300, 10);
        add(accion);
        setAlwaysOnTop(true);
        misCartas = new JList<String>();
        misCartas.setBounds(10, 80, 300, 300);
        add(misCartas);
        misCartas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(misCartas.getSelectedValue().toString());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        
        
        JButton tomarCarta = new JButton("Tomar Carta");
        tomarCarta.setBounds(10, 30, 300, 40);
        add(tomarCarta);
        tomarCarta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    comandosClientes.escuchando = false;
                    comandosClientes.enviar("tomar");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JButton ponerCarta = new JButton("Poner Carta");
        ponerCarta.setBounds(10, 400, 300, 40);
        ponerCarta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    comandosClientes.escuchando = false;
                    comandosClientes.enviar("poner " + misCartas.getSelectedValue().toString());
                    comandosClientes.cartasCliente.quitarCarta(misCartas.getSelectedIndex());
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(ponerCarta);
        
        setVisible(true);
    }
    @Override
    public void run() {
        address = null;
        System.out.print("ID de cliente: ");
        Scanner sc = new Scanner(System.in);
        PORT = Integer.valueOf("1001" +  sc.nextLine());
        System.out.print("IP del servidor: ");
        IP_SERVER = sc.nextLine();
        System.out.println("Creando cliente: " + PORT);
        try {
            address = InetAddress.getByName(IP_SERVER);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        
        try {
           
            int i = 0;
            
            try {
                socket = new DatagramSocket(PORT);
            } catch (SocketException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
            comandosClientes = new ComandosClientes(socket, PORT, address, Cliente.this);
            
            new Thread( new Envio()).start();
            Thread.sleep(100);
            

        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    class Envio implements Runnable{
        @Override
        public void run(){
            while(true){
                try {
                    if(comandosClientes.escuchando){
                        comandosClientes.escuchar();
                        Cliente.this.setTitle("Escuchando");
                    }
                    else{
                        Cliente.this.setTitle("Enviando comando");
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }

}
