package main.java.tp2.interfaz;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

// Asegurate de que estos imports coincidan con tu estructura real de paquetes
import main.java.tp2.logica.*;

public class VentanaPrincipal {

    private JFrame frame;
    private JMapViewer mapa;
    
    private RedLocalidades red;
    private Kruskal kruskal;
    
    private JTextField txtCostoKm, txtPorcentajeExceso, txtCostoProvincia;
    private JTextArea txtResultados;
    private Font fuenteMapa = new Font("Segoe UI", Font.BOLD, 13);
    
    private Localidad localidadParaConectar = null;
    private Map<Conexion, MapPolygonImpl> mapaLineasKruskal = new LinkedHashMap<>();
    private Map<Conexion, MapPolygonImpl> mapaLineasManuales = new LinkedHashMap<>();

    private final String[] PROVINCIAS_ARG = {
        "Buenos Aires", "CABA", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes", "Entre Ríos", 
        "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta", 
        "San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tierra del Fuego", "Tucumán", "--- Otra/Manual ---"
    };

    private final String[] COLORES_NOMBRES = {"Amarillo", "Rojo", "Azul", "Verde", "Naranja", "Negro", "Rosa", "Blanco"};
    private final Color[] COLORES_VALORES = {Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.BLACK, Color.PINK, Color.WHITE};

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        EventQueue.invokeLater(() -> {
            try {
                VentanaPrincipal window = new VentanaPrincipal();
                window.frame.setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public VentanaPrincipal() {
        red = new RedLocalidades();
        kruskal = new Kruskal();
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
        JButton btnGenerarOptima = new JButton("Generar Óptima");
        JButton btnCalcular = new JButton("Calcular Costos");
        panelOperaciones.add(btnGenerarOptima);
        panelOperaciones.add(btnCalcular);
        panelControles.add(panelOperaciones);

        JPanel panelEliminacion = new JPanel(new GridLayout(2, 2, 5, 5));
        panelEliminacion.setBorder(new TitledBorder(new EtchedBorder(), "Eliminación"));
        panelEliminacion.setBounds(0, 175, 360, 75);
        JButton btnElimManuales = new JButton("Eliminar Manuales");
        JButton btnElimOptimas = new JButton("Eliminar Óptimas");
        JButton btnElimUna = new JButton("Eliminar Conexión");
        JButton btnElimLoc = new JButton("Eliminar Localidades");
        panelEliminacion.add(btnElimManuales);
        panelEliminacion.add(btnElimOptimas);
        panelEliminacion.add(btnElimUna);
        panelEliminacion.add(btnElimLoc);
        panelControles.add(panelEliminacion);

        JPanel panelArchivos = new JPanel(new GridLayout(1, 3, 5, 5));
        panelArchivos.setBorder(new TitledBorder(new EtchedBorder(), "Sistema"));
        panelArchivos.setBounds(0, 255, 360, 50);
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargar = new JButton("Cargar");
        JButton btnLimpiarChat = new JButton("Limpiar Chat");
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

        mapa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Coordinate coord = (Coordinate) mapa.getPosition(e.getPoint());
                
                Localidad locCercana = buscarLocalidadCercana(e.getPoint());

                if (e.getButton() == MouseEvent.BUTTON3) { 
                    if (locCercana != null) {
                        String[] opciones = {"Conectar a", "Editar", "Eliminar", "Cancelar"};
                        int sel = JOptionPane.showOptionDialog(frame, "Opciones para: " + locCercana.getNombre(), "Acción", 0, 1, null, opciones, opciones[0]);
                        if (sel == 0) { 
                            localidadParaConectar = locCercana;
                            JOptionPane.showMessageDialog(frame, "Seleccionaste " + locCercana.getNombre() + ".\nHacé clic izquierdo en el destino.");
                        } else if (sel == 1) { mostrarFormularioEdicion(locCercana); } 
                        else if (sel == 2) { 
                            red.getLocalidades().remove(locCercana);
                            actualizarMapaVisual(); limpiarMapasTotales(); 
                        }
                    } else { localidadParaConectar = null; }
                } 
                else if (e.getButton() == MouseEvent.BUTTON1) { 
                    if (localidadParaConectar != null) { 
                        if (locCercana != null && !locCercana.equals(localidadParaConectar)) {
                            Conexion conManual = new Conexion(localidadParaConectar, locCercana);
                            dibujarLineaManual(conManual);
                        } else { JOptionPane.showMessageDialog(frame, "Destino inválido."); }
                        localidadParaConectar = null; 
                    } else if (locCercana == null) { mostrarFormularioCreacion(coord); }
                }
            }
        });

        btnGenerarOptima.addActionListener(e -> generarKruskal());
        btnCalcular.addActionListener(e -> calcularRedActual());
        
        btnElimUna.addActionListener(e -> eliminarUnaAristaMenuConPreview());
        btnElimManuales.addActionListener(e -> limpiarLineasManuales());
        btnElimOptimas.addActionListener(e -> limpiarLineasKruskal());
        btnElimLoc.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Borrar TODAS las localidades y conexiones?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                red.getLocalidades().clear();
                limpiarMapasTotales();
                actualizarMapaVisual();
                txtResultados.setText("");
            }
        });
        
        btnLimpiarChat.addActionListener(e -> txtResultados.setText(""));
        btnGuardar.addActionListener(e -> {
            try { 
                List<Conexion> manuales = new ArrayList<>(mapaLineasManuales.keySet());
                List<Conexion> optimas = new ArrayList<>(mapaLineasKruskal.keySet());
                
                PersistData.guardarRed(red.getLocalidades(), manuales, optimas, "datos_red.txt"); 
                JOptionPane.showMessageDialog(frame, "Datos guardados (Nodos y Conexiones)."); 
            } 
            catch (IOException ex) { JOptionPane.showMessageDialog(frame, "Error al guardar."); }
        });
        
        btnCargar.addActionListener(e -> {
            try { 
            	PersistData.DatosGuardados datos = PersistData.cargarRed("datos_red.txt");
                red.setLocalidades(datos.localidades); 
                limpiarMapasTotales(); 
                actualizarMapaVisual(); 
                for(Conexion c : datos.manuales) dibujarLineaManual(c);
                for(Conexion c : datos.optimas) dibujarLineaKruskal(c);
            } 
            catch (IOException ex) { JOptionPane.showMessageDialog(frame, "Error al cargar."); }
        });
    }


    private Localidad buscarLocalidadCercana(Point clickPixel) {
        int radioToleranciaPixeles = 15; 
        
        for (Localidad loc : red.getLocalidades()) {
            Point locPixel = mapa.getMapPosition(loc.getLatitud(), loc.getLongitud());
            if (locPixel != null) {
                if (locPixel.distance(clickPixel) <= radioToleranciaPixeles) {
                    return loc;
                }
            }
        }
        return null;
    }

    private void mostrarFormularioCreacion(Coordinate coord) {
        MapMarkerDot marcadorTemporal = new MapMarkerDot("Nueva Localidad", coord);
        marcadorTemporal.setBackColor(COLORES_VALORES[0]); marcadorTemporal.setFont(fuenteMapa);
        mapa.addMapMarker(marcadorTemporal); mapa.repaint();

        JTextField campoNombre = new JTextField("Nueva Localidad");
        JTextField campoLat = new JTextField(String.valueOf(coord.getLat()));
        JTextField campoLon = new JTextField(String.valueOf(coord.getLon()));
        JComboBox<String> comboProv = new JComboBox<>(PROVINCIAS_ARG);
        JTextField campoProvManual = new JTextField(); campoProvManual.setEnabled(false);
        JComboBox<String> comboColores = new JComboBox<>(COLORES_NOMBRES);

        comboProv.addActionListener(e -> campoProvManual.setEnabled(comboProv.getSelectedItem().equals("--- Otra/Manual ---")));
        
        Runnable actualizarVista = () -> {
            try {
                String n = campoNombre.getText().trim();
                marcadorTemporal.setName(n.isEmpty() ? "..." : n);
                marcadorTemporal.setLat(Double.parseDouble(campoLat.getText()));
                marcadorTemporal.setLon(Double.parseDouble(campoLon.getText()));
                marcadorTemporal.setBackColor(COLORES_VALORES[comboColores.getSelectedIndex()]);
                mapa.repaint();
            } catch(Exception ignored){}
        };

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarVista.run(); }
            public void removeUpdate(DocumentEvent e) { actualizarVista.run(); }
            public void changedUpdate(DocumentEvent e) { actualizarVista.run(); }
        };
        campoNombre.getDocument().addDocumentListener(dl); campoLat.getDocument().addDocumentListener(dl); campoLon.getDocument().addDocumentListener(dl);
        comboColores.addActionListener(e -> actualizarVista.run());

        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        panel.add(new JLabel("Nombre:")); panel.add(campoNombre);
        panel.add(new JLabel("Provincia:")); panel.add(comboProv); panel.add(campoProvManual);
        panel.add(new JLabel("Latitud:")); panel.add(campoLat);
        panel.add(new JLabel("Longitud:")); panel.add(campoLon);
        panel.add(new JLabel("Color:")); panel.add(comboColores);

        int resultado = JOptionPane.showConfirmDialog(frame, panel, "Crear Localidad", JOptionPane.OK_CANCEL_OPTION);
        mapa.removeMapMarker(marcadorTemporal);

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) return;
            String prov = comboProv.getSelectedItem().equals("--- Otra/Manual ---") ? campoProvManual.getText().trim() : (String) comboProv.getSelectedItem();
            try {
                if (!red.agregarLocalidad(new Localidad(nombre, prov, Double.parseDouble(campoLat.getText()), Double.parseDouble(campoLon.getText()), COLORES_VALORES[comboColores.getSelectedIndex()]))) {
                    JOptionPane.showMessageDialog(frame, "Ya existe una localidad con el nombre: " + nombre, "Duplicado", JOptionPane.ERROR_MESSAGE);
                } else {
                    actualizarMapaVisual();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Error en coordenadas."); }
        } else { mapa.repaint(); }
    }

    private void mostrarFormularioEdicion(Localidad loc) {
        double latOrig = loc.getLatitud(); double lonOrig = loc.getLongitud(); String nombreOrig = loc.getNombre(); Color colorOrig = loc.getColor();

        JTextField campoNombre = new JTextField(loc.getNombre());
        JTextField campoLat = new JTextField(String.valueOf(loc.getLatitud()));
        JTextField campoLon = new JTextField(String.valueOf(loc.getLongitud()));
        JComboBox<String> comboProv = new JComboBox<>(PROVINCIAS_ARG);
        JTextField campoProvManual = new JTextField();
        JComboBox<String> comboColores = new JComboBox<>(COLORES_NOMBRES);

        for (int i=0; i<COLORES_VALORES.length; i++) { if (COLORES_VALORES[i].equals(loc.getColor())) { comboColores.setSelectedIndex(i); break; } }
        
        boolean pEncontrada = false;
        for (String p : PROVINCIAS_ARG) { if (p.equals(loc.getProvincia())) { comboProv.setSelectedItem(p); pEncontrada = true; break; } }
        if (!pEncontrada) { comboProv.setSelectedItem("--- Otra/Manual ---"); campoProvManual.setEnabled(true); campoProvManual.setText(loc.getProvincia()); } else { campoProvManual.setEnabled(false); }
        comboProv.addActionListener(e -> campoProvManual.setEnabled(comboProv.getSelectedItem().equals("--- Otra/Manual ---")));

        Runnable actualizarVista = () -> {
            try {
                loc.setNombre(campoNombre.getText().trim()); loc.setLatitud(Double.parseDouble(campoLat.getText())); 
                loc.setLongitud(Double.parseDouble(campoLon.getText())); loc.setColor(COLORES_VALORES[comboColores.getSelectedIndex()]);
                actualizarMapaVisual(); 
            } catch(Exception ignored){}
        };

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarVista.run(); }
            public void removeUpdate(DocumentEvent e) { actualizarVista.run(); }
            public void changedUpdate(DocumentEvent e) { actualizarVista.run(); }
        };
        campoNombre.getDocument().addDocumentListener(dl); campoLat.getDocument().addDocumentListener(dl); campoLon.getDocument().addDocumentListener(dl);
        comboColores.addActionListener(e -> actualizarVista.run());

        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        panel.add(new JLabel("Nombre:")); panel.add(campoNombre);
        panel.add(new JLabel("Provincia:")); panel.add(comboProv); panel.add(campoProvManual);
        panel.add(new JLabel("Latitud:")); panel.add(campoLat);
        panel.add(new JLabel("Longitud:")); panel.add(campoLon);
        panel.add(new JLabel("Color:")); panel.add(comboColores);

        if (JOptionPane.showConfirmDialog(frame, panel, "Editar Localidad", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String n = campoNombre.getText().trim();
            if (n.isEmpty()) { loc.setNombre(nombreOrig); actualizarMapaVisual(); return; }
            loc.setProvincia(comboProv.getSelectedItem().equals("--- Otra/Manual ---") ? campoProvManual.getText().trim() : (String) comboProv.getSelectedItem());
            limpiarMapasTotales(); 
        } else {
            loc.setNombre(nombreOrig); loc.setLatitud(latOrig); loc.setLongitud(lonOrig); loc.setColor(colorOrig); actualizarMapaVisual();
        }
    }


    private void generarKruskal() {
        try {
            double cKm = Double.parseDouble(txtCostoKm.getText());
            double cExc = Double.parseDouble(txtPorcentajeExceso.getText());
            double cProv = Double.parseDouble(txtCostoProvincia.getText());
            
            limpiarLineasKruskal();
            
            List<Conexion> agm = kruskal.calcularAGM(red.getLocalidades(), cKm, cExc, cProv);
            for (Conexion con : agm) dibujarLineaKruskal(con);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Verifique los costos numéricos antes de generar la red.");
        }
    }

    private void calcularRedActual() {
        if (mapaLineasKruskal.isEmpty() && mapaLineasManuales.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay conexiones en el mapa para calcular.");
            return;
        }

        try {
            double cKm = Double.parseDouble(txtCostoKm.getText());
            double cExc = Double.parseDouble(txtPorcentajeExceso.getText());
            double cProv = Double.parseDouble(txtCostoProvincia.getText());
            double granTotal = 0;

            txtResultados.append("============ REPORTE DE COSTOS ============\n\n");

            if (!mapaLineasKruskal.isEmpty()) {
                txtResultados.append("-- CONEXIONES ÓPTIMAS --\n");
                double subtotal = 0;
                for (Conexion con : mapaLineasKruskal.keySet()) {
                    double costo = con.tasarCosto(cKm, cExc, cProv);
                    txtResultados.append("• " + con.getOrigen().getNombre() + " <-> " + con.getDestino().getNombre() + "\n  Dist: " + String.format("%.2f", con.getDistancia()) + " km | Costo: $" + String.format("%.2f", costo) + "\n");
                    subtotal += costo;
                }
                txtResultados.append(">> Subtotal Óptimo: $" + String.format("%.2f", subtotal) + "\n\n");
                granTotal += subtotal;
            }

            if (!mapaLineasManuales.isEmpty()) {
                txtResultados.append("-- CONEXIONES MANUALES --\n");
                double subtotal = 0;
                for (Conexion con : mapaLineasManuales.keySet()) {
                    double costo = con.tasarCosto(cKm, cExc, cProv);
                    txtResultados.append("• " + con.getOrigen().getNombre() + " <-> " + con.getDestino().getNombre() + "\n  Dist: " + String.format("%.2f", con.getDistancia()) + " km | Costo: $" + String.format("%.2f", costo) + "\n");
                    subtotal += costo;
                }
                txtResultados.append(">> Subtotal Manual: $" + String.format("%.2f", subtotal) + "\n\n");
                granTotal += subtotal;
            }

            txtResultados.append("===========================================\n");
            txtResultados.append("COSTO TOTAL RED ACTUAL: $" + String.format("%.2f", granTotal) + "\n\n");

            txtResultados.setCaretPosition(txtResultados.getDocument().getLength());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Verifique que los costos sean números válidos.");
        }
    }

    private class AristaWrapper {
        Conexion c; boolean esKruskal;
        AristaWrapper(Conexion c, boolean esKruskal) { this.c = c; this.esKruskal = esKruskal; }
        @Override public String toString() { return (esKruskal ? "[Óptima] " : "[Manual] ") + c.toString(); }
    }

    private void eliminarUnaAristaMenuConPreview() {
        List<AristaWrapper> todas = new ArrayList<>();
        for (Conexion c : mapaLineasKruskal.keySet()) todas.add(new AristaWrapper(c, true));
        for (Conexion c : mapaLineasManuales.keySet()) todas.add(new AristaWrapper(c, false));
        if(todas.isEmpty()){ JOptionPane.showMessageDialog(frame, "No hay conexiones."); return; }
        
        JComboBox<AristaWrapper> combo = new JComboBox<>(todas.toArray(new AristaWrapper[0]));
        
        combo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AristaWrapper sel = (AristaWrapper) e.getItem();
                mapa.removeAllMapPolygons(); 
                MapPolygonImpl poly = sel.esKruskal ? mapaLineasKruskal.get(sel.c) : mapaLineasManuales.get(sel.c);
                mapa.addMapPolygon(poly); 
                mapa.repaint();
            }
        });

        combo.setSelectedIndex(0);

        int res = JOptionPane.showConfirmDialog(frame, combo, "Eliminar Conexión", JOptionPane.OK_CANCEL_OPTION);
        
        if (res == JOptionPane.OK_OPTION) {
            AristaWrapper sel = (AristaWrapper) combo.getSelectedItem();
            if (sel.esKruskal) mapaLineasKruskal.remove(sel.c); else mapaLineasManuales.remove(sel.c);
        }
        
        mapa.removeAllMapPolygons();
        for (MapPolygonImpl p : mapaLineasKruskal.values()) mapa.addMapPolygon(p);
        for (MapPolygonImpl p : mapaLineasManuales.values()) mapa.addMapPolygon(p);
        mapa.repaint();
    }


    private void limpiarLineasKruskal() {
        for (MapPolygonImpl p : mapaLineasKruskal.values()) mapa.removeMapPolygon(p);
        mapaLineasKruskal.clear();
    }

    private void limpiarLineasManuales() {
        for (MapPolygonImpl p : mapaLineasManuales.values()) mapa.removeMapPolygon(p);
        mapaLineasManuales.clear();
    }

    private void limpiarMapasTotales() {
        limpiarLineasKruskal();
        limpiarLineasManuales();
    }

    private void actualizarMapaVisual() {
        mapa.removeAllMapMarkers();
        for (Localidad loc : red.getLocalidades()) {
            MapMarkerDot m = new MapMarkerDot(loc.getNombre(), new Coordinate(loc.getLatitud(), loc.getLongitud()));
            m.setBackColor(loc.getColor()); m.setFont(fuenteMapa); mapa.addMapMarker(m);
        }
    }

    private void dibujarLineaKruskal(Conexion con) {
        List<Coordinate> puntos = new ArrayList<>();
        puntos.add(new Coordinate(con.getOrigen().getLatitud(), con.getOrigen().getLongitud()));
        puntos.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        puntos.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        MapPolygonImpl linea = new MapPolygonImpl(puntos);
        linea.setColor(Color.RED); linea.setStroke(new BasicStroke(3.0f));
        mapaLineasKruskal.put(con, linea); mapa.addMapPolygon(linea);
    }

    private void dibujarLineaManual(Conexion con) {
        List<Coordinate> puntos = new ArrayList<>();
        puntos.add(new Coordinate(con.getOrigen().getLatitud(), con.getOrigen().getLongitud()));
        puntos.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        puntos.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        MapPolygonImpl linea = new MapPolygonImpl(puntos);
        linea.setColor(Color.BLUE); linea.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{10.0f}, 0.0f));
        mapaLineasManuales.put(con, linea); mapa.addMapPolygon(linea);
    }
}