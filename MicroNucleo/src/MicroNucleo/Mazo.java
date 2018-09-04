/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroNucleo;

import java.util.ArrayList;
import java.util.List;


public class Mazo{
    public String ultimaJugada = "";
    private  List<String> mazo = new ArrayList<String>();
    public Mazo(){
        mazo.add("Carta 1");
        mazo.add("Carta 2");
        mazo.add("Carta 3");
        mazo.add("Carta 4");
        mazo.add("Carta 5");
        mazo.add("Carta 6");
        mazo.add("Carta 7");
        mazo.add("Carta 8");
        mazo.add("Carta 9");
        mazo.add("Carta 10");
        mazo.add("Carta 11");
        mazo.add("Carta 12");
        mazo.add("Carta 13");
        mazo.add("Carta 14");
    }
    
    public String tomarCarta(int numero){
        if(maximo() > 0){
            String res = mazo.get(numero);
            mazo.remove(numero);
            return res;
        }else{
            return "Fin";
        }
    }
    public int maximo(){
        System.out.println("Quedan " + mazo.size());
        return mazo.size();
    }
}
