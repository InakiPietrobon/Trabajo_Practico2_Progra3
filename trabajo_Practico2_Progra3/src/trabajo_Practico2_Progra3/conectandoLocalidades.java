package trabajo_Practico2_Progra3;

import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;


public class conectandoLocalidades {

	private JFrame frame;
	private JMapViewer mapa;
	private logicaLocalidades logica;
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					conectandoLocalidades window = new conectandoLocalidades();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public conectandoLocalidades() {
		logica = new logicaLocalidades();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Conectando Localidades");
		
		mapa = new JMapViewer();
		
		
		
		
		Point punto = new Point(1000, 1000);
		mapa.setCenter(punto);
		logica.agregarPoligono();
		mapa.addMapPolygon(logica.crearPoligono(logica.listaCoordeandas));
		
		frame.getContentPane().add(mapa);
		
		
		
		
			
		
	}

}
