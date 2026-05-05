package trabajo_Practico2_Progra3;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

public class ConectandoLocalidades {

	private JFrame frame;
	private JInternalFrame frameMapa;
	private JMapViewer mapa;
	
	// Instancia de la capa lógica (Grafo/Cerebro)
	private SistemaLocalidades sistema;
	
	// Componentes de la interfaz
	private JTextField txtCostoKm;
	private JTextField txtPorcentajeExceso;
	private JTextField txtCostoProvincia;
	private JTextArea txtResultados;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConectandoLocalidades window = new ConectandoLocalidades();
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
	public ConectandoLocalidades() {
		// Inicializamos el backend lógico
		sistema = new SistemaLocalidades();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Configuración principal de la ventana
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Conectando Localidades - Programación III");
		frame.getContentPane().setLayout(null);
		
		// Panel de Instrucciones básicas para el usuario
		JPanel panelAyuda = new JPanel();
		panelAyuda.setBorder(new TitledBorder(new EtchedBorder(), "Instrucciones"));
		panelAyuda.setBounds(10, 11, 290, 75);
		frame.getContentPane().add(panelAyuda);
		panelAyuda.setLayout(null);
		
		JLabel lblInstruccion1 = new JLabel("• Hace click izquierdo en el mapa para");
		lblInstruccion1.setBounds(10, 23, 270, 14);
		panelAyuda.add(lblInstruccion1);
		
		JLabel lblInstruccion2 = new JLabel("  registrar una nueva localidad.");
		lblInstruccion2.setBounds(10, 43, 270, 14);
		panelAyuda.add(lblInstruccion2);
		
		// Panel de Entrada para Parámetros de Costos
		JPanel panelCostos = new JPanel();
		panelCostos.setBorder(new TitledBorder(new EtchedBorder(), "Parámetros de Costo"));
		panelCostos.setBounds(10, 97, 290, 160);
		frame.getContentPane().add(panelCostos);
		panelCostos.setLayout(null);
		
		JLabel lblCostoKm = new JLabel("Costo/Km ($):");
		lblCostoKm.setBounds(10, 25, 110, 14);
		panelCostos.add(lblCostoKm);
		
		txtCostoKm = new JTextField("1500"); // Valor base sugerido
		txtCostoKm.setBounds(140, 22, 140, 20);
		panelCostos.add(txtCostoKm);
		
		JLabel lblPorcentaje = new JLabel("% Aumento (>300km):");
		lblPorcentaje.setBounds(10, 55, 130, 14);
		panelCostos.add(lblPorcentaje);
		
		txtPorcentajeExceso = new JTextField("20"); // 20% de recargo sugerido
		txtPorcentajeExceso.setBounds(140, 52, 140, 20);
		panelCostos.add(txtPorcentajeExceso);
		
		JLabel lblCostoProv = new JLabel("Costo Interprovincial ($):");
		lblCostoProv.setBounds(10, 85, 130, 14);
		panelCostos.add(lblCostoProv);
		
		txtCostoProvincia = new JTextField("40000"); // Costo fijo interprovincial sugerido
		txtCostoProvincia.setBounds(140, 82, 140, 20);
		panelCostos.add(txtCostoProvincia);
		
		JButton btnPlanificar = new JButton("Calcular Red Óptima");
		btnPlanificar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnPlanificar.setBounds(10, 119, 270, 30);
		panelCostos.add(btnPlanificar);
		
		// Panel donde se muestra el informe textual del AGM
		JPanel panelResultados = new JPanel();
		panelResultados.setBorder(new TitledBorder(new EtchedBorder(), "Informe de la Solución"));
		panelResultados.setBounds(10, 268, 290, 332);
		frame.getContentPane().add(panelResultados);
		panelResultados.setLayout(null);
		
		txtResultados = new JTextArea();
		txtResultados.setEditable(false);
		txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane(txtResultados);
		scrollPane.setBounds(10, 21, 270, 300);
		panelResultados.add(scrollPane);
		
		// Inicialización y centrado de JMapViewer (Lado Derecho)
		mapa = new JMapViewer();
		mapa.setDisplayPosition(new Coordinate(-36.0, -62.0), 5); // Enfocado en Argentina
		
		frameMapa = new JInternalFrame("Visor Geográfico de la Red");
		frameMapa.setResizable(true);
		frameMapa.setBounds(315, 11, 509, 589);
		frame.getContentPane().add(frameMapa);
		frameMapa.getContentPane().setLayout(null);
		frameMapa.setVisible(true);
		
		mapa.setSize(frameMapa.getSize());
		frameMapa.getContentPane().add(mapa);
		
		// =========================================================================
		// EVENTO 1: Clic en el Mapa para capturar coordenadas geográficas
		// =========================================================================
		mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Reaccionamos únicamente al clic izquierdo
				if (e.getButton() == MouseEvent.BUTTON1) {
					
					// Traducimos el clic de la pantalla a coordenadas reales de latitud y longitud
					Coordinate coordClickeada = (Coordinate) mapa.getPosition(e.getPoint());
					
					// Pedimos el nombre
					String nombre = JOptionPane.showInputDialog(frame, 
							"Ingresá el NOMBRE de la nueva localidad:", 
							"Registro de Localidad", 
							JOptionPane.QUESTION_MESSAGE);
					
					if (nombre == null || nombre.trim().isEmpty()) return;
					nombre = nombre.trim();
					
					// Pedimos la provincia
					String provincia = JOptionPane.showInputDialog(frame, 
							"¿A qué PROVINCIA pertenece " + nombre + "?", 
							"Registro de Provincia", 
							JOptionPane.QUESTION_MESSAGE);
					
					if (provincia == null || provincia.trim().isEmpty()) return;
					provincia = provincia.trim();
					
					// Instanciamos el objeto de la capa de negocio
					Localidad nueva = new Localidad(nombre, provincia, coordClickeada.getLat(), coordClickeada.getLon());
					
					// Validamos duplicados usando las reglas del backend
					if (sistema.getLocalidades().contains(nueva)) {
						JOptionPane.showMessageDialog(frame, "Ya existe una localidad registrada con el nombre: " + nombre, "Localidad Duplicada", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Guardamos en el modelo lógico
					sistema.agregarLocalidad(nueva);
					
					// Renderizamos el marcador de forma inmediata en el mapa
					MapMarkerDot marcador = new MapMarkerDot(nombre, coordClickeada);
					mapa.addMapMarker(marcador);
					
					// Agregamos un log al panel de resultados para dar feedback al usuario
					txtResultados.append("Registrada: " + nombre + " (" + provincia + ")\n");
				}
			}
		});

