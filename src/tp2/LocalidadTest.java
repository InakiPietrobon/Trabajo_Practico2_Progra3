package tp2;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocalidadTest {

    @Test
    public void testDistanciaMismaLocalidadEsCero() {
        Localidad bsas = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
        assertEquals(0.0, bsas.distanciaEnKm(bsas), 0.01);
    }

    @Test
    public void testDistanciaSimetrica() {
        Localidad bsas = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
        Localidad cordoba = new Localidad("Córdoba", "Córdoba", -31.4201, -64.1888);
        
        double ida = bsas.distanciaEnKm(cordoba);
        double vuelta = cordoba.distanciaEnKm(bsas);
        
        assertEquals(ida, vuelta, 0.001);
    }

    @Test
    public void testDistanciaRealAproximada() {
        Localidad bsas = new Localidad("Buenos Aires", "Buenos Aires", -34.6037, -58.3816);
        Localidad cordoba = new Localidad("Córdoba", "Córdoba", -31.4201, -64.1888);
        
        double distancia = bsas.distanciaEnKm(cordoba);

        assertEquals(647.0, distancia, 5.0);
    }
}