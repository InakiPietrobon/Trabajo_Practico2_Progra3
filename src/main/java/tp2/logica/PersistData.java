package main.java.tp2.logica;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistData {
    
    public static class DatosGuardados {
        public List<Localidad> localidades = new ArrayList<>();
        public List<Conexion> manuales = new ArrayList<>();
        public List<Conexion> optimas = new ArrayList<>();
    }

    public static void guardarRed(List<Localidad> localidades, List<Conexion> manuales, List<Conexion> optimas, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) archivo.createNewFile();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Localidad l : localidades) {
                String linea = "LOC;" + l.getNombre() + ";" + l.getProvincia() + ";" + l.getLatitud() + ";" + l.getLongitud() + ";" + l.getColor().getRGB();
                bw.write(linea); bw.newLine();
            }
            for (Conexion c : manuales) {
                String linea = "CON;" + c.getOrigen().getNombre() + ";" + c.getDestino().getNombre();
                bw.write(linea); bw.newLine();
            }
            for (Conexion c : optimas) {
                String linea = "OPT;" + c.getOrigen().getNombre() + ";" + c.getDestino().getNombre();
                bw.write(linea); bw.newLine();
            }
        }
    }

    public static DatosGuardados cargarRed(String rutaArchivo) throws IOException {
        DatosGuardados datos = new DatosGuardados();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return datos;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                
                if (partes[0].equals("LOC") && partes.length == 6) {
                    Color colorGuardado = new Color(Integer.parseInt(partes[5]));
                    datos.localidades.add(new Localidad(partes[1], partes[2], Double.parseDouble(partes[3]), Double.parseDouble(partes[4]), colorGuardado));
                } 
                else if ((partes[0].equals("CON") || partes[0].equals("OPT")) && partes.length == 3) {
                    Localidad origen = buscarPorNombre(datos.localidades, partes[1]);
                    Localidad destino = buscarPorNombre(datos.localidades, partes[2]);
                    if (origen != null && destino != null) {
                        if (partes[0].equals("CON")) {
                            datos.manuales.add(new Conexion(origen, destino));
                        } else {
                            datos.optimas.add(new Conexion(origen, destino));
                        }
                    }
                }
            }
        }
        return datos;
    }

    private static Localidad buscarPorNombre(List<Localidad> lista, String nombre) {
        for (Localidad l : lista) { if (l.getNombre().equals(nombre)) return l; }
        return null;
    }
}