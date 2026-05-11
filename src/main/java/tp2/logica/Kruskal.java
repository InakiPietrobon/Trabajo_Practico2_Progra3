package main.java.tp2.logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {
    
    public List<Conexion> calcularAGM(List<Localidad> localidades, double cKm, double cExc, double cProv) {
        if (localidades.size() < 2) return new ArrayList<>();

        List<Conexion> todasLasConexiones = generarConexionesPosibles(localidades);

        Collections.sort(todasLasConexiones, new Comparator<Conexion>() {
            @Override
            public int compare(Conexion c1, Conexion c2) {
                return Double.compare(c1.calcularCosto(cKm, cExc, cProv), c2.calcularCosto(cKm, cExc, cProv));
            }
        });

        return construirArbolKruskal(todasLasConexiones, localidades);
    }

    private List<Conexion> generarConexionesPosibles(List<Localidad> localidades) {
        List<Conexion> conexiones = new ArrayList<>();
        for (int i = 0; i < localidades.size(); i++) {
            for (int j = i + 1; j < localidades.size(); j++) {
                conexiones.add(new Conexion(localidades.get(i), localidades.get(j)));
            }
        }
        return conexiones;
    }

    private List<Conexion> construirArbolKruskal(List<Conexion> conexionesOrdenadas, List<Localidad> localidades) {
        List<Conexion> arbolGeneradorMinimo = new ArrayList<>();
        Map<Localidad, Localidad> padre = new HashMap<>();
        
        for (Localidad l : localidades) {
            padre.put(l, l);
        }

        int aristasAgregadas = 0;
        for (Conexion conexion : conexionesOrdenadas) {
            Localidad raizOrigen = buscarRaiz(conexion.getOrigen(), padre);
            Localidad raizDestino = buscarRaiz(conexion.getDestino(), padre);

            if (!raizOrigen.equals(raizDestino)) {
                arbolGeneradorMinimo.add(conexion);
                padre.put(raizOrigen, raizDestino); 
                aristasAgregadas++;
                
                if (aristasAgregadas == localidades.size() - 1) break;
            }
        }
        return arbolGeneradorMinimo;
    }

    private Localidad buscarRaiz(Localidad l, Map<Localidad, Localidad> padre) {
        if (padre.get(l).equals(l)) return l;
        Localidad raiz = buscarRaiz(padre.get(l), padre);
        padre.put(l, raiz);
        return raiz;
    }
}