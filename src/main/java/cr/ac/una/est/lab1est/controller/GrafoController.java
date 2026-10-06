package cr.ac.una.est.lab1est.controller;

import cr.ac.una.est.lab1est.Grafo;
import cr.ac.una.est.lab1est.view.GrafoView;
import cr.ac.una.est.lab1est.view.MatrizView;
import cr.ac.una.est.lab1est.view.MenuView;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class GrafoController {

    private final Stage stage;

    private final Grafo grafo;

    private final MenuView menuView;
    private final MatrizView matrizView;
    private final GrafoView grafoView;

    public GrafoController(
            Stage stage
    ) {

        this.stage = stage;

        grafo = new Grafo();

        menuView =
                new MenuView();

        matrizView =
                new MatrizView();

        grafoView =
                new GrafoView(grafo);

        configurarEventos();

        mostrarMenu();
    }

    // =====================================================
    // EVENTOS
    // =====================================================

    private void configurarEventos() {

        // MENU

        menuView
                .getBtnMatriz()
                .setOnAction(
                        event -> mostrarMatriz()
                );

        menuView
                .getBtnGrafo()
                .setOnAction(
                        event -> mostrarGrafo()
                );

        // =================================================
        // GENERAR MATRIZ
        // =================================================

        matrizView
                .getBtnGenerar()
                .setOnAction(
                        event -> {

                            try {

                                int cantidad =
                                        Integer.parseInt(
                                                matrizView
                                                        .getTxtNodos()
                                                        .getText()
                                                        .trim()
                                        );

                                if (
                                        cantidad < 1
                                                ||
                                                cantidad > 15
                                ) {

                                    mostrarError(
                                            "Ingrese una cantidad entre 1 y 15."
                                    );

                                    return;
                                }

                                matrizView
                                        .crearMatriz(
                                                cantidad
                                        );

                                matrizView
                                        .getTxtResultado()
                                        .clear();

                            } catch (
                                    NumberFormatException e
                            ) {

                                mostrarError(
                                        "Ingrese una cantidad válida."
                                );
                            }
                        }
                );

        // =================================================
        // MATRIZ -> GRAFO
        // =================================================

        matrizView
                .getBtnDibujar()
                .setOnAction(
                        event -> {

                            int[][] matriz =
                                    matrizView
                                            .obtenerMatriz();

                            if (matriz == null) {

                                mostrarError(
                                        "La matriz contiene valores inválidos."
                                );

                                return;
                            }

                            grafoView
                                    .cargarDesdeMatriz(
                                            matriz
                                    );

                            mostrarGrafo();
                        }
                );

        // =================================================
        // LISTA DE ADYACENCIA DESDE MATRIZ
        // =================================================

        matrizView
                .getBtnLista()
                .setOnAction(
                        event -> {

                            int[][] matriz =
                                    matrizView
                                            .obtenerMatriz();

                            if (matriz == null) {

                                mostrarError(
                                        "Primero genere una matriz."
                                );

                                return;
                            }

                            grafoView
                                    .cargarDesdeMatriz(
                                            matriz
                                    );

                            matrizView
                                    .getTxtResultado()
                                    .setText(
                                            "LISTA DE ADYACENCIA\n\n"
                                                    +
                                                    grafo
                                                            .generarListaAdyacencia()
                                    );
                        }
                );

        // =================================================
        // BFS DESDE MATRIZ
        // =================================================

        matrizView
                .getBtnBFS()
                .setOnAction(
                        event -> {

                            int[][] matriz =
                                    matrizView
                                            .obtenerMatriz();

                            if (matriz == null) {

                                mostrarError(
                                        "Primero genere una matriz."
                                );

                                return;
                            }

                            int inicio =
                                    obtenerNodoInicial(
                                            matrizView
                                                    .getTxtNodoInicio()
                                                    .getText(),
                                            matriz.length
                                    );

                            if (inicio == -1) {
                                return;
                            }

                            grafoView
                                    .cargarDesdeMatriz(
                                            matriz
                                    );

                            matrizView
                                    .getTxtResultado()
                                    .setText(
                                            "RECORRIDO POR ANCHURA (BFS)\n\n"
                                                    +
                                                    grafo
                                                            .recorridoAnchura(
                                                                    inicio
                                                            )
                                    );
                        }
                );

        // =================================================
        // DFS DESDE MATRIZ
        // =================================================

        matrizView
                .getBtnDFS()
                .setOnAction(
                        event -> {

                            int[][] matriz =
                                    matrizView
                                            .obtenerMatriz();

                            if (matriz == null) {

                                mostrarError(
                                        "Primero genere una matriz."
                                );

                                return;
                            }

                            int inicio =
                                    obtenerNodoInicial(
                                            matrizView
                                                    .getTxtNodoInicio()
                                                    .getText(),
                                            matriz.length
                                    );

                            if (inicio == -1) {
                                return;
                            }

                            grafoView
                                    .cargarDesdeMatriz(
                                            matriz
                                    );

                            matrizView
                                    .getTxtResultado()
                                    .setText(
                                            "RECORRIDO POR PROFUNDIDAD (DFS)\n\n"
                                                    +
                                                    grafo
                                                            .recorridoProfundidad(
                                                                    inicio
                                                            )
                                    );
                        }
                );

        matrizView
                .getBtnRegresar()
                .setOnAction(
                        event -> mostrarMenu()
                );

        // =================================================
        // GRAFO -> MATRIZ
        // =================================================

        grafoView
                .getBtnGenerarMatriz()
                .setOnAction(
                        event -> {

                            if (
                                    grafo
                                            .getNodos()
                                            .isEmpty()
                            ) {

                                mostrarError(
                                        "Primero agregue nodos."
                                );

                                return;
                            }

                            int[][] matriz =
                                    grafo
                                            .generarMatriz();

                            matrizView
                                    .mostrarMatriz(
                                            matriz
                                    );

                            matrizView
                                    .getTxtNodos()
                                    .setText(
                                            String.valueOf(
                                                    matriz.length
                                            )
                                    );

                            mostrarMatriz();
                        }
                );

        // =================================================
        // LISTA DESDE GRAFO
        // =================================================

        grafoView
                .getBtnLista()
                .setOnAction(
                        event -> {

                            if (
                                    grafo
                                            .getNodos()
                                            .isEmpty()
                            ) {

                                mostrarError(
                                        "Primero agregue nodos."
                                );

                                return;
                            }

                            grafoView
                                    .getTxtResultado()
                                    .setText(
                                            "LISTA DE ADYACENCIA\n\n"
                                                    +
                                                    grafo
                                                            .generarListaAdyacencia()
                                    );
                        }
                );

        // =================================================
        // BFS DESDE GRAFO
        // =================================================

        grafoView
                .getBtnBFS()
                .setOnAction(
                        event -> {

                            int inicio =
                                    obtenerNodoInicial(
                                            grafoView
                                                    .getTxtNodoInicio()
                                                    .getText(),
                                            grafo
                                                    .getNodos()
                                                    .size()
                                    );

                            if (inicio == -1) {
                                return;
                            }

                            grafoView
                                    .getTxtResultado()
                                    .setText(
                                            "RECORRIDO POR ANCHURA (BFS)\n\n"
                                                    +
                                                    grafo
                                                            .recorridoAnchura(
                                                                    inicio
                                                            )
                                    );
                        }
                );

        // =================================================
        // DFS DESDE GRAFO
        // =================================================

        grafoView
                .getBtnDFS()
                .setOnAction(
                        event -> {

                            int inicio =
                                    obtenerNodoInicial(
                                            grafoView
                                                    .getTxtNodoInicio()
                                                    .getText(),
                                            grafo
                                                    .getNodos()
                                                    .size()
                                    );

                            if (inicio == -1) {
                                return;
                            }

                            grafoView
                                    .getTxtResultado()
                                    .setText(
                                            "RECORRIDO POR PROFUNDIDAD (DFS)\n\n"
                                                    +
                                                    grafo
                                                            .recorridoProfundidad(
                                                                    inicio
                                                            )
                                    );
                        }
                );

        // =================================================
        // LIMPIAR
        // =================================================

        grafoView
                .getBtnLimpiar()
                .setOnAction(
                        event -> grafoView.limpiar()
                );

        // =================================================
        // REGRESAR
        // =================================================

        grafoView
                .getBtnRegresar()
                .setOnAction(
                        event -> mostrarMenu()
                );
    }

    // =====================================================
    // VALIDAR NODO INICIAL
    // =====================================================

    private int obtenerNodoInicial(
            String texto,
            int cantidadNodos
    ) {

        try {

            int nodo =
                    Integer.parseInt(
                            texto.trim()
                    );

            if (
                    nodo < 1
                            ||
                            nodo > cantidadNodos
            ) {

                mostrarError(
                        "El nodo inicial no existe."
                );

                return -1;
            }

            return nodo;

        } catch (
                NumberFormatException e
        ) {

            mostrarError(
                    "Ingrese un nodo inicial válido."
            );

            return -1;
        }
    }

    // =====================================================
    // MOSTRAR MENU
    // =====================================================

    private void mostrarMenu() {

        Scene scene =
                new Scene(
                        menuView,
                        800,
                        600
                );

        aplicarCSS(scene);

        stage.setScene(scene);

        stage.setTitle(
                "Proyecto de Grafos"
        );

        stage.show();
    }

    // =====================================================
    // MOSTRAR MATRIZ
    // =====================================================

    private void mostrarMatriz() {

        Scene scene =
                new Scene(
                        matrizView,
                        900,
                        700
                );

        aplicarCSS(scene);

        stage.setScene(scene);

        stage.setTitle(
                "Matriz de Adyacencia"
        );

        stage.show();
    }

    // =====================================================
    // MOSTRAR GRAFO
    // =====================================================

    private void mostrarGrafo() {

        Scene scene =
                new Scene(
                        grafoView,
                        1050,
                        700
                );

        aplicarCSS(scene);

        stage.setScene(scene);

        stage.setTitle(
                "Grafo"
        );

        stage.show();
    }

    // =====================================================
    // CSS
    // =====================================================

    private void aplicarCSS(
            Scene scene
    ) {

        var recurso =
                getClass()
                        .getResource(
                                "/css/estilos.css"
                        );

        if (recurso != null) {

            scene.getStylesheets()
                    .add(
                            recurso.toExternalForm()
                    );
        }
    }

    // =====================================================
    // ERROR
    // =====================================================

    private void mostrarError(
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR,
                        mensaje,
                        ButtonType.OK
                );

        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(
                null
        );

        alerta.showAndWait();
    }
}