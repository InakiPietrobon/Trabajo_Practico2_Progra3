package main.java.tp2.interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class PantallaVisual {

    public JFrame frame;
    public JMapViewer mapa;
    
    public JTextField txtCostoKm, txtPorcentajeExceso, txtCostoProvincia;
    public JTextArea txtResultados;
    public Font fuenteMapa = new Font("Segoe UI", Font.BOLD, 13);
    
    public JButton btnGenerarOptima, btnCalcular;
    public JButton btnElimManuales, btnElimOptimas, btnElimUna, btnElimLoc;
    public JButton btnGuardar, btnCargar, btnLimpiarChat;

    public PantallaVisual() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Conectando Localidades - Programación III");
        frame.setBounds(50, 50, 1050, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); 
        frame.getContentPane().setLayout(null);
        
        JPanel panelControles = new JPanel(null);
        panelControles.setBounds(10, 10, 360, 660);
        frame.getContentPane().add(panelControles);
        
        JPanel panelCostos = new JPanel(null);
        panelCostos.setBorder(new TitledBorder(new EtchedBorder(), "Parámetros de Costo"));
        panelCostos.setBounds(0, 0, 360, 115);
        
        JLabel lblKm = new JLabel("Costo/Km ($):"); lblKm.setBounds(15, 25, 140, 20); panelCostos.add(lblKm);
        txtCostoKm = new JTextField("1500"); txtCostoKm.setBounds(160, 25, 185, 20); panelCostos.add(txtCostoKm);
        
        JLabel lblExc = new JLabel("% Aumento (>300km):"); lblExc.setBounds(15, 50, 140, 20); panelCostos.add(lblExc);
        txtPorcentajeExceso = new JTextField("20"); txtPorcentajeExceso.setBounds(160, 50, 185, 20); panelCostos.add(txtPorcentajeExceso);
        
        JLabel lblProv = new JLabel("Costo Interprovincial:"); lblProv.setBounds(15, 75, 140, 20); panelCostos.add(lblProv);
        txtCostoProvincia = new JTextField("40000"); txtCostoProvincia.setBounds(160, 75, 185, 20); panelCostos.add(txtCostoProvincia);
        panelControles.add(panelCostos);
        
        JPanel panelOperaciones = new JPanel(new GridLayout(1, 2, 5, 5));
        panelOperaciones.setBorder(new TitledBorder(new EtchedBorder(), "Operaciones"));
        panelOperaciones.setBounds(0, 120, 360, 50);
        btnGenerarOptima = new JButton("Generar Óptima");
        btnCalcular = new JButton("Calcular Costos");
        panelOperaciones.add(btnGenerarOptima);
        panelOperaciones.add(btnCalcular);
        panelControles.add(panelOperaciones);

        JPanel panelEliminacion = new JPanel(new GridLayout(2, 2, 5, 5));
        panelEliminacion.setBorder(new TitledBorder(new EtchedBorder(), "Eliminación"));
        panelEliminacion.setBounds(0, 175, 360, 75);
        btnElimManuales = new JButton("Eliminar Manuales");
        btnElimOptimas = new JButton("Eliminar Óptimas");
        btnElimUna = new JButton("Eliminar Conexión");
        btnElimLoc = new JButton("Eliminar Localidades");
        panelEliminacion.add(btnElimManuales);
        panelEliminacion.add(btnElimOptimas);
        panelEliminacion.add(btnElimUna);
        panelEliminacion.add(btnElimLoc);
        panelControles.add(panelEliminacion);

        JPanel panelArchivos = new JPanel(new GridLayout(1, 3, 5, 5));
        panelArchivos.setBorder(new TitledBorder(new EtchedBorder(), "Sistema"));
        panelArchivos.setBounds(0, 255, 360, 50);
        btnGuardar = new JButton("Guardar");
        btnCargar = new JButton("Cargar");
        btnLimpiarChat = new JButton("Limpiar Chat");
        panelArchivos.add(btnGuardar);
        panelArchivos.add(btnCargar);
        panelArchivos.add(btnLimpiarChat);
        panelControles.add(panelArchivos);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(new TitledBorder(new EtchedBorder(), "Consola de Informes"));
        panelResultados.setBounds(0, 310, 360, 350);
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelResultados.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
        panelControles.add(panelResultados);

        JInternalFrame frameMapa = new JInternalFrame("Visor Geográfico");
        frameMapa.setBounds(380, 10, 640, 660);
        frameMapa.setResizable(false);
        frameMapa.getContentPane().setLayout(new BorderLayout());
        mapa = new JMapViewer();
        mapa.setDisplayPosition(new Coordinate(-36.0, -62.0), 5);
        frameMapa.getContentPane().add(mapa, BorderLayout.CENTER);
        frameMapa.setVisible(true);
        frame.getContentPane().add(frameMapa);
    }
}