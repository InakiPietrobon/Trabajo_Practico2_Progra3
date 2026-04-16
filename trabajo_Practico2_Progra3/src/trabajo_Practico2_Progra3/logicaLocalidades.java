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
		listaCoordeandas.add(crearCoordenada(10, 1));
		listaCoordeandas.add(crearCoordenada(1, 10));
		listaCoordeandas.add(crearCoordenada(2, 12));
		listaCoordeandas.add(crearCoordenada(13, 3));
		listaCoordeandas.add(crearCoordenada(14, 14));
	}
	
	
	
}
