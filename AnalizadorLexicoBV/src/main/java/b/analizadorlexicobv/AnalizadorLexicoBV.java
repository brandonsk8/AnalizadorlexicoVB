/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package b.analizadorlexicobv;

/**
 *
 * @author BrandonR
 */




import Token.Token;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class AnalizadorLexicoBV {
    private List<Token> tokens;
    private List<String> palabrasReservadas = Arrays.asList("Module", "Dim", "As", "Integer", "String", "Boolean", "End", "Sub", "Main", "While", "Do", "Loop", "For", "To", "Next", "If", "ElseIf", "Else", "Then", "Function", "Return", "Const");
    private List<String> operadoresAritmeticos = Arrays.asList("+", "-", "*", "/", "Mod", "^");
    private List<String> operadoresRelacionales = Arrays.asList("==", "<>", ">", "<", ">=", "<=");
    private List<String> operadoresLogicos = Arrays.asList("And", "Or", "Not");
    private List<String> operadoresAsignacion = Arrays.asList("=", "+=", "-=", "*=", "/=");

    public AnalizadorLexicoBV() {
        tokens = new ArrayList<>();
    }

    public void analizar(String codigoFuente) {
        String[] palabras = codigoFuente.split("\\s+");

        int linea = 1;
        int columna = 1;

        for (String palabra : palabras) {
            
            if (palabrasReservadas.contains(palabra)) {
                tokens.add(new Token("Palabra Reservada", palabra, linea, columna));
            } else if (operadoresAritmeticos.contains(palabra)) {
                tokens.add(new Token("Operador Aritmético", palabra, linea, columna));
            } else if (operadoresRelacionales.contains(palabra)) {
                tokens.add(new Token("Operador Relacional", palabra, linea, columna));
            } else if (operadoresLogicos.contains(palabra)) {
                tokens.add(new Token("Operador Lógico", palabra, linea, columna));
            } else if (operadoresAsignacion.contains(palabra)) {
                tokens.add(new Token("Operador de Asignación", palabra, linea, columna));
            } else if (palabra.matches("[a-zA-Z][a-zA-Z0-9_]*")) { // Regla para identificadores
                tokens.add(new Token("Identificador", palabra, linea, columna));
            } else if (palabra.matches("'[^']*")) { // Regla para comentarios de una línea
                tokens.add(new Token("Comentario", palabra, linea, columna));
            } else if (palabra.matches("\\d+")) { // Regla para enteros
                tokens.add(new Token("Número Entero", palabra, linea, columna));
            } else if (palabra.matches("\\d+\\.\\d+")) { // Regla para decimales
                tokens.add(new Token("Número Decimal", palabra, linea, columna));
            } else if (palabra.matches("\"[^\"]*\"")) { // Regla para cadenas de texto
                tokens.add(new Token("Cadena de Texto", palabra, linea, columna));
            } else {
                tokens.add(new Token("Error Léxico", palabra, linea, columna)); // Manejo de errores
            }
            columna += palabra.length() + 1; // +1 por el espacio en blanco
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public static void main(String[] args) {
        // Iniciar con el menú principal
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}