		// =========================================================================
		// EVENTO 2: Click en el botón para calcular Kruskal y graficar el AGM
		// =========================================================================
		btnPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Reseteamos las líneas del mapa y el texto anterior
					mapa.removeAllMapPolygons();
					txtResultados.setText("");
					
					// Control inicial de cantidad de vértices
					if (sistema.getLocalidades().size() < 2) {
						txtResultados.setText("Error:\nSe requieren al menos 2\nlocalidades en el mapa para\ncalcular un tendido eléctrico\no de fibra óptica.");
						return;
					}
					
					// Lectura dinámica de las variables de costos desde la pantalla
					double costoPorKm = Double.parseDouble(txtCostoKm.getText().trim());
					double porcentajeExceso = Double.parseDouble(txtPorcentajeExceso.getText().trim());
					double costoInterprovincial = Double.parseDouble(txtCostoProvincia.getText().trim());
					
					// Invocamos el cálculo del Árbol Generador Mínimo (Kruskal)
					List<Conexion> conexionesOptimas = sistema.planificarConexiones(costoPorKm, porcentajeExceso, costoInterprovincial);
					
					// Construimos el reporte de texto
					StringBuilder reporte = new StringBuilder();
					reporte.append("=============================\n");
					reporte.append("   CONEXIONES OPTIMIZADAS\n");
					reporte.append("=============================\n\n");
					
					double costoTotalAcumulado = 0;
					
					// Dibujamos las aristas óptimas y armamos el string
					for (Conexion con : conexionesOptimas) {
						Coordinate p1 = new Coordinate(con.getOrigen().getLatitud(), con.getOrigen().getLongitud());
						Coordinate p2 = new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud());
						
						// Truco de JMapViewer para forzar el dibujo de una arista abierta de 2 extremos
						List<Coordinate> puntosLinea = new ArrayList<>();
						puntosLinea.add(p1);
						puntosLinea.add(p2);
						puntosLinea.add(p2); 
						
						MapPolygonImpl linea = new MapPolygonImpl(puntosLinea);
						linea.setColor(Color.RED); // Líneas de conexión rojas para que resalten
						linea.setStroke(new java.awt.BasicStroke(3.0f)); // Grosor visible
						mapa.addMapPolygon(linea);
						
						// Anexamos los datos al reporte en el JTextArea
						reporte.append("• ").append(con.getOrigen().getNombre())
						       .append(" ↔ ").append(con.getDestino().getNombre()).append("\n");
						reporte.append("  Costo: $").append(String.format("%.2f", con.getCostoTotal())).append("\n\n");
						
						costoTotalAcumulado += con.getCostoTotal();
					}
					
					reporte.append("-----------------------------\n");
					reporte.append("COSTO TOTAL RED: $").append(String.format("%.2f", costoTotalAcumulado)).append("\n");
					reporte.append("=============================");
					
					// Mostramos el desglose final en la pantalla
					txtResultados.setText(reporte.toString());
					
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Por favor, ingresá valores numéricos válidos en los casilleros de costo.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}