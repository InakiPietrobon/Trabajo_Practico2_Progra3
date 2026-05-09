package test.java.tp2.logica;

import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.Before;
import org.junit.Test;
import main.java.tp2.logica.*;

public class ConexionTest {

    private Localidad locOrigen;
    private Localidad locDestinoCerca;
    private Localidad locDestinoLejos;
    private Localidad locDestinoOtraProvincia;

    @Before
    public void setUp() {
        locOrigen = new Localidad("A", "Prov 1", 0.0, 0.0, Color.BLACK);
        locDestinoCerca = new Localidad("B", "Prov 1", 1.0, 0.0, Color.BLACK);
        locDestinoLejos = new Localidad("C", "Prov 1", 4.0, 0.0, Color.BLACK);
        locDestinoOtraProvincia = new Localidad("D", "Prov 2", 1.0, 0.0, Color.BLACK);
    }

    @Test
    public void testCostoNormal() {
        Conexion con = new Conexion(locOrigen, locDestinoCerca);
        double costo = con.tasarCosto(10.0, 20.0, 5000.0);
        
        assertEquals(1111.9, costo, 5.0);
    }

    @Test
    public void testCostoConAumentoPorDistancia() {
        Conexion con = new Conexion(locOrigen, locDestinoLejos);
        double costo = con.tasarCosto(10.0, 20.0, 5000.0);

        assertEquals(5336.4, costo, 10.0);
    }

    @Test
    public void testCostoInterprovincial() {
        Conexion con = new Conexion(locOrigen, locDestinoOtraProvincia);
        double costo = con.tasarCosto(10.0, 20.0, 5000.0);
        
        assertEquals(6111.9, costo, 5.0);
    }
}