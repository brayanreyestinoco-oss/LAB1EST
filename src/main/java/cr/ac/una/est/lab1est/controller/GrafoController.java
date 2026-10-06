package cr.ac.una.est.lab1est.controller;

import cr.ac.una.est.lab1est.model.Arista;
import cr.ac.una.est.lab1est.model.Grafo;
import cr.ac.una.est.lab1est.model.Nodo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoController {

    @FXML
    private Pane panelGrafo;

    @FXML
    private TextArea txtLista;

    @FXML
    private Label lblBFS;

    @FXML
    private Label lblDFS;

    @FXML
    private Label lblEstado;

    private final Grafo grafo = new Grafo();

    private Nodo nodoSeleccionado;

    private boolean modoAgregarNodo = false;

    private final Map<Nodo, Circle> circulos = new HashMap<>();

    private final Map<Nodo, Text> textos = new HashMap<>();

    private final Map<Arista, Line> lineas = new HashMap<>();

    private final Map<Arista, Text> textosPesos = new HashMap<>();


    // =========================================================
    // INICIALIZACIÓN
    // =========================================================

    @FXML
    private void initialize() {

        panelGrafo.setOnMouseClicked(event -> {

            if (!modoAgregarNodo) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            if (x < 35 || y < 35 ||
                    x > panelGrafo.getWidth() - 35 ||
                    y > panelGrafo.getHeight() - 35) {

                return;
            }

            agregarNodo(x, y);

            modoAgregarNodo = false;

            lblEstado.setText(
                    "Nodo agregado. Puede seleccionar dos nodos para conectarlos."
            );
        });
    }


    // =========================================================
    // AGREGAR NODO
    // =========================================================

    @FXML
    private void activarAgregarNodo() {

        modoAgregarNodo = true;

        lblEstado.setText(
                "Haga clic en el área blanca para colocar el nodo."
        );
    }

    private void agregarNodo(double x, double y) {

        Nodo nodo = grafo.agregarNodo(x, y);

        crearVisualNodo(nodo);

        actualizarResultados();
    }


    // =========================================================
    // CREAR VISUAL DEL NODO
    // =========================================================

    private void crearVisualNodo(Nodo nodo) {

        Circle circulo = new Circle(
                nodo.getX(),
                nodo.getY(),
                28
        );

        circulo.setFill(Color.WHITE);
        circulo.setStroke(Color.BLACK);
        circulo.setStrokeWidth(2);

        Text texto = new Text(
                nodo.getX() - 5,
                nodo.getY() + 6,
                String.valueOf(nodo.getNumero())
        );

        texto.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
        );

        circulos.put(nodo, circulo);
        textos.put(nodo, texto);

        panelGrafo.getChildren().addAll(
                circulo,
                texto
        );

        configurarNodo(
                nodo,
                circulo,
                texto
        );
    }


    // =========================================================
    // CONFIGURAR EVENTOS DEL NODO
    // =========================================================

    private void configurarNodo(
            Nodo nodo,
            Circle circulo,
            Text texto) {

        // Seleccionar nodo
        circulo.setOnMouseClicked(event -> {

            if (modoAgregarNodo) {
                event.consume();
                return;
            }

            seleccionarNodo(nodo);

            event.consume();
        });


        // Arrastrar nodo
        final double[] datos = new double[4];

        circulo.setOnMousePressed(event -> {

            datos[0] = event.getSceneX();
            datos[1] = event.getSceneY();

            datos[2] = nodo.getX();
            datos[3] = nodo.getY();

            event.consume();
        });


        circulo.setOnMouseDragged(event -> {

            double nuevoX =
                    datos[2]
                            + event.getSceneX()
                            - datos[0];

            double nuevoY =
                    datos[3]
                            + event.getSceneY()
                            - datos[1];


            if (nuevoX < 30) {
                nuevoX = 30;
            }

            if (nuevoY < 30) {
                nuevoY = 30;
            }

            if (panelGrafo.getWidth() > 0 &&
                    nuevoX > panelGrafo.getWidth() - 30) {

                nuevoX = panelGrafo.getWidth() - 30;
            }

            if (panelGrafo.getHeight() > 0 &&
                    nuevoY > panelGrafo.getHeight() - 30) {

                nuevoY = panelGrafo.getHeight() - 30;
            }


            nodo.setX(nuevoX);
            nodo.setY(nuevoY);

            circulo.setCenterX(nuevoX);
            circulo.setCenterY(nuevoY);

            texto.setX(nuevoX - 5);
            texto.setY(nuevoY + 6);

            actualizarAristasVisuales();

            event.consume();
        });
    }


    // =========================================================
    // SELECCIONAR NODOS
    // =========================================================

    private void seleccionarNodo(Nodo nodo) {

        if (nodoSeleccionado == null) {

            nodoSeleccionado = nodo;

            circulos.get(nodo).setFill(
                    Color.LIGHTBLUE
            );

            lblEstado.setText(
                    "Nodo "
                            + nodo.getNumero()
                            + " seleccionado. "
                            + "Seleccione otro nodo."
            );

            return;
        }


        if (nodoSeleccionado == nodo) {

            circulos.get(nodo).setFill(
                    Color.WHITE
            );

            nodoSeleccionado = null;

            lblEstado.setText(
                    "Selección cancelada."
            );

            return;
        }


        pedirPeso(
                nodoSeleccionado,
                nodo
        );
    }


    // =========================================================
    // PEDIR PESO
    // =========================================================

    private void pedirPeso(
            Nodo nodo1,
            Nodo nodo2) {

        TextInputDialog dialog =
                new TextInputDialog("1");

        dialog.setTitle(
                "Peso de la arista"
        );

        dialog.setHeaderText(
                "Conectar nodo "
                        + nodo1.getNumero()
                        + " con nodo "
                        + nodo2.getNumero()
        );

        dialog.setContentText(
                "Ingrese el peso:"
        );


        dialog.showAndWait().ifPresent(respuesta -> {

            try {

                int peso =
                        Integer.parseInt(
                                respuesta.trim()
                        );


                if (peso <= 0) {

                    mostrarError(
                            "El peso debe ser mayor que 0."
                    );

                    return;
                }


                boolean conectado =
                        grafo.conectar(
                                nodo1,
                                nodo2,
                                peso
                        );


                if (!conectado) {

                    mostrarError(
                            "Esos nodos ya están conectados."
                    );

                    return;
                }


                Arista arista =
                        grafo.getAristas().get(
                                grafo.getAristas().size() - 1
                        );


                crearVisualArista(arista);


                lblEstado.setText(
                        "Arista creada con peso "
                                + peso
                                + "."
                );


                actualizarResultados();

            } catch (NumberFormatException e) {

                mostrarError(
                        "Ingrese un peso entero válido."
                );
            }
        });


        restaurarSeleccion();
    }


    // =========================================================
    // CREAR VISUAL DE ARISTA
    // =========================================================

    private void crearVisualArista(Arista arista) {

        Line linea = new Line(
                arista.getNodo1().getX(),
                arista.getNodo1().getY(),
                arista.getNodo2().getX(),
                arista.getNodo2().getY()
        );

        linea.setStrokeWidth(2);


        Text peso = new Text(
                calcularXMedio(arista),
                calcularYMedio(arista),
                String.valueOf(
                        arista.getPeso()
                )
        );

        peso.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );


        lineas.put(
                arista,
                linea
        );

        textosPesos.put(
                arista,
                peso
        );


        // Las líneas van detrás de los nodos
        panelGrafo.getChildren().add(
                0,
                linea
        );

        // El peso va encima de la línea
        panelGrafo.getChildren().add(
                peso
        );
    }


    private double calcularXMedio(Arista arista) {

        return (
                arista.getNodo1().getX()
                        +
                        arista.getNodo2().getX()
        ) / 2;
    }


    private double calcularYMedio(Arista arista) {

        return (
                arista.getNodo1().getY()
                        +
                        arista.getNodo2().getY()
        ) / 2;
    }


    // =========================================================
    // ACTUALIZAR ARISTAS CUANDO SE MUEVE UN NODO
    // =========================================================

    private void actualizarAristasVisuales() {

        for (Arista arista : grafo.getAristas()) {

            Line linea = lineas.get(arista);

            Text peso = textosPesos.get(arista);


            if (linea != null) {

                linea.setStartX(
                        arista.getNodo1().getX()
                );

                linea.setStartY(
                        arista.getNodo1().getY()
                );

                linea.setEndX(
                        arista.getNodo2().getX()
                );

                linea.setEndY(
                        arista.getNodo2().getY()
                );
            }


            if (peso != null) {

                peso.setX(
                        calcularXMedio(arista)
                );

                peso.setY(
                        calcularYMedio(arista)
                );
            }
        }
    }


    // =========================================================
    // RESTAURAR SELECCIÓN
    // =========================================================

    private void restaurarSeleccion() {

        if (nodoSeleccionado != null) {

            Circle circulo =
                    circulos.get(nodoSeleccionado);

            if (circulo != null) {

                circulo.setFill(
                        Color.WHITE
                );
            }
        }

        nodoSeleccionado = null;
    }


    // =========================================================
    // LIMPIAR GRAFO
    // =========================================================

    @FXML
    private void limpiarGrafo() {

        grafo.limpiar();

        panelGrafo.getChildren().clear();

        circulos.clear();
        textos.clear();
        lineas.clear();
        textosPesos.clear();

        nodoSeleccionado = null;

        modoAgregarNodo = false;


        lblEstado.setText(
                "Grafo limpio."
        );

        txtLista.clear();

        lblBFS.setText(
                "Anchura (BFS): -"
        );

        lblDFS.setText(
                "Profundidad (DFS): -"
        );
    }


    // =========================================================
    // GENERAR MATRIZ
    // =========================================================

    @FXML
    private void generarMatriz() throws Exception {

        if (grafo.getNodos().isEmpty()) {

            mostrarError(
                    "El grafo no tiene nodos."
            );

            return;
        }


        int[][] matriz =
                grafo.generarMatriz();


        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/cr/ac/una/est/lab1est/matriz.fxml"
                        )
                );


        Scene scene =
                new Scene(
                        loader.load()
                );


        MatrizController controller =
                loader.getController();


        controller.cargarMatriz(
                matriz
        );


        scene.getStylesheets().add(
                getClass().getResource(
                        "/cr/ac/una/est/lab1est/estilos.css"
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


    // =========================================================
    // ACTUALIZAR RESULTADOS
    // =========================================================

    private void actualizarResultados() {

        StringBuilder texto =
                new StringBuilder();


        for (Map.Entry<Integer, List<String>> entrada :
                grafo.listaAdyacencia().entrySet()) {

            texto.append(
                    "Nodo "
            );

            texto.append(
                    entrada.getKey()
            );

            texto.append(
                    " -> "
            );


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
    // CARGAR MATRIZ
    // =========================================================

    public void cargarMatriz(int[][] matriz) {

        limpiarGrafo();


        grafo.cargarMatriz(
                matriz
        );


        colocarNodosAutomaticamente();


        /*
         * Primero creamos los nodos.
         * Después creamos las aristas.
         * Así las líneas quedan correctamente detrás
         * de los nodos.
         */

        for (Nodo nodo : grafo.getNodos()) {

            crearVisualNodo(nodo);
        }


        for (Arista arista : grafo.getAristas()) {

            crearVisualArista(arista);
        }


        actualizarResultados();
    }


    // =========================================================
    // COLOCAR NODOS AUTOMÁTICAMENTE
    // =========================================================

    private void colocarNodosAutomaticamente() {

        int cantidad =
                grafo.getNodos().size();


        if (cantidad == 0) {
            return;
        }


        double centroX = 390;
        double centroY = 280;
        double radio = 200;


        for (int i = 0; i < cantidad; i++) {

            Nodo nodo =
                    grafo.getNodos().get(i);


            double angulo =
                    2 * Math.PI * i / cantidad;


            double x =
                    centroX
                            + radio
                            * Math.cos(angulo);


            double y =
                    centroY
                            + radio
                            * Math.sin(angulo);


            nodo.setX(x);
            nodo.setY(y);
        }
    }


    // =========================================================
    // MOSTRAR ERROR
    // =========================================================

    private void mostrarError(String mensaje) {

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