package cr.ac.una.est.lab1est.controller;

import cr.ac.una.est.lab1est.model.Grafo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatrizController {

    @FXML
    private TextField txtCantidad;

    @FXML
    private GridPane gridMatriz;

    @FXML
    private TextArea txtLista;

    @FXML
    private Label lblBFS;

    @FXML
    private Label lblDFS;


    private final List<List<TextField>> campos =
            new ArrayList<>();

    private int cantidad = 0;

    private boolean sincronizando = false;


    // =========================================================
    // GENERAR MATRIZ
    // =========================================================

    @FXML
    private void generarMatriz() {

        try {

            cantidad = Integer.parseInt(
                    txtCantidad.getText().trim()
            );

            if (cantidad < 1 || cantidad > 15) {

                mostrarError(
                        "La cantidad debe estar entre 1 y 15."
                );

                return;
            }

            crearMatriz();

            limpiarResultados();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Ingrese una cantidad válida."
            );
        }
    }


    // =========================================================
    // CREAR MATRIZ
    // =========================================================

    private void crearMatriz() {

        gridMatriz.getChildren().clear();

        campos.clear();

        sincronizando = false;


        // Encabezados
        for (int i = 0; i < cantidad; i++) {

            Label columna =
                    new Label(
                            String.valueOf(i + 1)
                    );

            columna.getStyleClass().add(
                    "encabezado-matriz"
            );

            gridMatriz.add(
                    columna,
                    i + 1,
                    0
            );


            Label fila =
                    new Label(
                            String.valueOf(i + 1)
                    );

            fila.getStyleClass().add(
                    "encabezado-matriz"
            );

            gridMatriz.add(
                    fila,
                    0,
                    i + 1
            );
        }


        // Celdas
        for (int i = 0; i < cantidad; i++) {

            List<TextField> fila =
                    new ArrayList<>();


            for (int j = 0; j < cantidad; j++) {

                TextField campo =
                        new TextField("0");

                campo.setPrefWidth(55);
                campo.setPrefHeight(40);

                campo.setAlignment(
                        Pos.CENTER
                );


                // La diagonal siempre es 0
                if (i == j) {

                    campo.setDisable(true);
                }


                final int filaActual = i;
                final int columnaActual = j;


                campo.textProperty().addListener(
                        (obs, anterior, nuevo) -> {

                            if (sincronizando) {
                                return;
                            }


                            // Permitir borrar temporalmente
                            if (nuevo.isEmpty()) {
                                return;
                            }


                            try {

                                int valor =
                                        Integer.parseInt(
                                                nuevo
                                        );


                                // No se permiten negativos
                                if (valor < 0) {

                                    sincronizando = true;

                                    campo.setText(
                                            anterior
                                    );

                                    sincronizando = false;

                                    return;
                                }


                                // Mantener matriz simétrica
                                if (filaActual != columnaActual) {

                                    sincronizar(
                                            filaActual,
                                            columnaActual,
                                            nuevo
                                    );
                                }

                            } catch (NumberFormatException e) {

                                sincronizando = true;

                                campo.setText(
                                        anterior
                                );

                                sincronizando = false;
                            }
                        }
                );


                fila.add(campo);


                gridMatriz.add(
                        campo,
                        j + 1,
                        i + 1
                );
            }


            campos.add(fila);
        }
    }


    // =========================================================
    // SINCRONIZAR MATRIZ SIMÉTRICA
    // =========================================================

    private void sincronizar(
            int fila,
            int columna,
            String valor) {

        if (sincronizando) {
            return;
        }

        sincronizando = true;

        campos.get(columna)
                .get(fila)
                .setText(valor);

        sincronizando = false;
    }


    // =========================================================
    // OBTENER MATRIZ
    // =========================================================

    private int[][] obtenerMatriz() {

        int[][] matriz =
                new int[cantidad][cantidad];


        for (int i = 0; i < cantidad; i++) {

            for (int j = 0; j < cantidad; j++) {

                String valor =
                        campos.get(i)
                                .get(j)
                                .getText()
                                .trim();


                if (valor.isEmpty()) {

                    matriz[i][j] = 0;

                } else {

                    try {

                        int numero =
                                Integer.parseInt(valor);

                        if (numero < 0) {
                            numero = 0;
                        }

                        matriz[i][j] = numero;

                    } catch (NumberFormatException e) {

                        matriz[i][j] = 0;
                    }
                }
            }
        }


        return matriz;
    }


    // =========================================================
    // MATRIZ → GRAFO
    // =========================================================

    @FXML
    private void dibujarGrafo()
            throws Exception {

        if (cantidad == 0 ||
                campos.isEmpty()) {

            mostrarError(
                    "Primero genere la matriz."
            );

            return;
        }


        int[][] matriz =
                obtenerMatriz();


        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/cr/ac/una/est/lab1est/grafo.fxml"
                        )
                );


        if (loader.getLocation() == null) {

            throw new IllegalStateException(
                    "No se encontró grafo.fxml. " +
                            "Revise src/main/resources/cr/ac/una/est/lab1est/"
            );
        }


        Scene scene =
                new Scene(
                        loader.load()
                );


        GrafoController controller =
                loader.getController();


        controller.cargarMatriz(
                matriz
        );


        var css =
                getClass().getResource(
                        "/cr/ac/una/est/lab1est/estilos.css"
                );


        if (css != null) {

            scene.getStylesheets().add(
                    css.toExternalForm()
            );
        }


        Stage stage =
                new Stage();

        stage.setTitle(
                "Grafo generado"
        );

        stage.setScene(scene);

        stage.setMinWidth(1150);
        stage.setMinHeight(750);

        stage.show();
    }


    // =========================================================
    // CALCULAR RESULTADOS
    // =========================================================

    @FXML
    private void generarResultados() {

        if (cantidad == 0 ||
                campos.isEmpty()) {

            mostrarError(
                    "Primero genere la matriz."
            );

            return;
        }


        try {

            int[][] matriz =
                    obtenerMatriz();


            Grafo grafo =
                    new Grafo();


            grafo.cargarMatriz(
                    matriz
            );


            mostrarResultados(
                    grafo
            );

        } catch (Exception e) {

            mostrarError(
                    "No se pudo procesar la matriz."
            );
        }
    }


    // =========================================================
    // CARGAR MATRIZ DESDE EL GRAFO
    // =========================================================

    public void cargarMatriz(
            int[][] matriz) {

        if (matriz == null ||
                matriz.length == 0) {

            return;
        }


        cantidad =
                matriz.length;


        txtCantidad.setText(
                String.valueOf(cantidad)
        );


        crearMatriz();


        sincronizando = true;


        for (int i = 0; i < cantidad; i++) {

            for (int j = 0; j < cantidad; j++) {

                campos.get(i)
                        .get(j)
                        .setText(
                                String.valueOf(
                                        matriz[i][j]
                                )
                        );
            }
        }


        sincronizando = false;


        Grafo grafo =
                new Grafo();


        grafo.cargarMatriz(
                matriz
        );


        mostrarResultados(
                grafo
        );
    }


    // =========================================================
    // MOSTRAR RESULTADOS
    // =========================================================

    private void mostrarResultados(
            Grafo grafo) {

        StringBuilder texto =
                new StringBuilder();


        for (Map.Entry<Integer, List<String>> entrada :
                grafo.listaAdyacencia().entrySet()) {

            texto.append("Nodo ")
                    .append(
                            entrada.getKey()
                    )
                    .append(" -> ");


            if (entrada.getValue().isEmpty()) {

                texto.append(
                        "sin conexiones"
                );

            } else {

                texto.append(
                        String.join(
                                ", ",
                                entrada.getValue()
                        )
                );
            }


            texto.append("\n");
        }


        txtLista.setText(
                texto.toString()
        );


        if (!grafo.getNodos().isEmpty()) {

            lblBFS.setText(
                    "Anchura (BFS): "
                            + grafo.recorridoAnchura(1)
            );

            lblDFS.setText(
                    "Profundidad (DFS): "
                            + grafo.recorridoProfundidad(1)
            );

        } else {

            lblBFS.setText(
                    "Anchura (BFS): -"
            );

            lblDFS.setText(
                    "Profundidad (DFS): -"
            );
        }
    }


    // =========================================================
    // LIMPIAR RESULTADOS
    // =========================================================

    private void limpiarResultados() {

        txtLista.clear();

        lblBFS.setText(
                "Anchura (BFS): -"
        );

        lblDFS.setText(
                "Profundidad (DFS): -"
        );
    }


    // =========================================================
    // MOSTRAR ERROR
    // =========================================================

    private void mostrarError(
            String mensaje) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(null);

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}