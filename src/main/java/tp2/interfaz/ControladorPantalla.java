package main.java.tp2.interfaz;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import main.java.tp2.logica.*;

public class ControladorPantalla {

    private PantallaVisual vista;
    private RedLocalidades red;
    private Kruskal kruskal;

    private Localidad localidadParaConectar = null;
    private Map<Conexion, MapPolygonImpl> mapaLineasKruskal = new LinkedHashMap<Conexion, MapPolygonImpl>();
    private Map<Conexion, MapPolygonImpl> mapaLineasManuales = new LinkedHashMap<Conexion, MapPolygonImpl>();

    private final String[] PROVINCIAS_ARG = {
        "Buenos Aires", "CABA", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes", "Entre Ríos", 
        "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta", 
        "San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tierra del Fuego", "Tucumán", "Otra"
    };

    private final String[] COLORES_NOMBRES = {"Amarillo", "Rojo", "Azul", "Verde", "Naranja", "Negro", "Rosa", "Blanco"};
    private final Color[] COLORES_VALORES = {Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.BLACK, Color.PINK, Color.WHITE};

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PantallaVisual ventana = new PantallaVisual();
                    ControladorPantalla controlador = new ControladorPantalla(ventana);
                    controlador.iniciar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ControladorPantalla(PantallaVisual vista) {
        this.vista = vista;
        this.red = new RedLocalidades();
        this.kruskal = new Kruskal();
        configurarEventos();
    }

    public void iniciar() {
        vista.frame.setVisible(true);
    }

