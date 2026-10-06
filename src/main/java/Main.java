package cr.ac.una.est.lab1est;

import cr.ac.una.est.lab1est.controller.GrafoController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        new GrafoController(stage);

    }

    public static void main(String[] args) {

        launch(args);

    }
}