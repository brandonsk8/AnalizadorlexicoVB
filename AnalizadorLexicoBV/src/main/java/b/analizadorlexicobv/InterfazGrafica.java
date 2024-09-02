/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package b.analizadorlexicobv;

/**
 *
 * @author BrandonR
 */
import GeneradorImagen.GeneradorImagen;
import Token.Token;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;



public class InterfazGrafica extends JFrame {

    private JTextArea textAreaCodigo;
    private JLabel etiquetaPosicion;
    private JPanel panelImagen;
    private AnalizadorLexicoBV analizador;
    private JComboBox<Integer> comboBoxTamañoCuadricula;
    private JTable tablaTokens;
    private DefaultTableModel modeloTabla;
    private BufferedImage imagenGenerada;

    public InterfazGrafica(AnalizadorLexicoBV analizador) {
        this.analizador = analizador;

        // Configuración básica de la ventana
        setTitle("Analizador Léxico VisualBasic");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicialización de componentes
        textAreaCodigo = new JTextArea();
        textAreaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollCodigo = new JScrollPane(textAreaCodigo);
        panelImagen = new JPanel();
        etiquetaPosicion = new JLabel("Fila: 1, Columna: 1");

        // Configuracinn del layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollCodigo, panelImagen);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
        add(etiquetaPosicion, BorderLayout.SOUTH);

        // Menu para cargar archivo
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem cargarArchivo = new JMenuItem("Cargar archivo TXT");
        menuArchivo.add(cargarArchivo);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // seleccionar el tamaño de la cuadrícula
        comboBoxTamañoCuadricula = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10});
        JPanel panelBotones = new JPanel();
        panelBotones.add(new JLabel("Tamaño de Cuadrícula:"));
        panelBotones.add(comboBoxTamañoCuadricula);
        
        // Creación de botones
        JButton botonAnalizar = new JButton("Analizar Código");
        JButton botonGenerarImagen = new JButton("Generar Imagen");
        JButton botonGuardarImagen = new JButton("Guardar Imagen");
        JButton botonReporte = new JButton("Generar Reporte"); 

        // Agregar botones al panel
        panelBotones.add(botonAnalizar);
        panelBotones.add(botonGenerarImagen);
        panelBotones.add(botonGuardarImagen);
        panelBotones.add(botonReporte); // Añadiendo el botón de reporte al panel

        // Agregar el panel de botones al layout principal
        add(panelBotones, BorderLayout.NORTH);

        // Tabla para mostrar los tokens generados
        modeloTabla = new DefaultTableModel(new Object[]{"Tipo", "Lexema"}, 0);
        tablaTokens = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaTokens);
        add(scrollTabla, BorderLayout.EAST);

        // Agregar listeners
        textAreaCodigo.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                int caretpos = textAreaCodigo.getCaretPosition();
                int fila = 1;
                int columna = 1;

                try {
                    fila = textAreaCodigo.getLineOfOffset(caretpos) + 1;
                    columna = caretpos - textAreaCodigo.getLineStartOffset(fila - 1) + 1;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                etiquetaPosicion.setText("Fila: " + fila + ", Columna: " + columna);
            }
        });

        // Acción para cargar archivo
        cargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    cargarArchivo(selectedFile);
                }
            }
        });

        // Acción del botón Analizar
        botonAnalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoFuente = textAreaCodigo.getText();
                analizador.analizar(codigoFuente);

                // Limpiar la tabla antes de agregar nuevos datos
                modeloTabla.setRowCount(0);

                // Agregar los tokens a la tabla
                for (Token token : analizador.getTokens()) {
                    modeloTabla.addRow(new Object[]{token.getTipo(), token.getLexema()});
                }

                JOptionPane.showMessageDialog(null, "Análisis léxico completado. Se encontraron " + analizador.getTokens().size() + " tokens.");
            }
        });

        // Acción del botón Generar Imagen
        botonGenerarImagen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarImagenEnPanel();
            }
        });

        // Acción del botón Guardar Imagen
        botonGuardarImagen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarImagen();
            }
        });

        // Acción del botón Generar Reporte
        botonReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
    }

    private void cargarArchivo(File file) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            textAreaCodigo.read(br, null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo: " + e.getMessage());
        }
    }

    private void generarImagenEnPanel() {
        panelImagen.removeAll();

        // Obtener el tamaño de la cuadrícula desde el JComboBox
        int tamaño = (int) comboBoxTamañoCuadricula.getSelectedItem();
        panelImagen.setLayout(new GridLayout(tamaño, tamaño));

        // Crear instancia de GeneradorImagen
        GeneradorImagen generador = new GeneradorImagen(tamaño, tamaño, 50, analizador.getTokens());
        generador.generarImagen("resultado.png");

        // Cargar y mostrar la imagen generada
        try {
            imagenGenerada = ImageIO.read(new File("resultado.png"));
            JLabel labelImagen = new JLabel(new ImageIcon(imagenGenerada));
            panelImagen.add(labelImagen);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen generada: " + ex.getMessage());
        }

        panelImagen.revalidate();
        panelImagen.repaint();
    }

    private void guardarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar imagen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PNG", "png"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(imagenGenerada, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Imagen guardada exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage());
            }
        }
    }

    private void generarReporte() {
        DefaultTableModel modeloReporte = new DefaultTableModel(new Object[]{"Token", "Lexema", "Línea", "Columna", "Cuadro"}, 0);

        int filas = (int) comboBoxTamañoCuadricula.getSelectedItem();
        int columnas = filas; // Asumiendo que es un cuadrado

        int index = 0;
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                if (index < analizador.getTokens().size()) {
                    Token token = analizador.getTokens().get(index);
                    Color color = new GeneradorImagen(filas, columnas, 50, analizador.getTokens()).obtenerColorParaToken(token);

                    // Formatear la información del cuadro
                    String infoCuadro = String.format("Fila: %d, Col: %d\nColor: #%02x%02x%02x", y + 1, x + 1, color.getRed(), color.getGreen(), color.getBlue());

                    modeloReporte.addRow(new Object[]{token.getTipo(), token.getLexema(), token.getLinea(), token.getColumna(), infoCuadro});
                    index++;
                }
            }
        }

        // Mostrar el reporte en un nuevo tabla
        JTable tablaReporte = new JTable(modeloReporte);
        JScrollPane scrollReporte = new JScrollPane(tablaReporte);
        JOptionPane.showMessageDialog(this, scrollReporte, "Reporte de Tokens", JOptionPane.INFORMATION_MESSAGE);
    }
}

