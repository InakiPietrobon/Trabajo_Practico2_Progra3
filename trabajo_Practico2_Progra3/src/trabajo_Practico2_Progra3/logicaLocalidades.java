package trabajo_Practico2_Progra3;

import java.util.ArrayList;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;


public class logicaLocalidades {

	ArrayList <Coordinate> listaCoordeandas = new ArrayList<>();
	
	public MapPolygon crearPoligono(ArrayList<Coordinate> c) {
		MapPolygon poligono = new MapPolygonImpl(c);
		return poligono;
	}
	
	
	
	
	public Coordinate crearCoordenada(int x, int y) {
		Coordinate coordenada= new Coordinate(x, y);
		return coordenada;
	}
	
	
	public void agregarPoligono() {
		listaCoordeandas.add(crearCoordenada(-34, -63));
		listaCoordeandas.add(crearCoordenada(-35, -62));
		listaCoordeandas.add(crearCoordenada(-36, -61));
		listaCoordeandas.add(crearCoordenada(-37, -60));
		listaCoordeandas.add(crearCoordenada(-39, -59));
	}
	
	
	
}
