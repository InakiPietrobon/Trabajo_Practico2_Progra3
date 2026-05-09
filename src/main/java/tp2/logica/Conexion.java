package main.java.tp2.logica;

public class Conexion implements Comparable<Conexion> {
    private Localidad origen;
    private Localidad destino;
    private double distancia;

    public Conexion(Localidad origen, Localidad destino) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = origen.distanciaEnKm(destino);
    }

    public double tasarCosto(double costoPorKm, double porcentajeExceso, double costoInterprovincial) {
        double costoBase = this.distancia * costoPorKm;
        if (this.distancia > 300) {
            costoBase += costoBase * (porcentajeExceso / 100.0);
        }
        if (!origen.getProvincia().equalsIgnoreCase(destino.getProvincia())) {
            costoBase += costoInterprovincial;
        }
        return costoBase;
    }

    public Localidad getOrigen() { return origen; }
    public Localidad getDestino() { return destino; }
    public double getDistancia() { return distancia; }

    @Override
    public int compareTo(Conexion otra) { 
        return Double.compare(this.distancia, otra.distancia); 
    }

    @Override
    public String toString() {
        return origen.getNombre() + " <-> " + destino.getNombre() + " (" + String.format("%.1f", distancia) + " km)";
    }
}