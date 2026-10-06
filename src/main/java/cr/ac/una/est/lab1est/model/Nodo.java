package cr.ac.una.est.lab1est.model;

public class Nodo {

    private final int numero;
    private double x;
    private double y;

    public Nodo(int numero, double x, double y) {
        this.numero = numero;
        this.x = x;
        this.y = y;
    }

    public int getNumero() {
        return numero;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}