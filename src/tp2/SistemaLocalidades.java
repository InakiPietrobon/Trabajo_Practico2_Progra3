package tp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaLocalidades {
    private List<Localidad> localidades;

    public SistemaLocalidades() {
        this.localidades = new ArrayList<>();
    }

    public void agregarLocalidad(Localidad l) {
        if (!localidades.contains(l)) {
            localidades.add(l);
        }
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    /**
     * Resuelve el Arbol Generador Mínimo usando el algoritmo de Kruskal.
     */
    public List<Conexion> planificarConexiones(double costoPorKm, double porcentajeExceso, double costoInterprovincial) {
        List<Conexion> arbolGeneradorMinimo = new ArrayList<>();
        
        // Si hay menos de 2 localidades, no se pueden hacer conexiones
        if (localidades.size() < 2) {
            return arbolGeneradorMinimo;
        }

        // 1. Generar todas las aristas posibles (Grafo Completo)
        List<Conexion> todasLasConexiones = new ArrayList<>();
        for (int i = 0; i < localidades.size(); i++) {
            for (int j = i + 1; j < localidades.size(); j++) {
                todasLasConexiones.add(new Conexion(localidades.get(i), localidades.get(j), costoPorKm, porcentajeExceso, costoInterprovincial));
            }
        }

        // 2. Ordenar las aristas de menor a mayor costo
        Collections.sort(todasLasConexiones);

        // 3. Inicializar Union-Find para detectar ciclos
        Map<Localidad, Localidad> padre = new HashMap<>();
        for (Localidad l : localidades) {
            padre.put(l, l); // Al principio cada nodo es su propio padre
        }

        // 4. Recorrer las aristas ordenadas y unirlas si no forman ciclos
        int aristasAgregadas = 0;
        for (Conexion conexion : todasLasConexiones) {
            Localidad raizOrigen = buscarRaiz(conexion.getOrigen(), padre);
            Localidad raizDestino = buscarRaiz(conexion.getDestino(), padre);

            // Si las raíces son distintas, no forman ciclo
            if (!raizOrigen.equals(raizDestino)) {
                arbolGeneradorMinimo.add(conexion);
                padre.put(raizOrigen, raizDestino); // Unión de conjuntos
                aristasAgregadas++;

                // Un AGM de N vértices siempre tiene exactamente N-1 aristas
                if (aristasAgregadas == localidades.size() - 1) {
                    break;
                }
            }
        }

        return arbolGeneradorMinimo;
    }

    // Método auxiliar de Union-Find (Busqueda con compresión de caminos)
    private Localidad buscarRaiz(Localidad l, Map<Localidad, Localidad> padre) {
        if (padre.get(l).equals(l)) {
            return l;
        }
        // Compresión de caminos para optimizar futuras búsquedas
        Localidad raiz = buscarRaiz(padre.get(l), padre);
        padre.put(l, raiz);
        return raiz;
    }
}