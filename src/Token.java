/*
 * Clase Token
 * Representa cada elemento que forma parte de una expresion aritmetica.
 * Un token puede ser un numero, un operador o un parentesis.
 */
public class Token {
    private String valor;
    private String tipo;

    // Constructor a partir del valor se determina automaticamente el tipo
    public Token(String valor) {
        this.valor = valor;
        this.tipo = determinarTipo(valor);
    }

    // Clasifica el token segun el caracter o conjunto de caracteres recibido
    private String determinarTipo(String valor) {
        if (valor.matches("\\d+")) {
            return "NUMERO";
        } else if (valor.equals("+") || valor.equals("-") || valor.equals("*") || valor.equals("/")) {
            return "OPERADOR";
        } else if (valor.equals("(")) {
            return "PARENTESIS_IZQ";
        } else if (valor.equals(")")) {
            return "PARENTESIS_DER";
        } else {
            return "DESCONOCIDO";
        }
    }

    // Getters
    public String getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    // toString: devuelve el valor del token
    public String toString() {
        return valor;
    }
}
