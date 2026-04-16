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


public class conectandoLocalidades {

	private JFrame frame;
	private JMapViewer mapa;

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
		
		frame.getContentPane().add(mapa);
		
		
		Coordinate coordenada = new Coordinate(-30, -58); 
		mapa.setDisplayPosition(coordenada, 2);		
		MapMarkerDot marcador = new MapMarkerDot("Punto 1", coordenada);
		
		marcador.getStyle().setBackColor(Color.red);
		
		mapa.addMapMarker(marcador);
		
		
			
		
	}

}
