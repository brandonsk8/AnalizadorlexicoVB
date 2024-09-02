/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GeneradorImagen;

/**
 *
 * @author BrandonR
 */


import Token.Token;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class GeneradorImagen {

    private int filas;
    private int columnas;
    private int tamañoCuadro;
    private List<Token> tokens;

    public GeneradorImagen(int filas, int columnas, int tamañoCuadro, List<Token> tokens) {
        this.filas = filas;
        this.columnas = columnas;
        this.tamañoCuadro = tamañoCuadro;
        this.tokens = tokens;
    }

    public void generarImagen(String nombreArchivo) {
        BufferedImage imagen = new BufferedImage(columnas * tamañoCuadro, filas * tamañoCuadro, BufferedImage.TYPE_INT_RGB);
        Graphics g = imagen.getGraphics();

        int index = 0;
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                if (index < tokens.size()) {
                    Token token = tokens.get(index);
                    if (token.getTipo().equals("Square.Color")) {
                        int[] posicion = new int[2];
                        Color color = procesarSquareColor(token.getLexema(), posicion);
                        // Dibujar el cuadro en la posición especificada
                        if (posicion[0] >= 0 && posicion[0] < filas && posicion[1] >= 0 && posicion[1] < columnas) {
                            g.setColor(color);
                            g.fillRect(posicion[1] * tamañoCuadro, posicion[0] * tamañoCuadro, tamañoCuadro, tamañoCuadro);
                        }
                    } else {
                        // Otros tokens
                        Color color = obtenerColorParaToken(token);
                        g.setColor(color);
                        g.fillRect(x * tamañoCuadro, y * tamañoCuadro, tamañoCuadro, tamañoCuadro);
                    }
                    index++;
                } else {
                    // Si no hay suficientes tokens, llenamos las celdas restantes con un color de fondo (blanco o gris claro)
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x * tamañoCuadro, y * tamañoCuadro, tamañoCuadro, tamañoCuadro);
                }
            }
        }

        g.dispose();

        try {
            ImageIO.write(imagen, "png", new File(nombreArchivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color obtenerColorParaToken(Token token) {
        String lexema = token.getLexema();
        switch (token.getTipo()) {
            case "Palabra Reservada":
                return new Color(0x60A917); // Verde para Palabras Reservadas
            case "Identificador":
                return Color.GREEN;
            case "Operador Aritmético":
                switch (lexema) {
                    case "+":
                        return new Color(0xFF33FF); // Magenta  Suma
                    case "-":
                        return new Color(0xC19A6B); // Marron  Resta
                    case "^":
                        return new Color(0xFCD0B4); // Melocoton p Exponente
                    case "/":
                        return new Color(0xB4D941); // Verde c División
                    case "Mod":
                        return new Color(0xD9AB41); // Ocre  Módulo
                    case "*":
                        return new Color(0xD80073); // Fucsia  Multiplicaciopn
                    default:
                        return Color.RED; // Rojo para otros operadores 
                }
            case "Operador Relacional":
                switch (lexema) {
                    case "==":
                        return new Color(0x6A00FF); // Púrpura signo Igual
                    case "<>":
                        return new Color(0x3F2212); // Marron oscuro  Diferente
                    case ">":
                        return new Color(0xD9D441); // Amarillo para Mayor que
                    case "<":
                        return new Color(0xD94A41); // Rojo Menor que
                    case ">=":
                        return new Color(0xE3C800); // Amarillo O  Mayor o Igual que
                    case "<=":
                        return new Color(0xF0A30A); // Naranja Menor o Igual que
                    default:
                        return Color.ORANGE;
                }
            case "Operador Lógico":
                switch (lexema) {
                    case "And":
                        return new Color(0x414ED9); // Azul para Y (And)
                    case "Or":
                        return new Color(0x41D95D); // Verde para O (Or)
                    case "Not":
                        return new Color(0xA741D9); // Púrpura Negación = Not
                    default:
                        return Color.MAGENTA;
                }
            case "Operador de Asignación":
                if (lexema.equals("=")) {
                    return new Color(0x41D9D4); // Cian Asignacion Simple
                } else {
                    return Color.WHITE; // Blanco  Asignacion Compuesta
                }
            case "Número Entero":
                return new Color(0x1BA1E2); // Azul Cl para Enteros
            case "Número Decimal":
                return new Color(0xFFFF88); // Amarillo Cl para Decimales
            case "Cadena de Texto":
                return new Color(0xE51400); // Rojo para Cadenas de Texto
            case "Booleano":
                return new Color(0xFA6800); // Naranja para Booleanos
            case "Carácter":
                return new Color(0x0050EF); // Azul para Caracteres
            case "Comentario":
                return new Color(0xB3B3B3); // Gris para Comentarios
            case "Signo":
                switch (lexema) {
                    case "(":
                    case ")":
                        return new Color(0x9AD8DB); // Azul bajo para Paréntesis
                    case "{":
                    case "}":
                        return new Color(0xDBD29A); // Beige para Llaves
                    case "[":
                    case "]":
                        return new Color(0xDBA49A); // Marron var para Corchetes
                    case ",":
                        return new Color(0xB79ADB); // Lavanda para Comas
                    case ".":
                        return new Color(0x9ADBA6); // Verde Grisáceo para Puntos
                    default:
                        return Color.GRAY;
                }
            case "Square.Color":
                int[] posicion = new int[2];
                Color color = procesarSquareColor(lexema, posicion); // Llama al método especial para procesar `Square.Color`
                return color; // Se retorna el color para el cuadrito seleccionado con el token espcial
            case "Error Léxico":
                return Color.BLACK;
            default:
                return Color.GRAY;
        }
    }

    private Color procesarSquareColor(String lexema, int[] posicion) {
        try {
            // Eliminar "Square.Color(" y ")"
            String parametros = lexema.substring(13, lexema.length() - 1);
            String[] partes = parametros.split(",");

            // Verifica que partes tenga tres elementos: color, fila y columna
            if (partes.length != 3) {
                return Color.PINK; // Retornar color de error si el formato no es correcto
            }

            // Obtener el color hexadecimal
            String colorHex = partes[0].trim();
            int fila = Integer.parseInt(partes[1].trim()) - 1; // de índice de fila
            int columna = Integer.parseInt(partes[2].trim()) - 1; //  de índice de columna

            // Guardar las posiciones en el array proporcionado
            posicion[0] = fila;
            posicion[1] = columna;

            // Convertir el color hexadecimal a un objeto Color
            return Color.decode(colorHex);
        } catch (Exception e) {
            e.printStackTrace();
            return Color.PINK; // Si ocurre un error, retornar un color de error
        }
    }
}

