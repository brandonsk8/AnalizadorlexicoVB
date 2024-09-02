/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package b.analizadorlexicobv;

/**
 *
 * @author BrandonR
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Configuración básica de la ventana
        setTitle("Menú Principal - Analizador Léxico VB");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear botones para las opciones
        JButton botonAnalizadorLexico = new JButton("Analizador Léxico");
        JButton botonReportes = new JButton("Reportes");
        JButton botonSalir = new JButton("Salir");

        // botones con fuente arial
        botonAnalizadorLexico.setFont(new Font("Arial", Font.PLAIN, 18));
        botonReportes.setFont(new Font("Arial", Font.PLAIN, 18));
        botonSalir.setFont(new Font("Arial", Font.PLAIN, 18));
        botonAnalizadorLexico.setFocusPainted(false);
        botonReportes.setFocusPainted(false);
        botonSalir.setFocusPainted(false);

        //  centrado de botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(new Color(245, 245, 245)); // Color de fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Espacio entre botones

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(botonAnalizadorLexico, gbc);

        gbc.gridy = 1;
        panelBotones.add(botonReportes, gbc);

        gbc.gridy = 2;
        panelBotones.add(botonSalir, gbc);

        add(panelBotones, BorderLayout.CENTER);

        // Acción para el botón "Analizador Léxico"
        botonAnalizadorLexico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalizadorLexicoBV analizador = new AnalizadorLexicoBV();
                InterfazGrafica interfaz = new InterfazGrafica(analizador);
                interfaz.setVisible(true);
                dispose();  // Cierra el menú principal
            }
        });

        // Acción para el botón "Reportes"
        botonReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Bienvenido a reportes.");
            }
        });

        // Acción para el botón "Salir"
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
