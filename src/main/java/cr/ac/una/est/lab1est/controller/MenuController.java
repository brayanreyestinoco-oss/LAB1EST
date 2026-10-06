package cr.ac.una.est.lab1est.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MenuController {

    // =========================================================
    // ABRIR MATRIZ
    // =========================================================

    @FXML
    private void abrirMatriz() throws Exception {

        URL fxml =
                getClass().getResource(
                        "/cr/ac/una/est/lab1est/matriz.fxml"
                );

        if (fxml == null) {

            throw new IllegalStateException(
                    "No se encontró matriz.fxml en: " +
                            "src/main/resources/cr/ac/una/est/lab1est/"
            );
        }


        FXMLLoader loader =
                new FXMLLoader(fxml);


        Scene scene =
                new Scene(
                        loader.load()
                );


        URL css =
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
                "Matriz de Adyacencia"
        );

        stage.setScene(scene);

        stage.setMinWidth(1050);
        stage.setMinHeight(700);

        stage.show();
    }


    // =========================================================
    // ABRIR GRAFO
    // =========================================================

    @FXML
    private void abrirGrafo() throws Exception {

        URL fxml =
                getClass().getResource(
                        "/cr/ac/una/est/lab1est/grafo.fxml"
                );

        if (fxml == null) {

            throw new IllegalStateException(
                    "No se encontró grafo.fxml en: " +
                            "src/main/resources/cr/ac/una/est/lab1est/"
            );
        }


        FXMLLoader loader =
                new FXMLLoader(fxml);


        Scene scene =
                new Scene(
                        loader.load()
                );


        URL css =
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
                "Editor de Grafos"
        );

        stage.setScene(scene);

        stage.setMinWidth(1150);
        stage.setMinHeight(750);

        stage.show();
    }
}