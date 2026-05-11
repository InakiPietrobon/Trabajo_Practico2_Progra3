package main.java.tp2.logica;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ManejoArchivos {

    public static class DatosRed {
        public List<Localidad> localidades;
        public List<Conexion> manuales;
        public List<Conexion> optimas;

        public DatosRed(List<Localidad> locs, List<Conexion> man, List<Conexion> opt) {
            this.localidades = locs;
            this.manuales = man;
            this.optimas = opt;
        }
    }

    private static class ColorAdapter extends TypeAdapter<Color> {
        @Override
        public void write(JsonWriter out, Color color) throws IOException {
            if (color == null) out.nullValue();
            else out.value(color.getRGB());
        }
        @Override
        public Color read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull(); return null;
            }
            return new Color(in.nextInt());
        }
    }

    public static void guardarRed(List<Localidad> localidades, List<Conexion> manuales, List<Conexion> optimas, String ruta) throws IOException {
        DatosRed datos = new DatosRed(localidades, manuales, optimas);
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .setPrettyPrinting()
                .create();
                
        String json = gson.toJson(datos);

        FileWriter writer = new FileWriter(ruta);
        try {
            writer.write(json);
        } finally {
            writer.close();
        }
    }

    public static DatosRed cargarRed(String ruta) throws IOException {
        File f = new File(ruta);
        if (!f.exists()) return new DatosRed(new ArrayList<Localidad>(), new ArrayList<Conexion>(), new ArrayList<Conexion>());

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .create();

        BufferedReader br = new BufferedReader(new FileReader(ruta));
        DatosRed ret = null;
        try {
            ret = gson.fromJson(br, DatosRed.class);
        } finally {
            br.close();
        }
        
        return ret;
    }
}