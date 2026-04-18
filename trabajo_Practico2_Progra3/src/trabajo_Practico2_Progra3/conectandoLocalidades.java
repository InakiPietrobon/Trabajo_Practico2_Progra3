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
import javax.swing.JPanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class conectandoLocalidades {

	private JFrame frame;
	private JMapViewer mapa, mapa2;
	private logicaLocalidades logica;
	private JPanel panel;
	
	
	

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
		mapa = new JMapViewer();
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Conectando Localidades");
		frame.getContentPane().setLayout(null);
		
		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(31, 52, 233, 150);
		frame.getContentPane().add(panel_1);
		panel_1.add(mapa);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(99, 213, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		
		
		
		
			
		
	}
}
