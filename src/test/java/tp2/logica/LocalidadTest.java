package test.java.tp2.logica;

import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.Test;
import main.java.tp2.logica.*;

public class LocalidadTest {

    @Test
    public void testDistanciaEnKm() {
        Localidad puntoA = new Localidad("A", "Provincia 1", 0.0, 0.0, Color.BLACK);
        Localidad puntoB = new Localidad("B", "Provincia 1", 1.0, 0.0, Color.BLACK);
        
        double distancia = puntoA.distanciaEnKm(puntoB);
        
        assertEquals(111.19, distancia, 0.5);
    }

    @Test
    public void testIgualdadLocalidades() {
        Localidad loc1 = new Localidad("Buenos Aires", "CABA", -34.6, -58.3, Color.BLUE);
        Localidad loc2 = new Localidad("Buenos Aires", "Otra", 0.0, 0.0, Color.RED);
        Localidad loc3 = new Localidad("Cordoba", "CABA", -34.6, -58.3, Color.BLUE);
        
        assertTrue(loc1.equals(loc2));
        assertFalse(loc1.equals(loc3));
    }
}