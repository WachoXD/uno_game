/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroNucleo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 * @author Hades
 */
public class CartasCliente {
    List<String> cartas = new ArrayList<String>();
    Cliente cliente;
    public CartasCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    public String tomarCarta(int numero){
        if(maximo() > 0){
            String res = cartas.get(numero);
            cartas.remove(numero);
            return res;
        }else{
            return "No hay";
        }
    }
    
    public void agregarCarta(String carta){
        cartas.add(carta);
        String cartas = "";
        
        for(int i = 0; i < this.cartas.size(); i++){
            cartas += this.cartas.get(i) + ",";
            System.out.println(this.cartas.get(i));
        }
        DefaultListModel listModel = new DefaultListModel();
        for(int i = 0; i < cartas.split(",").length; i++){
            listModel.addElement(cartas.split(",")[i]);
        }
        cliente.misCartas.setModel(listModel);
        
    }
    
    public void quitarCarta(int index){
        cartas.remove(index);
        String cartas = "";
        
        for(int i = 0; i < this.cartas.size(); i++){
            cartas += this.cartas.get(i) + ",";
            System.out.println(this.cartas.get(i));
        }
        DefaultListModel listModel = new DefaultListModel();
        for(int i = 0; i < cartas.split(",").length; i++){
            listModel.addElement(cartas.split(",")[i]);
        }
        cliente.misCartas.setModel(listModel);
        
    }
    
    public int maximo(){
        return cartas.size();
    }
}
