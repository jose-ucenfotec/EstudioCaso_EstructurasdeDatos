public class Nodo {
    private Token token;
    private Nodo siguiente;

    // Constructor
    public Nodo(Token token) {
        this.token = token;
        this.siguiente = null;
    }

    // Getters
    public Token getToken() {
        return token;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    // Setters
    public void setToken(Token token) {
        this.token = token;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    // toString
    public String toString() {
        return token.toString();
    }
}
