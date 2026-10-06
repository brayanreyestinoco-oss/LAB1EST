package cr.ac.una.est.lab1est.model;

public class Arista {

    private final Nodo nodo1;
    private final Nodo nodo2;
    private int peso;

    public Arista(
            Nodo nodo1,
            Nodo nodo2,
            int peso) {

        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.peso = peso;
    }

    public Nodo getNodo1() {
        return nodo1;
    }

    public Nodo getNodo2() {
        return nodo2;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}