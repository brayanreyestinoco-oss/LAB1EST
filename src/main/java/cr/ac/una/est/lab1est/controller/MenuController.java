package cr.ac.una.est.lab1est.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cr.ac.una.est.lab1est.Main;
import java.io.IOException;
import java.net.URL;

public class MenuController {

    private static final String RUTA = "/cr/ac/una/est/lab1est/";

    // =========================================================
    // ABRIR MATRIZ
    // =========================================================

    @FXML
    private void abrirMatriz(ActionEvent event) throws IOException {
        abrirVentana(event, "matriz.fxml", "Matriz de Adyacencia", 1050, 700);
    }

    // =========================================================
    // ABRIR GRAFO
    // =========================================================

    @FXML
    private void abrirGrafo(ActionEvent event) throws IOException {
        abrirVentana(event, "grafo.fxml", "Editor de Grafos", 1150, 750);
    }

    // =========================================================
    // CERRAR PROGRAMA
    // =========================================================

    @FXML
    private void cerrarPrograma() {
        Platform.exit();
    }

    // =========================================================
    // ABRIR VENTANA (código común de las dos opciones)
    // =========================================================

    private void abrirVentana(
            ActionEvent event,
            String archivo,
            String titulo,
            double anchoMinimo,
            double altoMinimo) throws IOException {

        URL fxml = getClass().getResource(RUTA + archivo);

        if (fxml == null) {
            throw new IllegalStateException(
                    "No se encontró " + archivo + " en: "
                            + "src/main/resources" + RUTA
            );
        }

        FXMLLoader loader = new FXMLLoader(fxml);

        Scene scene = new Scene(loader.load());

        URL css = getClass().getResource(RUTA + "estilos.css");

        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        Stage stage = new Stage();

        // Guarda la ventana actual para poder regresar a ella con "Volver".
        stage.setTitle(titulo);
        stage.setScene(scene);

        stage.setMinWidth(anchoMinimo);
        stage.setMinHeight(altoMinimo);

        Main.cambiar(((Node) event.getSource()).getScene().getWindow(), stage);
    }
}