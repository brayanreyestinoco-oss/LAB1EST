package cr.ac.una.est.lab1est.controller;

import cr.ac.una.est.lab1est.model.Grafo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML
    private void generarMatriz() {

        try {

            cantidad =
                    Integer.parseInt(
                            txtCantidad.getText()
                    );

            if (cantidad < 1 ||
                    cantidad > 15) {

                mostrarError(
                        "La cantidad debe estar entre 1 y 15."
                );

                return;
            }

            crearMatriz();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Ingrese una cantidad válida."
            );
        }
    }

    private void crearMatriz() {

        gridMatriz.getChildren().clear();

        campos.clear();

        for (int i = 0; i < cantidad; i++) {

            Label columna =
                    new Label(
                            String.valueOf(i + 1)
                    );

            columna.getStyleClass()
                    .add("encabezado-matriz");

            gridMatriz.add(
                    columna,
                    i + 1,
                    0
            );

            Label fila =
                    new Label(
                            String.valueOf(i + 1)
                    );

            fila.getStyleClass()
                    .add("encabezado-matriz");

            gridMatriz.add(
                    fila,
                    0,
                    i + 1
            );
        }

        for (int i = 0; i < cantidad; i++) {

            List<TextField> fila =
                    new ArrayList<>();

            for (int j = 0;
                 j < cantidad;
                 j++) {

                TextField campo =
                        new TextField("0");

                campo.setPrefWidth(55);
                campo.setPrefHeight(40);

                campo.setAlignment(
                        Pos.CENTER
                );

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

                            if (nuevo.isEmpty()) {
                                return;
                            }

                            try {

                                int valor =
                                        Integer.parseInt(nuevo);

                                if (valor < 0) {

                                    campo.setText(
                                            anterior
                                    );

                                    return;
                                }

                                if (filaActual !=
                                        columnaActual) {

                                    sincronizar(
                                            filaActual,
                                            columnaActual,
                                            nuevo
                                    );
                                }

                            } catch (NumberFormatException e) {

                                campo.setText(
                                        anterior
                                );
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

    private int[][] obtenerMatriz() {

        int[][] matriz =
                new int[cantidad][cantidad];

        for (int i = 0; i < cantidad; i++) {

            for (int j = 0; j < cantidad; j++) {

                String valor =
                        campos.get(i)
                                .get(j)
                                .getText();

                if (valor.isEmpty()) {

                    matriz[i][j] = 0;

                } else {

                    matriz[i][j] =
                            Integer.parseInt(valor);
                }
            }
        }

        return matriz;
    }

    @FXML
    private void dibujarGrafo()
            throws Exception {

        if (cantidad == 0) {

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
                                "/cr.ac.una.est.lab1est/grafo.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        GrafoController controller =
                loader.getController();

        controller.cargarMatriz(matriz);

        scene.getStylesheets().add(
                getClass().getResource(
                        "/cr.ac.una.est.lab1est/estilos.css"
                ).toExternalForm()
        );

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

    @FXML
    private void generarResultados() {

        if (cantidad == 0) {

            mostrarError(
                    "Primero genere la matriz."
            );

            return;
        }

        Grafo grafo =
                new Grafo();

        grafo.cargarMatriz(
                obtenerMatriz()
        );

        mostrarResultados(grafo);
    }

    public void cargarMatriz(
            int[][] matriz) {

        cantidad =
                matriz.length;

        txtCantidad.setText(
                String.valueOf(cantidad)
        );

        crearMatriz();

        for (int i = 0;
             i < cantidad;
             i++) {

            for (int j = 0;
                 j < cantidad;
                 j++) {

                campos.get(i)
                        .get(j)
                        .setText(
                                String.valueOf(
                                        matriz[i][j]
                                )
                        );
            }
        }

        Grafo grafo =
                new Grafo();

        grafo.cargarMatriz(matriz);

        mostrarResultados(grafo);
    }

    private void mostrarResultados(
            Grafo grafo) {

        StringBuilder texto =
                new StringBuilder();

        for (Map.Entry<Integer, List<String>>
                entrada :
                grafo.listaAdyacencia()
                        .entrySet()) {

            texto.append("Nodo ")
                    .append(
                            entrada.getKey()
                    )
                    .append(" -> ");

            if (entrada.getValue()
                    .isEmpty()) {

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

    private void mostrarError(
            String mensaje) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}