package cr.ac.una.est.lab1est.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private void abrirMatriz() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/cr.ac.una.est.lab1est/matriz.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource(
                        "/cr.ac.una.est.lab1est/estilos.css"
                ).toExternalForm()
        );

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

    @FXML
    private void abrirGrafo() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/cr.ac.una.est.lab1est/grafo.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource(
                        "/cr.ac.una.est.lab1est/estilos.css"
                ).toExternalForm()
        );

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