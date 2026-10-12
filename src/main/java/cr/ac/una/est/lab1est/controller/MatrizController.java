package cr.ac.una.est.lab1est.controller;

import cr.ac.una.est.lab1est.Main;
import cr.ac.una.est.lab1est.model.Grafo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    private final List<List<TextField>> campos = new ArrayList<>();
    private int cantidad = 0;
    private boolean sincronizando = false;

    // Genera una matriz nueva solo cuando la cantidad ingresada es válida.
    @FXML
    private void generarMatriz() {
        final int nuevaCantidad;

        try {
            nuevaCantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Ingrese una cantidad entera válida de nodos.");
            return;
        }

        if (nuevaCantidad < 1 || nuevaCantidad > 15) {
            mostrarError("La cantidad de nodos debe estar entre 1 y 15.");
            return;
        }

        // La cantidad anterior no se modifica si la entrada nueva no es válida.
        cantidad = nuevaCantidad;
        crearMatriz();
        limpiarResultados();
    }

    // Crea los encabezados y las celdas editables de la matriz.
    private void crearMatriz() {
        gridMatriz.getChildren().clear();
        campos.clear();
        sincronizando = false;

        // Encabezados de columnas y filas.
        for (int i = 0; i < cantidad; i++) {
            Label columna = new Label(String.valueOf(i + 1));
            columna.getStyleClass().add("encabezado-matriz");
            gridMatriz.add(columna, i + 1, 0);

            Label fila = new Label(String.valueOf(i + 1));
            fila.getStyleClass().add("encabezado-matriz");
            gridMatriz.add(fila, 0, i + 1);
        }

        // Celdas de la matriz.
        for (int i = 0; i < cantidad; i++) {
            List<TextField> fila = new ArrayList<>();

            for (int j = 0; j < cantidad; j++) {
                TextField campo = new TextField("0");
                campo.setPrefWidth(55);
                campo.setPrefHeight(40);
                campo.setAlignment(Pos.CENTER);

                // No se permiten lazos: la diagonal siempre es cero.
                if (i == j) {
                    campo.setDisable(true);
                }

                final int filaActual = i;
                final int columnaActual = j;

                campo.textProperty().addListener((obs, anterior, nuevo) -> {
                    if (sincronizando || filaActual == columnaActual) {
                        return;
                    }

                    // Se permite dejar la celda vacía durante la edición.
                    // La celda simétrica también se vacía y se interpretará como 0
                    // si se procesa la matriz antes de que se escriba otro valor.
                    if (nuevo == null || nuevo.isEmpty()) {
                        sincronizar(filaActual, columnaActual, "");
                        return;
                    }

                    try {
                        int valor = Integer.parseInt(nuevo);
                        if (valor < 0) {
                            restaurarTexto(campo, anterior);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // No se aceptan letras, decimales ni otros caracteres.
                        restaurarTexto(campo, anterior);
                        return;
                    }

                    // La matriz representa un grafo no dirigido, por eso es simétrica.
                    sincronizar(filaActual, columnaActual, nuevo);
                });

                fila.add(campo);
                gridMatriz.add(campo, j + 1, i + 1);
            }

            campos.add(fila);
        }
    }

    private void restaurarTexto(TextField campo, String textoAnterior) {
        sincronizando = true;
        try {
            campo.setText(textoAnterior == null ? "" : textoAnterior);
        } finally {
            sincronizando = false;
        }
    }

    // Mantiene sincronizadas las dos posiciones simétricas A[i][j] y A[j][i].
    private void sincronizar(int fila, int columna, String valor) {
        if (sincronizando || campos.isEmpty()) {
            return;
        }

        sincronizando = true;
        try {
            campos.get(columna).get(fila).setText(valor);
        } finally {
            sincronizando = false;
        }
    }

    // Convierte el contenido visual de las celdas en una matriz de enteros.
    private int[][] obtenerMatriz() {
        int[][] matriz = new int[cantidad][cantidad];

        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                String valor = campos.get(i).get(j).getText();

                if (valor == null || valor.trim().isEmpty()) {
                    matriz[i][j] = 0;
                    continue;
                }

                final int numero;
                try {
                    numero = Integer.parseInt(valor.trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "La celda de la fila " + (i + 1)
                                    + " y columna " + (j + 1)
                                    + " debe contener un entero no negativo."
                    );
                }

                if (numero < 0) {
                    throw new IllegalArgumentException(
                            "Los valores de la matriz no pueden ser negativos."
                    );
                }

                matriz[i][j] = numero;
            }
        }

        // Verifica que la matriz siga siendo simétrica.
        for (int i = 0; i < cantidad; i++) {
            if (matriz[i][i] != 0) {
                throw new IllegalArgumentException(
                        "La diagonal principal debe contener únicamente ceros."
                );
            }

            for (int j = i + 1; j < cantidad; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    throw new IllegalArgumentException(
                            "La matriz debe ser simétrica para representar un grafo no dirigido."
                    );
                }
            }
        }

        return matriz;
    }

    // Opción 1: convierte la matriz de adyacencia en un grafo dibujado.
    @FXML
    private void dibujarGrafo() {
        if (cantidad == 0 || campos.isEmpty()) {
            mostrarError("Primero genere una matriz.");
            return;
        }

        try {
            int[][] matriz = obtenerMatriz();
            URL recurso = getClass().getResource(
                    "/cr/ac/una/est/lab1est/grafo.fxml"
            );

            if (recurso == null) {
                mostrarError("No se encontró el archivo grafo.fxml.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(recurso);
            Scene scene = new Scene(loader.load());

            GrafoController controller = loader.getController();
            controller.cargarMatriz(matriz);

            URL css = getClass().getResource(
                    "/cr/ac/una/est/lab1est/estilos.css"
            );
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            Stage stage = new Stage();
            stage.setUserData(txtCantidad.getScene().getWindow());
            stage.setTitle("Grafo generado desde la matriz");
            stage.setScene(scene);
            stage.setMinWidth(1150);
            stage.setMinHeight(750);
            stage.show();

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (IOException e) {
            mostrarError("No se pudo generar el grafo. Detalle: " + e.getMessage());
        }
    }

    // Calcula la lista de adyacencia y los recorridos BFS y DFS.
    @FXML
    private void generarResultados() {
        if (cantidad == 0 || campos.isEmpty()) {
            mostrarError("Primero genere una matriz.");
            return;
        }

        try {
            Grafo grafo = new Grafo();
            grafo.cargarMatriz(obtenerMatriz());
            mostrarResultados(grafo);
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    // Se usa cuando la matriz se genera desde el editor gráfico.
    public void cargarMatriz(int[][] matriz) {
        if (matriz == null || matriz.length == 0) {
            mostrarError("No se recibió una matriz válida.");
            return;
        }

        if (matriz.length > 15) {
            mostrarError("La matriz recibida supera el máximo permitido de 15 nodos.");
            return;
        }

        for (int[] fila : matriz) {
            if (fila == null || fila.length != matriz.length) {
                mostrarError("La matriz recibida no es cuadrada.");
                return;
            }
        }

        cantidad = matriz.length;
        txtCantidad.setText(String.valueOf(cantidad));
        crearMatriz();

        sincronizando = true;
        try {
            for (int i = 0; i < cantidad; i++) {
                for (int j = 0; j < cantidad; j++) {
                    campos.get(i).get(j).setText(String.valueOf(matriz[i][j]));
                }
            }
        } finally {
            sincronizando = false;
        }

        Grafo grafo = new Grafo();
        grafo.cargarMatriz(matriz);
        mostrarResultados(grafo);
    }

    private void mostrarResultados(Grafo grafo) {
        StringBuilder texto = new StringBuilder();

        for (Map.Entry<Integer, List<String>> entrada
                : grafo.listaAdyacencia().entrySet()) {
            texto.append("Nodo ")
                    .append(entrada.getKey())
                    .append(" -> ");

            if (entrada.getValue().isEmpty()) {
                texto.append("sin conexiones");
            } else {
                texto.append(String.join(", ", entrada.getValue()));
            }

            texto.append("\n");
        }

        txtLista.setText(texto.toString());

        if (!grafo.getNodos().isEmpty()) {
            lblBFS.setText("Anchura (BFS): " + grafo.recorridoAnchura(1));
            lblDFS.setText("Profundidad (DFS): " + grafo.recorridoProfundidad(1));
        } else {
            lblBFS.setText("Anchura (BFS): -");
            lblDFS.setText("Profundidad (DFS): -");
        }
    }

    // Cierra esta ventana y regresa a la anterior.
    @FXML
    private void volver(ActionEvent event) {
        Main.volver(((Node) event.getSource()).getScene().getWindow());
    }

    private void limpiarResultados() {
        txtLista.clear();
        lblBFS.setText("Anchura (BFS): -");
        lblDFS.setText("Profundidad (DFS): -");
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje == null ? "Ocurrió un error inesperado." : mensaje);
        alerta.showAndWait();
    }
}