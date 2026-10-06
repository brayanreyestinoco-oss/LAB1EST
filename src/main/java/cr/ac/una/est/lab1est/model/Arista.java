package cr.ac.una.est.lab1est.model;

import javafx.scene.shape.Line;

public class Arista {

    private final Nodo nodo1;
    private final Nodo nodo2;

    private final Line linea;

    public Arista(
            Nodo nodo1,
            Nodo nodo2
    ) {

        this.nodo1 = nodo1;
        this.nodo2 = nodo2;

        linea =
                new Line(
                        nodo1.getX(),
                        nodo1.getY(),
                        nodo2.getX(),
                        nodo2.getY()
                );

        linea.setStrokeWidth(2);
    }

    public Nodo getNodo1() {
        return nodo1;
    }

    public Nodo getNodo2() {
        return nodo2;
    }

    public Line getLinea() {
        return linea;
    }

    public void actualizar() {

        linea.setStartX(
                nodo1.getX()
        );

        linea.setStartY(
                nodo1.getY()
        );

        linea.setEndX(
                nodo2.getX()
        );

        linea.setEndY(
                nodo2.getY()
        );
    }
}