    private void configurarEventos() {
        vista.mapa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClicMapa(e);
            }
        });

        vista.btnGenerarOptima.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarKruskal();
            }
        });

        vista.btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularRedActual();
            }
        });

        vista.btnElimManuales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarLineasManuales();
            }
        });

        vista.btnElimOptimas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarLineasKruskal();
            }
        });

        vista.btnElimUna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarUnaConexion();
            }
        });

        vista.btnLimpiarChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vista.txtResultados.setText("");
            }
        });

        vista.btnElimLoc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(vista.frame, "¿Borrar TODAS las localidades y conexiones?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    red.getLocalidades().clear();
                    limpiarMapasTotales();
                    actualizarMapaVisual();
                    vista.txtResultados.setText("");
                }
            }
        });

        vista.btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Conexion> manuales = new ArrayList<Conexion>(mapaLineasManuales.keySet());
                    List<Conexion> optimas = new ArrayList<Conexion>(mapaLineasKruskal.keySet());
                    ManejoArchivos.guardarRed(red.getLocalidades(), manuales, optimas, "datos_red.txt");
                    JOptionPane.showMessageDialog(vista.frame, "Datos guardados exitosamente.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(vista.frame, "Error al guardar el archivo.");
                }
            }
        });

        vista.btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ManejoArchivos.DatosRed datos = ManejoArchivos.cargarRed("datos_red.txt");
                    red.setLocalidades(datos.localidades);
                    limpiarMapasTotales();
                    actualizarMapaVisual();
                    for (Conexion c : datos.manuales) dibujarLineaManual(c);
                    for (Conexion c : datos.optimas) dibujarLineaKruskal(c);
                    JOptionPane.showMessageDialog(vista.frame, "Red cargada correctamente.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(vista.frame, "Error al cargar el archivo.");
                }
            }
        });
    }

    private void manejarClicMapa(MouseEvent e) {
        Coordinate coord = (Coordinate) vista.mapa.getPosition(e.getPoint());
        Localidad locCercana = buscarLocalidadCercana(e.getPoint());

        if (e.getButton() == MouseEvent.BUTTON3) {
            if (locCercana != null) {
                String[] opciones = {"Conectar a", "Editar", "Eliminar", "Cancelar"};
                int sel = JOptionPane.showOptionDialog(vista.frame, "Opciones para: " + locCercana.getNombre(), "Acción", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (sel == 0) {
                    localidadParaConectar = locCercana;
                    JOptionPane.showMessageDialog(vista.frame, "Seleccionaste " + locCercana.getNombre() + ".\nHaga clic izquierdo en otra localidad para conectar.");
                } else if (sel == 1) {
                    mostrarFormularioEdicion(locCercana);
                } else if (sel == 2) {
                    red.getLocalidades().remove(locCercana);
                    actualizarMapaVisual();
                    limpiarMapasTotales();
                }
            } else {
                localidadParaConectar = null;
            }
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (localidadParaConectar != null) {
                if (locCercana != null && !locCercana.equals(localidadParaConectar)) {
                    Conexion conManual = new Conexion(localidadParaConectar, locCercana);
                    dibujarLineaManual(conManual);
                } else {
                    JOptionPane.showMessageDialog(vista.frame, "Destino inválido.");
                }
                localidadParaConectar = null;
            } else if (locCercana == null) {
                mostrarFormularioCreacion(coord);
            }
        }
    }

    private Localidad buscarLocalidadCercana(Point clickPixel) {
        int radioToleranciaPixeles = 15;
        for (Localidad loc : red.getLocalidades()) {
            Point locPixel = vista.mapa.getMapPosition(loc.getLatitud(), loc.getLongitud());
            if (locPixel != null) {
                if (locPixel.distance(clickPixel) <= radioToleranciaPixeles) {
                    return loc;
                }
            }
        }
        return null;
    }

    private void mostrarFormularioCreacion(Coordinate coord) {
        JTextField campoNombre = new JTextField("Nueva Localidad");
        JTextField campoLat = new JTextField(String.valueOf(coord.getLat()));
        JTextField campoLon = new JTextField(String.valueOf(coord.getLon()));
        final JComboBox<String> comboProv = new JComboBox<String>(PROVINCIAS_ARG);
        final JTextField campoProvManual = new JTextField();
        campoProvManual.setEnabled(false);
        final JComboBox<String> comboColores = new JComboBox<String>(COLORES_NOMBRES);

        comboProv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                campoProvManual.setEnabled(comboProv.getSelectedItem().equals("Otra"));
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        panel.add(new JLabel("Nombre:")); panel.add(campoNombre);
        panel.add(new JLabel("Provincia:")); panel.add(comboProv); panel.add(campoProvManual);
        panel.add(new JLabel("Latitud:")); panel.add(campoLat);
        panel.add(new JLabel("Longitud:")); panel.add(campoLon);
        panel.add(new JLabel("Color:")); panel.add(comboColores);

        int resultado = JOptionPane.showConfirmDialog(vista.frame, panel, "Crear Localidad", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) return;
            String prov = comboProv.getSelectedItem().equals("Otra") ? campoProvManual.getText().trim() : (String) comboProv.getSelectedItem();
            try {
                Localidad nuevaLoc = new Localidad(nombre, prov, Double.parseDouble(campoLat.getText()), Double.parseDouble(campoLon.getText()), COLORES_VALORES[comboColores.getSelectedIndex()]);
                if (!red.agregarLocalidad(nuevaLoc)) {
                    JOptionPane.showMessageDialog(vista.frame, "Ese nombre ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    actualizarMapaVisual();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista.frame, "Error en los datos.");
            }
        }
    }

    private void mostrarFormularioEdicion(final Localidad loc) {
        final JTextField campoNombre = new JTextField(loc.getNombre());
        final JTextField campoLat = new JTextField(String.valueOf(loc.getLatitud()));
        final JTextField campoLon = new JTextField(String.valueOf(loc.getLongitud()));
        final JComboBox<String> comboProv = new JComboBox<String>(PROVINCIAS_ARG);
        final JTextField campoProvManual = new JTextField();
        final JComboBox<String> comboColores = new JComboBox<String>(COLORES_NOMBRES);
        

        for (int i = 0; i < COLORES_VALORES.length; i++) {
            if (COLORES_VALORES[i].equals(loc.getColor())) { comboColores.setSelectedIndex(i); break; }
        }

        boolean pEncontrada = false;
        for (String p : PROVINCIAS_ARG) {
            if (p.equals(loc.getProvincia())) {
                comboProv.setSelectedItem(p);
                pEncontrada = true;
                break;
            }
        }
        if (!pEncontrada) {
            comboProv.setSelectedItem("Otra");
            campoProvManual.setEnabled(true);
            campoProvManual.setText(loc.getProvincia());
        } else {
            campoProvManual.setEnabled(false);
        }
        
        comboProv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                campoProvManual.setEnabled(comboProv.getSelectedItem().equals("Otra"));
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));

        panel.add(new JLabel("Nombre:")); panel.add(campoNombre);
        panel.add(new JLabel("Provincia:")); panel.add(comboProv); panel.add(campoProvManual);
        panel.add(new JLabel("Latitud:")); panel.add(campoLat);
        panel.add(new JLabel("Longitud:")); panel.add(campoLon);
        panel.add(new JLabel("Color:")); panel.add(comboColores);

        int resultado = JOptionPane.showConfirmDialog(vista.frame, panel, "Editar Localidad", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String n = campoNombre.getText().trim();
            if (!n.isEmpty()) {
                loc.setNombre(n);
                loc.setLatitud(Double.parseDouble(campoLat.getText()));
                loc.setLongitud(Double.parseDouble(campoLon.getText()));
                loc.setProvincia(comboProv.getSelectedItem().equals("Otra") ? campoProvManual.getText().trim() : (String) comboProv.getSelectedItem());
                loc.setColor(COLORES_VALORES[comboColores.getSelectedIndex()]);
                limpiarMapasTotales();
                actualizarMapaVisual();
            }
        }
    }

    private void generarKruskal() {
        try {
            double cKm = Double.parseDouble(vista.txtCostoKm.getText());
            double cExc = Double.parseDouble(vista.txtPorcentajeExceso.getText());
            double cProv = Double.parseDouble(vista.txtCostoProvincia.getText());
            limpiarLineasKruskal();
            List<Conexion> agm = kruskal.calcularAGM(red.getLocalidades(), cKm, cExc, cProv);
            for (Conexion con : agm) dibujarLineaKruskal(con);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista.frame, "Error en los parámetros de costo.");
        }
    }

    private void calcularRedActual() {
        if (mapaLineasKruskal.isEmpty() && mapaLineasManuales.isEmpty()) {
            JOptionPane.showMessageDialog(vista.frame, "No hay nada para calcular.");
            return;
        }
        try {
            double cKm = Double.parseDouble(vista.txtCostoKm.getText());
            double cExc = Double.parseDouble(vista.txtPorcentajeExceso.getText());
            double cProv = Double.parseDouble(vista.txtCostoProvincia.getText());
            double total = 0;

            vista.txtResultados.append("--- REPORTE ---\n");
            for (Conexion con : mapaLineasKruskal.keySet()) {
                double costo = con.calcularCosto(cKm, cExc, cProv);
                vista.txtResultados.append("Opt: " + con.getOrigen().getNombre() + " <-> " + con.getDestino().getNombre() + "\n  Dist: " + String.format("%.2f", con.getDistancia()) + " km | Costo: $" + String.format("%.2f", costo) + "\n");
                total += costo;
            }
            for (Conexion con : mapaLineasManuales.keySet()) {
                double costo = con.calcularCosto(cKm, cExc, cProv);
                vista.txtResultados.append("Man: " + con.getOrigen().getNombre() + " <-> " + con.getDestino().getNombre() + "\n  Dist: " + String.format("%.2f", con.getDistancia()) + " km | Costo: $" + String.format("%.2f", costo) + "\n");
                total += costo;
            }           
            vista.txtResultados.append("TOTAL: $" + String.format("%.2f", total) + "\n\n");
        } catch (Exception ex) {}
    }

    private void eliminarUnaConexion() {
        List<Conexion> todas = new ArrayList<Conexion>();
        todas.addAll(mapaLineasKruskal.keySet());
        todas.addAll(mapaLineasManuales.keySet());
        if (todas.isEmpty()) return;

        Object sel = JOptionPane.showInputDialog(vista.frame, "Seleccione conexión a eliminar:", "Eliminar", JOptionPane.QUESTION_MESSAGE, null, todas.toArray(), todas.get(0));
        if (sel != null) {
            Conexion c = (Conexion) sel;
            if (mapaLineasKruskal.containsKey(c)) vista.mapa.removeMapPolygon(mapaLineasKruskal.remove(c));
            else vista.mapa.removeMapPolygon(mapaLineasManuales.remove(c));
            vista.mapa.repaint();
        }
    }

    private void limpiarLineasKruskal() {
        for (MapPolygonImpl p : mapaLineasKruskal.values()) vista.mapa.removeMapPolygon(p);
        mapaLineasKruskal.clear();
    }

    private void limpiarLineasManuales() {
        for (MapPolygonImpl p : mapaLineasManuales.values()) vista.mapa.removeMapPolygon(p);
        mapaLineasManuales.clear();
    }

    private void limpiarMapasTotales() {
        limpiarLineasKruskal();
        limpiarLineasManuales();
    }

    private void actualizarMapaVisual() {
        vista.mapa.removeAllMapMarkers();
        for (Localidad loc : red.getLocalidades()) {
            MapMarkerDot m = new MapMarkerDot(loc.getNombre(), new Coordinate(loc.getLatitud(), loc.getLongitud()));
            m.setBackColor(loc.getColor());
            m.setFont(vista.fuenteMapa);
            vista.mapa.addMapMarker(m);
        }
    }

    private void dibujarLineaKruskal(Conexion con) {
        List<Coordinate> pts = new ArrayList<Coordinate>();
        pts.add(new Coordinate(con.getOrigen().getLatitud(), con.getOrigen().getLongitud()));
        pts.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        pts.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        MapPolygonImpl linea = new MapPolygonImpl(pts);
        linea.setColor(Color.RED);
        linea.setStroke(new BasicStroke(3.0f));
        mapaLineasKruskal.put(con, linea);
        vista.mapa.addMapPolygon(linea);
    }

    private void dibujarLineaManual(Conexion con) {
        List<Coordinate> pts = new ArrayList<Coordinate>();
        pts.add(new Coordinate(con.getOrigen().getLatitud(), con.getOrigen().getLongitud()));
        pts.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        pts.add(new Coordinate(con.getDestino().getLatitud(), con.getDestino().getLongitud()));
        MapPolygonImpl linea = new MapPolygonImpl(pts);
        linea.setColor(Color.BLUE);
        linea.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{10.0f}, 0.0f));
        mapaLineasManuales.put(con, linea);
        vista.mapa.addMapPolygon(linea);
    }
}