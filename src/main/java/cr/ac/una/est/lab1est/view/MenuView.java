package cr.ac.una.est.lab1est.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuView extends VBox {

    private final Button btnMatriz;
    private final Button btnGrafo;

    public MenuView() {

        setAlignment(
                Pos.CENTER
        );

        setSpacing(25);

        getStyleClass()
                .add("menu");

        Label titulo =
                new Label(
                        "PROYECTO DE GRAFOS"
                );

        titulo.getStyleClass()
                .add("titulo");

        Label subtitulo =
                new Label(
                        "Seleccione una opción"
                );

        subtitulo.getStyleClass()
                .add("subtitulo");

        btnMatriz =
                new Button(
                        "1. Matriz de Adyacencia → Grafo"
                );

        btnGrafo =
                new Button(
                        "2. Grafo → Matriz de Adyacencia"
                );

        btnMatriz
                .getStyleClass()
                .add("boton-menu");

        btnGrafo
                .getStyleClass()
                .add("boton-menu");

        getChildren().addAll(
                titulo,
                subtitulo,
                btnMatriz,
                btnGrafo
        );
    }

    public Button getBtnMatriz() {
        return btnMatriz;
    }

    public Button getBtnGrafo() {
        return btnGrafo;
    }
}