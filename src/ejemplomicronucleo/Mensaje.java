package ejemplomicronucleo;

import java.util.StringTokenizer;

public class Mensaje 
{
    private int idDestino,idOrigen,codop;
    private byte[] mensaje;
    private String msg,IP;
    public Mensaje(byte[] mensaje,String IP)
    {
        this.mensaje=mensaje;
        this.IP=IP;
        String tmp=new String(mensaje);
        StringTokenizer st;
        st = new StringTokenizer(tmp,"|");
        idDestino=Integer.parseInt(st.nextToken());
        idOrigen=Integer.parseInt(st.nextToken());
        codop=Integer.parseInt(st.nextToken());
        msg=st.nextToken();
        
    }
    public Mensaje(byte[] mensaje)
    {
        this.mensaje=mensaje;
        this.IP="";
        String tmp=new String(mensaje);
        StringTokenizer st;
        st = new StringTokenizer(tmp,"|");
        idDestino=Integer.parseInt(st.nextToken());
        idOrigen=Integer.parseInt(st.nextToken());
        codop=Integer.parseInt(st.nextToken());
        msg=st.nextToken();
        
    }
    public byte[] getBytes()
    {
        return mensaje;
    }
    public String getIP()
    {
        return IP;
    }
    public int getDestino()
    {
        return idDestino;
    }
    public int getOrigen()
    {
        return idOrigen;
    }
    public int getCodop()
    {
        return codop;
    }
    public String getMensaje()
    {
        return msg;
    }
    
}
