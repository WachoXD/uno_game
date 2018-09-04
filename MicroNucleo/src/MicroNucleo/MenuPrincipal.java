package MicroNucleo;

import com.sun.security.ntlm.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Hades
 *
 */
public class MenuPrincipal extends JFrame {

    public static void main(String[] args) throws InterruptedException {
        new MenuPrincipal();
    }

    public MenuPrincipal() {
        setBounds(0, 0, 800, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton crearCliente = new JButton("Crear Cliente");
        crearCliente.setBounds(10, 30, 300, 40);
        crearCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Cliente(10011)).start();
            }
        });
        add(crearCliente);

        JButton crearServidor = new JButton("Crear Servidor");
        crearServidor.setBounds(10, 80, 300, 40);
        crearServidor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("Servidor Iniciado");
                    System.out.print("Ip Local: ");
                    try {
                        System.out.println(InetAddress.getLocalHost());
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    new Thread(new Servidor(10011)).start();
                    Thread.sleep(100);
                    new Thread(new Servidor(10012)).start();
                    Thread.sleep(100);
                    new Thread(new Servidor(10013)).start();
                    Thread.sleep(100);
                    new Thread(new Servidor(10014)).start();
                    Thread.sleep(100);
                    setVisible(false);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(crearServidor);
        

        setVisible(true);
    }
}
