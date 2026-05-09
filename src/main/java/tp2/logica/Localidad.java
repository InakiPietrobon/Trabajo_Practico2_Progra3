package main.java.tp2.logica;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

public class Localidad implements Serializable {
    private String nombre;
    private String provincia;
    private double latitud;
    private double longitud;
    private Color color;

    public Localidad(String nombre, String provincia, double latitud, double longitud, Color color) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.color = color;
    }

    public double distanciaEnKm(Localidad otra) {
        final int RADIO_TIERRA = 6371; 
        double latDistance = Math.toRadians(otra.latitud - this.latitud);
        double lonDistance = Math.toRadians(otra.longitud - this.longitud);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otra.latitud))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA * c;
    }

    public String getNombre() { return nombre; }
    public String getProvincia() { return provincia; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public Color getColor() { return color; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setColor(Color color) { this.color = color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localidad otra = (Localidad) o;
        return Objects.equals(nombre, otra.nombre);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre); }
    @Override
    public String toString() { return nombre + " (" + provincia + ")"; }
}