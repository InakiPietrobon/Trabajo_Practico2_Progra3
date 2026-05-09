package test.java.tp2.logica;

import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.Test;
import main.java.tp2.logica.*;

public class RedLocalidadesTest {

    @Test
    public void testAgregarLocalidadNueva() {
        RedLocalidades red = new RedLocalidades();
        Localidad loc = new Localidad("Mendoza", "Mendoza", 0, 0, Color.RED);
        
        boolean agregada = red.agregarLocalidad(loc);
        
        assertTrue(agregada);
        assertEquals(1, red.getLocalidades().size());
    }

    @Test
    public void testEvitarDuplicados() {
        RedLocalidades red = new RedLocalidades();
        Localidad loc1 = new Localidad("Salta", "Salta", 0, 0, Color.RED);
        Localidad loc2 = new Localidad("Salta", "Salta", 10, 10, Color.BLUE);
        
        red.agregarLocalidad(loc1);
        boolean agregada = red.agregarLocalidad(loc2);
        
        assertFalse(agregada);
        assertEquals(1, red.getLocalidades().size());
    }
}