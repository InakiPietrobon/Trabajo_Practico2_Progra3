package main.java.tp2.logica;

import java.util.ArrayList;
import java.util.List;

public class RedLocalidades {
    private List<Localidad> localidades;
    
    public RedLocalidades() { this.localidades = new ArrayList<>(); }
    
    public boolean agregarLocalidad(Localidad l) { 
        if (!localidades.contains(l)) { 
            localidades.add(l); 
            return true;
        }
        return false; 
    }
    
    public List<Localidad> getLocalidades() { return localidades; }
    public void setLocalidades(List<Localidad> nuevasLocalidades) { this.localidades = nuevasLocalidades; }
}