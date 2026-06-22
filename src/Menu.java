import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * El usuario ingresa una expresion y el programa, apoyandose en la pila,
 * la valida, la convierte a notacion postfija y la evalua.
 */
public class Menu {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Pila pila = new Pila();

    public void mostrar() throws IOException {
        int opcion = 0;

        do {
            System.out.println("\n===== ANALIZADOR DE EXPRESIONES CON PILA =====");
            System.out.println("1. Analizar una expresion aritmetica");
            System.out.println("2. Salir");
            System.out.print("Opcion: ");

            String entrada = reader.readLine();
            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcion no valida. Ingrese un numero.");
                continue;
            }

            if (opcion == 1) {
                System.out.print("Ingrese la expresion: ");
                String expresion = reader.readLine();

                if (expresion == null || expresion.trim().isEmpty()) {
                    System.out.println("Expresion vacia.");
                } else {
                    System.out.println("\n--- Resultado del analisis ---");
                    if (pila.validarExpresion(expresion)) {
                        ArrayList<Token> postfija = pila.infijaAPostfija(expresion);
                        double resultado = pila.evaluarPostfija(postfija);

                        System.out.println("Es valida?         SI");
                        System.out.println("Notacion infija:   " + expresion);

                        System.out.print("Notacion postfija: ");
                        for (int i = 0; i < postfija.size(); i++) {
                            System.out.print(postfija.get(i).getValor() + " ");
                        }
                        System.out.println();

                        System.out.println("Resultado:         " + resultado);
                    } else {
                        System.out.println("Es valida?         NO");
                        System.out.println("Notacion infija:   " + expresion);
                        System.out.println("Notacion postfija: No disponible");
                    }
                }
            } else if (opcion == 2) {
                System.out.println("Saliendo del programa...");
            } else {
                System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 2);
    }
}
