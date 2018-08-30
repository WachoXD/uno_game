package ejemplomicronucleo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente extends JFrame implements Runnable
{
    private Thread hiloCliente;
    private JLabel lblIP,lblID;
    private JTextField txtIP,txtID;
    private JTextArea txtMensajes;
    private JPanel pnlMensajes;
    private JButton btnOp1,btnOp2,btnOp3;
    private JScrollPane pScroll; 
    private Micronucleo middleware;
    private int idProceso,CODOP;
    private boolean activo;
    public Cliente(Micronucleo middleware,int idProceso)
    {
        super("Cliente, ID Proceso: "+idProceso);
        this.idProceso=idProceso;
        this.middleware=middleware;
        activo=true;
        CODOP=0;
        lblIP=new JLabel("IP Servidor:");
        lblID=new JLabel("ID Proceso:");
        txtIP=new JTextField ();
        txtID=new JTextField ();
        btnOp1=new JButton("Operacion 1");
        btnOp2=new JButton("Operacion 2");
        btnOp3=new JButton("Operacion 3");
        txtMensajes=new JTextArea();
        pnlMensajes=new JPanel();
        
        setSize(590,380);
        setLayout(null);
        
        lblIP.setSize(100,25);
        lblIP.setLocation(5,10);
        txtIP.setSize(150,25);
        txtIP.setLocation(105,10);
        lblID.setSize(100,25);
        lblID.setLocation(310,10);
        txtID.setSize(150,25);
        txtID.setLocation(415,10);
        pnlMensajes.setSize(560, 200);
        pnlMensajes.setLocation(5,45);
        pScroll = new JScrollPane(txtMensajes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pScroll.setSize(560, 200);
        pnlMensajes.add( pScroll);
        pnlMensajes.setLayout(null); 
        btnOp1.setSize(120,40);
        btnOp1.setLocation(30,280);
        btnOp2.setSize(120,40);
        btnOp2.setLocation(230,280);
        btnOp3.setSize(120,40);
        btnOp3.setLocation(420,280);
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we)
            {
                activo=false;
                notificar();
            }
        });
        
        btnOp1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                CODOP=1;
                notificar();
            }
        });
        
        btnOp2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                CODOP=2;
                notificar();
            }
        });

        btnOp3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                CODOP=3;
                notificar();
            }
        });

        
        add(lblIP);
        add(txtIP);
        add(lblID);
        add(txtID);
        add(pnlMensajes);
        add(btnOp1);
        add(btnOp2);
        add(btnOp3);
        hiloCliente=new Thread(this);
        hiloCliente.start();
        setVisible(true);
        
    }

    @Override
    public void run() 
    {
        while(activo)
        {
            synchronized(hiloCliente)
            {
                try
                {
                    hiloCliente.wait();
                } catch (InterruptedException ex) {}
            }
            if (!activo)
                return;
            byte [] msg=null;
                            
            switch (CODOP)
            {
                case 1:{ 
                            setMensaje("Seleccionaste la operacion uno, solicitud de saludo..");
                            setMensaje("Preparando mensaje de peticion...");
                            msg= (txtID.getText()+"|"+idProceso+"|"+"1"+"|"+"hola mundo").getBytes();
                            break;
                       }
                case 2:{ 
                            setMensaje("Seleccionaste la operacion dos, solicitud de nombre..");
                            setMensaje("Preparando mensaje de peticion...");
                            msg= (txtID.getText()+"|"+idProceso+"|"+"2"+"|"+"cual es tu nombre").getBytes();
                            break;
                       }
                case 3:{ 
                            setMensaje("Seleccionaste la operacion uno, solicitud de despedida..");
                            setMensaje("Preparando mensaje de peticion...");
                            msg= (txtID.getText()+"|"+idProceso+"|"+"3"+"|"+"adios bye").getBytes();
                            break;
                       }
                        
            }
            setMensaje("Enviando peticion por la red...");
            middleware.send(Integer.parseInt(txtID.getText()), txtIP.getText(),msg);
            setMensaje("Esperando respuesta del servidor...");
            msg=middleware.receive(idProceso,msg);
            setMensaje("Respuesta recibida, analizando respuesta...");
            Mensaje res=new Mensaje(msg);
            setMensaje("Respuesta: "+res.getMensaje());
                            
        }
        
    }
    public void setMensaje(String cad)
    {
        txtMensajes.append(cad+"\n");
    }
    
    public void notificar()
    {
        synchronized(hiloCliente)
        {
            hiloCliente.notify();
        }
    }

    
    
}
