package main.java.tp2.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {
    public List<Conexion> calcularAGM(List<Localidad> localidades, double cKm, double cExc, double cProv) {
        List<Conexion> arbolGeneradorMinimo = new ArrayList<>();
        if (localidades.size() < 2) return arbolGeneradorMinimo;

        List<Conexion> todasLasConexiones = new ArrayList<>();
        for (int i = 0; i < localidades.size(); i++) {
            for (int j = i + 1; j < localidades.size(); j++) {
                todasLasConexiones.add(new Conexion(localidades.get(i), localidades.get(j)));
            }
        }
        
        todasLasConexiones.sort((c1, c2) -> Double.compare(
            c1.tasarCosto(cKm, cExc, cProv),
            c2.tasarCosto(cKm, cExc, cProv)
        ));

        Map<Localidad, Localidad> padre = new HashMap<>();
        for (Localidad l : localidades) padre.put(l, l);

        int aristasAgregadas = 0;
        for (Conexion conexion : todasLasConexiones) {
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