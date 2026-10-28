package cr.ac.una.est.lab1est;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    private static final String RUTA = "/cr/ac/una/est/lab1est/";

    @Override
    public void start(Stage stage) throws IOException {

        URL fxml = Objects.requireNonNull(
                Main.class.getResource(RUTA + "menu.fxml"),
                "No se encontró menu.fxml"
        );

        FXMLLoader loader = new FXMLLoader(fxml);

        Scene scene = new Scene(loader.load());

        URL css = Objects.requireNonNull(
                Main.class.getResource(RUTA + "estilos.css"),
                "No se encontró estilos.css"
        );

        scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("Sistema de Grafos");
        stage.setScene(scene);

        stage.setMinWidth(700);
        stage.setMinHeight(550);

        // Si se cierra el menú principal, se cierra todo el programa.
        stage.setOnCloseRequest(event -> Platform.exit());

        stage.show();
    }

    // Cierra la ventana actual y regresa a la ventana desde la que se abrió.
    // La ventana anterior se guarda en el userData del Stage al abrirla.
    // Abre la ventana nueva y cierra (oculta) la actual.
    // La ventana actual se guarda en el userData para poder volver a ella.
    public static void cambiar(Window actual, Stage nueva) {

        nueva.setUserData(actual);

        // Si se cierra con la X, también regresa a la anterior.
        nueva.setOnCloseRequest(event -> {
            event.consume();
            volver(nueva);
        });

        nueva.show();   // primero se muestra la nueva...
        actual.hide();  // ...y luego se oculta la actual
    }

    // Cierra la ventana actual y regresa a la anterior.
    public static void volver(Window actual) {

        if (actual instanceof Stage stage
                && stage.getUserData() instanceof Stage anterior) {

            anterior.show();   // primero se muestra la anterior...
            anterior.toFront();
        }

        actual.hide();         // ...y luego se oculta la actual
    }

    public static void main(String[] args) {
        launch(args);
    }
}