package ejemplomicronucleo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Servidor extends JFrame implements Runnable
{
    private JTextArea txtMensajes;
    private JPanel pnlMensajes;
    private JButton btnSalir;
    private JScrollPane pScroll; 
    private Micronucleo middleware;
    private int idProceso;
    private boolean activo;
    private Thread hiloServidor; 
    public Servidor(Micronucleo middleware,int idProceso)
    {
        super("Servidor, ID Proceso: "+idProceso);
        this.idProceso=idProceso;
        this.middleware=middleware;
        hiloServidor=new Thread(this);
        activo=true;
        btnSalir=new JButton("Salir");
        txtMensajes=new JTextArea();
        pnlMensajes=new JPanel();
        
        setSize(590,380);
        setLayout(null);
        
        pnlMensajes.setSize(565, 280);
        pnlMensajes.setLocation(5,5);
        pScroll = new JScrollPane(txtMensajes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pScroll.setSize(565, 280);
        pnlMensajes.add( pScroll);
        pnlMensajes.setLayout(null); 
        btnSalir.setSize(120,40);
        btnSalir.setLocation(230,295);
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we)
            {
                activo=false;
                notificar();
            }
        });
         btnSalir.addActionListener(new ActionListener(){        
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
               activo=false;
                notificar();
               getMe().dispose();
            }
            
        });
        
        
        add(pnlMensajes);
        add(btnSalir);
        setVisible(true);
        hiloServidor.start();
    }
    public Servidor getMe()
    {
        return this;
    }
    @Override
    public void run() 
    {
        String respuesta="";
        while(activo)
        {
            byte[] msg=new byte[1024];
            setMensaje("Esperando peticion...");
            msg=middleware.receive(idProceso, msg);
            setMensaje("Peticion recibida de la red, analizando peticion...");
          Mensaje resp=new Mensaje(msg);
            switch(resp.getCodop())
            {
                case 1:
                {
                    setMensaje("La peticion es de saludo, resolviendo peticion..."+resp.getIP());
                    String r="Hola, como estas?";
                    setMensaje("Preparando respuesta...");
                    respuesta=resp.getOrigen()+"|"+idProceso+"|"+"100"+"|"+r;
                    break;
                }
                case 2:
                {
                    setMensaje("La peticion es de nombre, resolviendo peticion...");
                    String r="Mi nombre es Luis";
                    setMensaje("Preparando respuesta...");
                    respuesta=resp.getOrigen()+"|"+idProceso+"|"+"200"+"|"+r;
                    break;
                }
                case 3:
                {
                    setMensaje("La peticion es de despedida, resolviendo peticion...");
                    String r="Adios, bonito dia!";
                    setMensaje("Preparando respuesta...");
                    respuesta=resp.getOrigen()+"|"+idProceso+"|"+"300"+"|"+r;
                    break;
                }
            }
            setMensaje("Enviando peticion por la red...");
            middleware.send(resp.getOrigen(),resp.getIP() ,respuesta.getBytes());
                            
        }
    }
      public void setMensaje(String cad)
    {
        txtMensajes.append(cad+"\n");
    }
    public void notificar()
    {
        synchronized(hiloServidor)
        {
            hiloServidor.notify();
        }
    }
}
