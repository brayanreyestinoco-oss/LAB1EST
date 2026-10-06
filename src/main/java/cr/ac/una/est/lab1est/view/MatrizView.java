package cr.ac.una.est.lab1est.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MatrizView extends VBox {

    private final GridPane matrizGrid;

    private TextField[][] campos;

    private final TextField txtNodos;
    private final TextField txtNodoInicio;

    private final Button btnGenerar;
    private final Button btnDibujar;
    private final Button btnLista;
    private final Button btnBFS;
    private final Button btnDFS;
    private final Button btnRegresar;

    private final TextArea txtResultado;

    public MatrizView() {

        setAlignment(
                Pos.TOP_CENTER
        );

        setSpacing(12);

        getStyleClass()
                .add("contenedor");

        Label titulo =
                new Label(
                        "MATRIZ DE ADYACENCIA"
                );

        titulo.getStyleClass()
                .add("titulo-seccion");

        Label cantidadLabel =
                new Label(
                        "Cantidad de nodos:"
                );

        txtNodos =
                new TextField();

        txtNodos.setPromptText(
                "Ejemplo: 5"
        );

        txtNodos.setPrefWidth(100);

        btnGenerar =
                new Button(
                        "Generar matriz"
                );

        btnGenerar
                .getStyleClass()
                .add("boton");

        HBox controles =
                new HBox(
                        10,
                        cantidadLabel,
                        txtNodos,
                        btnGenerar
                );

        controles.setAlignment(
                Pos.CENTER
        );

        matrizGrid =
                new GridPane();

        matrizGrid.setHgap(5);
        matrizGrid.setVgap(5);

        matrizGrid.setAlignment(
                Pos.CENTER
        );

        Label nodoLabel =
                new Label(
                        "Nodo inicial para BFS / DFS:"
                );

        txtNodoInicio =
                new TextField();

        txtNodoInicio.setPromptText(
                "Ej: 1"
        );

        txtNodoInicio.setPrefWidth(80);

        btnDibujar =
                new Button(
                        "Dibujar grafo"
                );

        btnLista =
                new Button(
                        "Lista de adyacencia"
                );

        btnBFS =
                new Button(
                        "Recorrido BFS"
                );

        btnDFS =
                new Button(
                        "Recorrido DFS"
                );

        btnRegresar =
                new Button(
                        "Regresar"
                );

        btnDibujar
                .getStyleClass()
                .add("boton-principal");

        btnLista
                .getStyleClass()
                .add("boton");

        btnBFS
                .getStyleClass()
                .add("boton");

        btnDFS
                .getStyleClass()
                .add("boton");

        btnRegresar
                .getStyleClass()
                .add("boton");

        HBox botones =
                new HBox(
                        8,
                        btnDibujar,
                        btnLista,
                        btnBFS,
                        btnDFS,
                        btnRegresar
                );

        botones.setAlignment(
                Pos.CENTER
        );

        txtResultado =
                new TextArea();

        txtResultado.setEditable(
                false
        );

        txtResultado.setPrefRowCount(
                5
        );

        txtResultado.setPrefWidth(
                700
        );

        getChildren().addAll(
                titulo,
                controles,
                matrizGrid,
                nodoLabel,
                txtNodoInicio,
                botones,
                txtResultado
        );
    }

    // =====================================================
    // CREAR MATRIZ
    // =====================================================

    public void crearMatriz(
            int cantidad
    ) {

        matrizGrid
                .getChildren()
                .clear();

        campos =
                new TextField[cantidad][cantidad];

        // Encabezados

        for (int i = 0;
             i < cantidad;
             i++) {

            Label columna =
                    new Label(
                            String.valueOf(i + 1)
                    );

            columna.getStyleClass()
                    .add("encabezado");

            GridPane.setColumnIndex(
                    columna,
                    i + 1
            );

            GridPane.setRowIndex(
                    columna,
                    0
            );

            matrizGrid
                    .getChildren()
                    .add(columna);

            Label fila =
                    new Label(
                            String.valueOf(i + 1)
                    );

            fila.getStyleClass()
                    .add("encabezado");

            GridPane.setColumnIndex(
                    fila,
                    0
            );

            GridPane.setRowIndex(
                    fila,
                    i + 1
            );

            matrizGrid
                    .getChildren()
                    .add(fila);
        }

        // Celdas

        for (int fila = 0;
             fila < cantidad;
             fila++) {

            for (int columna = 0;
                 columna < cantidad;
                 columna++) {

                TextField campo =
                        new TextField("0");

                campo.setPrefWidth(50);
                campo.setPrefHeight(40);

                campo.setAlignment(
                        Pos.CENTER
                );

                final int f = fila;
                final int c = columna;

                // Diagonal = 0

                if (fila == columna) {

                    campo.setEditable(
                            false
                    );

                    campo.getStyleClass()
                            .add("diagonal");
                }

                campo.textProperty()
                        .addListener(
                                (
                                        observable,
                                        anterior,
                                        nuevo
                                ) -> {

                                    if (
                                            !nuevo.equals("0")
                                                    &&
                                                    !nuevo.equals("1")
                                                    &&
                                                    !nuevo.isEmpty()
                                    ) {

                                        campo.setText(
                                                anterior
                                        );

                                        return;
                                    }

                                    // Grafo no dirigido:
                                    // si cambia [i][j],
                                    // cambia [j][i].

                                    if (
                                            f != c
                                                    &&
                                                    (
                                                            nuevo.equals("0")
                                                                    ||
                                                                    nuevo.equals("1")
                                                    )
                                    ) {

                                        if (
                                                campos[c][f]
                                                        != null
                                        ) {

                                            campos[c][f]
                                                    .setText(
                                                            nuevo
                                                    );
                                        }
                                    }
                                }
                        );

                campos[fila][columna] =
                        campo;

                GridPane.setColumnIndex(
                        campo,
                        columna + 1
                );

                GridPane.setRowIndex(
                        campo,
                        fila + 1
                );

                matrizGrid
                        .getChildren()
                        .add(campo);
            }
        }
    }

    // =====================================================
    // OBTENER MATRIZ
    // =====================================================

    public int[][] obtenerMatriz() {

        if (campos == null) {
            return null;
        }

        int cantidad =
                campos.length;

        int[][] matriz =
                new int[cantidad][cantidad];

        for (int i = 0;
             i < cantidad;
             i++) {

            for (int j = 0;
                 j < cantidad;
                 j++) {

                String valor =
                        campos[i][j]
                                .getText();

                if (
                        !valor.equals("0")
                                &&
                                !valor.equals("1")
                ) {

                    return null;
                }

                matriz[i][j] =
                        Integer.parseInt(
                                valor
                        );
            }
        }

        return matriz;
    }

    // =====================================================
    // MOSTRAR MATRIZ
    // =====================================================

    public void mostrarMatriz(
            int[][] matriz
    ) {

        int cantidad =
                matriz.length;

        matrizGrid
                .getChildren()
                .clear();

        campos =
                new TextField[cantidad][cantidad];

        for (int i = 0;
             i < cantidad;
             i++) {

            Label columna =
                    new Label(
                            String.valueOf(i + 1)
                    );

            columna.getStyleClass()
                    .add("encabezado");

            GridPane.setColumnIndex(
                    columna,
                    i + 1
            );

            GridPane.setRowIndex(
                    columna,
                    0
            );

            matrizGrid
                    .getChildren()
                    .add(columna);

            Label fila =
                    new Label(
                            String.valueOf(i + 1)
                    );

            fila.getStyleClass()
                    .add("encabezado");

            GridPane.setColumnIndex(
                    fila,
                    0
            );

            GridPane.setRowIndex(
                    fila,
                    i + 1
            );

            matrizGrid
                    .getChildren()
                    .add(fila);
        }

        for (int i = 0;
             i < cantidad;
             i++) {

            for (int j = 0;
                 j < cantidad;
                 j++) {

                TextField campo =
                        new TextField(
                                String.valueOf(
                                        matriz[i][j]
                                )
                        );

                campo.setPrefWidth(50);
                campo.setPrefHeight(40);

                campo.setAlignment(
                        Pos.CENTER
                );

                campo.setEditable(
                        false
                );

                if (i == j) {

                    campo.getStyleClass()
                            .add("diagonal");
                }

                campos[i][j] =
                        campo;

                GridPane.setColumnIndex(
                        campo,
                        j + 1
                );

                GridPane.setRowIndex(
                        campo,
                        i + 1
                );

                matrizGrid
                        .getChildren()
                        .add(campo);
            }
        }
    }

    public TextField getTxtNodos() {
        return txtNodos;
    }

    public TextField getTxtNodoInicio() {
        return txtNodoInicio;
    }

    public TextArea getTxtResultado() {
        return txtResultado;
    }

    public Button getBtnGenerar() {
        return btnGenerar;
    }

    public Button getBtnDibujar() {
        return btnDibujar;
    }

    public Button getBtnLista() {
        return btnLista;
    }

    public Button getBtnBFS() {
        return btnBFS;
    }

    public Button getBtnDFS() {
        return btnDFS;
    }

    public Button getBtnRegresar() {
        return btnRegresar;
    }
}