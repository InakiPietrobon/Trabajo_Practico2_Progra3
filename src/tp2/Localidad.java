package tp2;

import java.util.Objects;

public class Localidad {
    private String nombre;
    private String provincia;
    private double latitud;
    private double longitud;

    public Localidad(String nombre, String provincia, double latitud, double longitud) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Calcula la distancia real en kilómetros usando la fórmula de Haversine
    public double distanciaEnKm(Localidad otra) {
        final int RADIO_TIERRA = 6371; // Radio de la Tierra en km

        double latDistance = Math.toRadians(otra.latitud - this.latitud);
        double lonDistance = Math.toRadians(otra.longitud - this.longitud);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otra.latitud))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA * c;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getProvincia() { return provincia; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }

    // Equals y HashCode basados en el nombre para evitar localidades duplicadas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localidad otra = (Localidad) o;
        return Objects.equals(nombre, otra.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre + " (" + provincia + ")";
    }
}