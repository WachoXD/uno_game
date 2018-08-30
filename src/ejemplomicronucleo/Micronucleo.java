package ejemplomicronucleo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Micronucleo extends JFrame
{
    private JButton btnServidor,btnCliente,btnSalir;
    private DatagramPacket dp;
    private DatagramSocket dsReceive, dsSend;
    private InetAddress ipServidor;
    private final static short ENVIO = 6969;
    private final static short RECEPCION = 6968;
    private Hashtable tablaLocal,tablaRemota;
     private JTextArea txtMensajes;
     private JScrollPane pScroll; 
    private JPanel pnlMensajes;
    private RecibePaquetes recibePaquetes;
	
    public Micronucleo() 
    {
       super();
       try
       {
            
            setTitle("Micronucleo, IP:"+InetAddress.getLocalHost().getHostAddress());
       }catch (UnknownHostException ue){}
        try
        {
            dsReceive = new DatagramSocket(RECEPCION);
            dsReceive.setSoTimeout(3000);
        } catch(SocketException se)
        {
              setMensaje("No se puede abrir el socket del servidor: " + se);
              System.exit(-1);
        }
        try
        {
            dsSend = new DatagramSocket(ENVIO);
            ipServidor = InetAddress.getByName("127.0.0.1");                   
        } catch(SocketException se)
        {
              setMensaje("No se puede abrir el socket del cliente: " + se);
              System.exit(-1);
        }catch(UnknownHostException uhe)
        {
              setMensaje("No se puede obtener la IP del servidor: " + uhe);
              System.exit(-1); 
          }   
        recibePaquetes=new RecibePaquetes(dsReceive,this);
        tablaLocal=new Hashtable();
        tablaRemota=new Hashtable();
        btnServidor=new JButton ("Crear Servidor");
        btnCliente=new JButton("Crear Ciiente");
        btnSalir=new JButton("Salir");
        txtMensajes=new JTextArea();
        pnlMensajes=new JPanel();
        btnServidor.setSize(150,50);
        btnServidor.setLocation(5,40);
        btnCliente.setSize(150,50);
        btnCliente.setLocation(5,100);
        btnSalir.setSize(150,50);
        btnSalir.setLocation(5,160);
        pnlMensajes.setSize(360, 200);
        pnlMensajes.setLocation(180,25);
        pScroll = new JScrollPane(txtMensajes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pScroll.setSize(360, 200);
        pnlMensajes.add( pScroll);
        pnlMensajes.setLayout(null);
        txtMensajes.setEditable(false);
        
        setSize(590,280);
        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension d=tk.getScreenSize();
        setLocation((d.width-getSize().width)/2,(d.height-getSize().height)/2);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e)
		{
			tablaLocal.clear();
			tablaRemota.clear();
			recibePaquetes.paraHilo();
			System.exit(0);		
		}
        });
            btnSalir.addActionListener(new ActionListener(){        
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
               	tablaLocal.clear();
			tablaRemota.clear();
			recibePaquetes.paraHilo();
			System.exit(0);
            }
            
        });
        btnCliente.addActionListener(new ActionListener(){        
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
               int valor=(int)(Math.random()*Integer.MAX_VALUE);
                new Cliente(getMe(),valor);
                setMensaje("Se ha creado un cliente con el ID: "+valor);
            }
            
        });
        
        btnServidor.addActionListener(new ActionListener(){        
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                int valor=(int)(Math.random()*Integer.MAX_VALUE);
                new Servidor(getMe(),valor);
                setMensaje("Se ha creado un servidor con el ID: "+valor);
            }
            
        });
        add(btnServidor);
        add(btnCliente);
        add(btnSalir);
        add(pnlMensajes);
        setVisible(true);
        recibePaquetes.start();
        
    }
    public void almacenaDestinoRemoto(String ip, int id)
    {
		tablaRemota.put(new Integer(id),new DatosServidor(id,ip));
    }
    public Micronucleo getMe()
    {
        return this;
    }
    public boolean send(int idDestino,String IP,byte[] msg)
    {
        DatosServidor datos;
        //buscamos en tabla sde servidores locales
        setMensaje("Se ha solicitado un envio de mensaje por la red...");
        setMensaje("Buscando destino en tabla de servidores locales...");
        if (tablaLocal.containsKey(new Integer(idDestino)))
	{
            setMensaje("Destino encontrado en servidores locales...");
            try 
            {
                setMensaje("Extrayendo IP de destino...");
                ipServidor = InetAddress.getByName("127.0.0.1");
            } catch (UnknownHostException ex) 
            {
                setMensaje("Error al obtener el destino");
                return false;
            }
        }
	else
	{
            //buscamos en tabla de servidores remotos
             setMensaje("Buscando destino en tabla de servidores remotos...");
            if (tablaRemota.containsKey(new Integer(idDestino)))
            {
                setMensaje("Destino encontrado en servidores remotos...");
		datos=(DatosServidor)tablaRemota.get(new Integer(idDestino));
		try
        	{
                    setMensaje("Extrayendo IP de destino...");
                    ipServidor = InetAddress.getByName(datos.getIP());                   
                } catch (UnknownHostException ex) 
                {
                    setMensaje("Error al obtener el destino");
                    return false;
                }
            }
            else
            {
                //no se encuentra en ninguna de las tablas locales oremotas
                setMensaje("Buscando en la red el destino...");
		try
        	{
                    ipServidor = InetAddress.getByName(IP);                   
                }catch (UnknownHostException ex) 
                {
                    System.out.println("Error al obtener el destino");
                    return false;
                }
          	datos=new DatosServidor(idDestino,ipServidor.getHostAddress());
                setMensaje("Guardando destino en tabla de servidores remotos...");
                tablaRemota.put(new Integer(idDestino),datos);
            }
                setMensaje("Creando mensaje a ser enviado...");
                DatagramPacket dp = new DatagramPacket (msg,msg.length,ipServidor,RECEPCION);
                try {
                    setMensaje("Enviando mensaje por la red...");
                    dsSend.send(dp);
                } catch (IOException ex) {
                    System.out.println("Error al enviar el mensaje por la red");
                    return false;
                }
            
        }
         return true;
    }
    public DatagramPacket receive(int id, byte mensaje[])
    {
	DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length);
        ProcesoEspera espera = new ProcesoEspera(id,null);
        recibePaquetes.poner(espera);
        synchronized(espera)
        {
            try
            {
                espera.wait();
                return espera.getMensaje();
                
            } catch(Exception e){}
        }  
        return null;
    }
    public void setMensaje(String cad)
    {
        txtMensajes.append(cad+"\n");
    }
    public static void main(String[] args) 
    {
        new Micronucleo();
    }
    
}
