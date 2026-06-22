import java.util.ArrayList;

/*
 * Pila (LIFO) construida con nodos enlazados.
 * La cima es el unico punto de acceso.
 */
public class Pila {
    private Nodo cima;   // referencia al nodo del tope de la pila
    private int tamano;  // cantidad de elementos almacenados

    // Constructor: la pila inicia vacia
    public Pila() {
        cima = null;
        tamano = 0;
    }

    // Apila un token colocandolo en la cima
    public void push(Token token) {
        Nodo nuevo = new Nodo(token);
        nuevo.setSiguiente(cima);
        cima = nuevo;
        tamano++;
    }

    // Desapila y devuelve el token de la cima
    public Token pop() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return null;
        }
        Token token = cima.getToken();
        cima = cima.getSiguiente();
        tamano--;
        return token;
    }

    // Consulta el token de la cima
    public Token peek() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return null;
        }
        return cima.getToken();
    }

    // Indica si la pila no tiene elementos
    public boolean isEmpty() {
        return cima == null;
    }

    // Devuelve la cantidad de elementos
    public int size() {
        return tamano;
    }

    // Divide la cadena de texto en tokens con significado (tokenizar).
    // Los digitos consecutivos se agrupan en un solo token NUMERO.
    private ArrayList<Token> tokenizar(String expresion) {
        ArrayList<Token> tokens = new ArrayList<>();
        String expr = expresion.replaceAll("\\s+", "");
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            if (Character.isDigit(c)) {
                StringBuilder numero = new StringBuilder();
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    numero.append(expr.charAt(i));
                    i++;
                }
                tokens.add(new Token(numero.toString()));
            } else {
                tokens.add(new Token(String.valueOf(c)));
                i++;
            }
        }
        return tokens;
    }

    // Prioridad de los operadores
    private int prioridad(String operador) {
        switch (operador) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            default: return 0;
        }
    }

    // Realiza la operacion aritmetica entre dos numeros
    private double operar(double a, double b, String operador) {
        switch (operador) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) {
                    System.out.println("Motivo: division por cero.");
                    return 0;
                }
                return a / b;
            default: return 0;
        }
    }


    // Valida la expresion, viendo caracteres permitidos, parentesis balanceados, que no inicie/termine con operador y que no haya operadores seguidos.
    public boolean validarExpresion(String expresion) {
        String expr = expresion.replaceAll("\\s+", "");

        if (expr.isEmpty()) return false;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (!"0123456789+-*/()".contains(String.valueOf(c))) {
                System.out.println("Motivo: caracter no valido -> '" + c + "'");
                return false;
            }
        }

        // El balance de parentesis se verifica con una pila: se apila cada  '(' y se desapila al encontrar ')'. Si al final la pila no queda vacia, los parentesis estan desbalanceados.
        Pila pilaParentesis = new Pila();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                pilaParentesis.push(new Token("("));
            } else if (c == ')') {
                if (pilaParentesis.isEmpty()) {
                    System.out.println("Motivo: parentesis de cierre sin apertura.");
                    return false;
                }
                pilaParentesis.pop();
            }
        }
        if (!pilaParentesis.isEmpty()) {
            System.out.println("Motivo: parentesis desbalanceados.");
            return false;
        }

        char ultimo = expr.charAt(expr.length() - 1);
        if (ultimo == '+' || ultimo == '-' || ultimo == '*' || ultimo == '/') {
            System.out.println("Motivo: la expresion no puede terminar con un operador.");
            return false;
        }

        char primero = expr.charAt(0);
        if (primero == '*' || primero == '/' || primero == '+') {
            System.out.println("Motivo: la expresion no puede empezar con '" + primero + "'");
            return false;
        }

        ArrayList<Token> tokens = tokenizar(expr);
        for (int i = 1; i < tokens.size(); i++) {
            boolean actualEsOp = tokens.get(i).getTipo().equals("OPERADOR");
            boolean anteriorEsOp = tokens.get(i - 1).getTipo().equals("OPERADOR");
            if (actualEsOp && anteriorEsOp) {
                System.out.println("Motivo: operadores consecutivos -> '"
                        + tokens.get(i - 1).getValor() + tokens.get(i).getValor() + "'");
                return false;
            }
        }
        return true;
    }

    // Conversion de notacion infija a postfija.
    // La pila de operadores es la que ordena la salida segun la prioridad.
    public ArrayList<Token> infijaAPostfija(String expresion) {
        ArrayList<Token> salida = new ArrayList<>();
        Pila pilaOperadores = new Pila();
        ArrayList<Token> tokens = tokenizar(expresion);

        for (Token token : tokens) {
            String tipo = token.getTipo();
            if (tipo.equals("NUMERO")) {
                salida.add(token);
            } else if (tipo.equals("OPERADOR")) {
                while (!pilaOperadores.isEmpty()
                        && pilaOperadores.peek().getTipo().equals("OPERADOR")
                        && prioridad(pilaOperadores.peek().getValor()) >= prioridad(token.getValor())) {
                    salida.add(pilaOperadores.pop());
                }
                pilaOperadores.push(token);
            } else if (tipo.equals("PARENTESIS_IZQ")) {
                pilaOperadores.push(token);
            } else if (tipo.equals("PARENTESIS_DER")) {
                while (!pilaOperadores.isEmpty()
                        && !pilaOperadores.peek().getTipo().equals("PARENTESIS_IZQ")) {
                    salida.add(pilaOperadores.pop());
                }
                if (!pilaOperadores.isEmpty()) pilaOperadores.pop();
            }
        }

        while (!pilaOperadores.isEmpty()) {
            salida.add(pilaOperadores.pop());
        }
        return salida;
    }

    // Evaluacion de la expresion postfija usando una pila de numeros.
    public double evaluarPostfija(ArrayList<Token> postfija) {
        Pila pilaNumeros = new Pila();

        for (Token token : postfija) {
            if (token.getTipo().equals("NUMERO")) {
                pilaNumeros.push(token);
            } else if (token.getTipo().equals("OPERADOR")) {
                Token b = pilaNumeros.pop();
                Token a = pilaNumeros.pop();

                if (a == null || b == null) {
                    System.out.println("Expresion invalida durante la evaluacion.");
                    return 0;
                }

                double numA = Double.parseDouble(a.getValor());
                double numB = Double.parseDouble(b.getValor());
                double resultado = operar(numA, numB, token.getValor());

                pilaNumeros.push(new Token(String.valueOf(resultado)));
            }
        }

        Token resultado = pilaNumeros.pop();
        if (resultado != null) {
            return Double.parseDouble(resultado.getValor());
        } else {
            return 0;
        }
    }
}
