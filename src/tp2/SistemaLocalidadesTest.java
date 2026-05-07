package tp2;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SistemaLocalidadesTest {

    private SistemaLocalidades sistema;

    @Before
    public void setUp() {
        sistema = new SistemaLocalidades();
    }

    @Test
    public void testGrafoConMenosDeDosNodosNoGeneraConexiones() {
        sistema.agregarLocalidad(new Localidad("Unica", "Provincia", 0, 0));
        List<Conexion> agm = sistema.planificarConexiones(100, 20, 5000);
        
        assertTrue("El AGM debe estar vacío si hay menos de 2 nodos", agm.isEmpty());
    }

    @Test
    public void testKruskalEvitaCiclosYEligeMenorCosto() {
        Localidad l1 = new Localidad("Punto 1", "Prov", -34.0, -58.0);
        Localidad l2 = new Localidad("Punto 2", "Prov", -34.1, -58.0);
        Localidad l3 = new Localidad("Punto 3", "Prov", -34.2, -58.0);
        
        sistema.agregarLocalidad(l1);
        sistema.agregarLocalidad(l2);
        sistema.agregarLocalidad(l3);
        
        List<Conexion> agm = sistema.planificarConexiones(100, 20, 5000);
        
        assertEquals("Debe tener N-1 aristas", 2, agm.size());
        
        for (Conexion con : agm) {
            boolean esConexionLarga = (con.getOrigen().equals(l1) && con.getDestino().equals(l3)) || 
                                      (con.getOrigen().equals(l3) && con.getDestino().equals(l1));
            assertFalse("Kruskal no debería elegir la arista más costosa que cierra el ciclo", esConexionLarga);
        }
    }
}