package tp2;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConexionTest {

    private double costoKm = 100.0;
    private double porcentaje = 20.0; // 20%
    private double costoProvincia = 5000.0;

    @Test
    public void testCostoSinPenalizaciones() {
        Localidad l1 = new Localidad("San Miguel", "Buenos Aires", -34.54, -58.71);
        Localidad l2 = new Localidad("Muñiz", "Buenos Aires", -34.55, -58.70);
        
        Conexion con = new Conexion(l1, l2, costoKm, porcentaje, costoProvincia);
        double distancia = l1.distanciaEnKm(l2);
        
        assertEquals(distancia * costoKm, con.getCostoTotal(), 0.01);
    }

    @Test
    public void testCostoInterprovincialDistanciaCorta() {
        Localidad l1 = new Localidad("Avellaneda", "Buenos Aires", -34.66, -58.36);
        Localidad l2 = new Localidad("La Boca", "CABA", -34.63, -58.36);
        
        Conexion con = new Conexion(l1, l2, costoKm, porcentaje, costoProvincia);
        double distancia = l1.distanciaEnKm(l2);
        
        assertEquals((distancia * costoKm) + costoProvincia, con.getCostoTotal(), 0.01);
    }
}