import java.io.IOException;

/*
 * Clase Main
 * Punto de entrada del programa. Crea el menu y lo muestra al usuario.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        menu.mostrar();
    }
}
