package cr.ac.una.est.lab1est;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource(
                        "/cr/ac/una/est/lab1est/menu.fxml"
                )
        );

        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                Main.class.getResource(
                        "/cr/ac/una/est/lab1est/estilos.css"
                ).toExternalForm()
        );

        stage.setTitle("Sistema de Grafos");
        stage.setScene(scene);

        stage.setMinWidth(700);
        stage.setMinHeight(550);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}