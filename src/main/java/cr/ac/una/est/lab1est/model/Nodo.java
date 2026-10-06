package cr.ac.una.est.lab1est.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Nodo {

    private final int numero;

    private double x;
    private double y;

    private final Circle circulo;
    private final Text texto;

    public Nodo(
            int numero,
            double x,
            double y
    ) {

        this.numero = numero;

        this.x = x;
        this.y = y;

        circulo =
                new Circle(
                        x,
                        y,
                        25
                );

        circulo.setFill(Color.WHITE);
        circulo.setStroke(Color.BLACK);
        circulo.setStrokeWidth(2);

        texto =
                new Text(
                        x - 5,
                        y + 5,
                        String.valueOf(numero)
                );

        texto.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );
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

    public Circle getCirculo() {
        return circulo;
    }

    public Text getTexto() {
        return texto;
    }

    public void mover(
            double nuevoX,
            double nuevoY
    ) {

        x = nuevoX;
        y = nuevoY;

        circulo.setCenterX(x);
        circulo.setCenterY(y);

        texto.setX(x - 5);
        texto.setY(y + 5);
    }
}