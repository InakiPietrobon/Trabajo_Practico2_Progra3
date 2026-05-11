package test.java.tp2.logica;

import static org.junit.Assert.*;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import main.java.tp2.logica.*;

public class KruskalTest {

    @Test
    public void testGeneracionAGM() {
        Localidad a = new Localidad("A", "Prov 1", 0.0, 0.0, Color.BLACK);
        Localidad b = new Localidad("B", "Prov 1", 1.0, 0.0, Color.BLACK);
        Localidad c = new Localidad("C", "Prov 1", 3.0, 0.0, Color.BLACK);
        
        List<Localidad> localidades = Arrays.asList(a, b, c);
        
        Kruskal kruskal = new Kruskal();
        List<Conexion> agm = kruskal.calcularAGM(localidades, 10.0, 0.0, 0.0);
        
        assertEquals(2, agm.size());
        
        double costoTotal = 0;
        for (Conexion con : agm) {
            costoTotal += con.calcularCosto(10.0, 0.0, 0.0);
        }
        
        assertEquals(3335.0, costoTotal, 10.0);
    }
    
    @Test
    public void testAgmConMenosDeDosLocalidades() {
        Localidad a = new Localidad("A", "Prov 1", 0.0, 0.0, Color.BLACK);
        List<Localidad> localidades = Arrays.asList(a);
        
        Kruskal kruskal = new Kruskal();
        List<Conexion> agm = kruskal.calcularAGM(localidades, 10.0, 0.0, 0.0);
        
        assertTrue(agm.isEmpty());
    }
}