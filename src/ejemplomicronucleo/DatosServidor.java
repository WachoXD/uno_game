package ejemplomicronucleo;
public class DatosServidor 
{
    private int idProceso;
    private String IP;
    public DatosServidor(int idProceso,String IP)
    {
        this.IP=IP;
        this.idProceso=idProceso;
    }
    public String getIP()
    {
        return IP;
    }
    public int getID()
    {
        return idProceso;
    }
}
