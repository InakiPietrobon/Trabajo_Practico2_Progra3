package tp2;

public class Conexion implements Comparable<Conexion> {
    private Localidad origen;
    private Localidad destino;
    private double costoTotal;

    public Conexion(Localidad origen, Localidad destino, double costoPorKm, double porcentajeExceso, double costoInterprovincial) {
        this.origen = origen;
        this.destino = destino;
        this.costoTotal = calcularCosto(costoPorKm, porcentajeExceso, costoInterprovincial);
    }

    private double calcularCosto(double costoPorKm, double porcentajeExceso, double costoInterprovincial) {
        double distancia = origen.distanciaEnKm(destino);
        double costoBase = distancia * costoPorKm;

        // Regla 1: Penalización por más de 300 km
        if (distancia > 300) {
            costoBase += costoBase * (porcentajeExceso / 100.0);
        }

        // Regla 2: Penalización por provincias distintas
        if (!origen.getProvincia().equalsIgnoreCase(destino.getProvincia())) {
            costoBase += costoInterprovincial;
        }

        return costoBase;
    }

    public Localidad getOrigen() { return origen; }
    public Localidad getDestino() { return destino; }
    public double getCostoTotal() { return costoTotal; }

    // Necesario para ordenar las aristas de menor a mayor costo en Kruskal
    @Override
    public int compareTo(Conexion otra) {
        return Double.compare(this.costoTotal, otra.costoTotal);
    }

    @Override
    public String toString() {
        return origen.getNombre() + " <--> " + destino.getNombre() + " | Costo: $" + String.format("%.2f", costoTotal);
    }
}