package cr.ac.una.est.lab1est.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grafo {

    private final List<Nodo> nodos;
    private final List<Arista> aristas;

    public Grafo() {

        nodos = new ArrayList<>();
        aristas = new ArrayList<>();

    }

    public List<Nodo> getNodos() {
        return nodos;
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    // =====================================================
    // AGREGAR NODO
    // =====================================================

    public Nodo agregarNodo(
            double x,
            double y
    ) {

        int numero =
                nodos.size() + 1;

        Nodo nodo =
                new Nodo(
                        numero,
                        x,
                        y
                );

        nodos.add(nodo);

        return nodo;
    }

    // =====================================================
    // CONECTAR NODOS
    // =====================================================

    public boolean conectar(
            Nodo nodo1,
            Nodo nodo2
    ) {

        if (nodo1 == null || nodo2 == null) {
            return false;
        }

        if (nodo1 == nodo2) {
            return false;
        }

        if (existeConexion(nodo1, nodo2)) {
            return false;
        }

        Arista arista =
                new Arista(
                        nodo1,
                        nodo2
                );

        aristas.add(arista);

        return true;
    }

    // =====================================================
    // VERIFICAR CONEXIÓN
    // =====================================================

    public boolean existeConexion(
            Nodo nodo1,
            Nodo nodo2
    ) {

        for (Arista arista : aristas) {

            if (
                    (
                            arista.getNodo1() == nodo1
                                    &&
                                    arista.getNodo2() == nodo2
                    )
                            ||
                            (
                                    arista.getNodo1() == nodo2
                                            &&
                                            arista.getNodo2() == nodo1
                            )
            ) {

                return true;
            }
        }

        return false;
    }

    // =====================================================
    // MATRIZ DE ADYACENCIA
    // =====================================================

    public int[][] generarMatriz() {

        int cantidad =
                nodos.size();

        int[][] matriz =
                new int[cantidad][cantidad];

        for (Arista arista : aristas) {

            int i =
                    arista
                            .getNodo1()
                            .getNumero() - 1;

            int j =
                    arista
                            .getNodo2()
                            .getNumero() - 1;

            matriz[i][j] = 1;
            matriz[j][i] = 1;
        }

        return matriz;
    }

    // =====================================================
    // LISTA DE ADYACENCIA
    // =====================================================

    public String generarListaAdyacencia() {

        if (nodos.isEmpty()) {

            return "No hay nodos en el grafo.";
        }

        StringBuilder resultado =
                new StringBuilder();

        for (Nodo nodo : nodos) {

            resultado
                    .append("Nodo ")
                    .append(nodo.getNumero())
                    .append(": ");

            List<Integer> vecinos =
                    obtenerVecinos(nodo);

            if (vecinos.isEmpty()) {

                resultado.append(
                        "sin conexiones"
                );

            } else {

                for (
                        int i = 0;
                        i < vecinos.size();
                        i++
                ) {

                    resultado.append(
                            vecinos.get(i)
                    );

                    if (
                            i <
                                    vecinos.size() - 1
                    ) {

                        resultado.append(
                                " → "
                        );
                    }
                }
            }

            resultado.append("\n");
        }

        return resultado.toString();
    }

    private List<Integer> obtenerVecinos(
            Nodo nodo
    ) {

        List<Integer> vecinos =
                new ArrayList<>();

        for (Arista arista : aristas) {

            if (
                    arista.getNodo1()
                            == nodo
            ) {

                vecinos.add(
                        arista
                                .getNodo2()
                                .getNumero()
                );

            } else if (
                    arista.getNodo2()
                            == nodo
            ) {

                vecinos.add(
                        arista
                                .getNodo1()
                                .getNumero()
                );
            }
        }

        Collections.sort(vecinos);

        return vecinos;
    }

    // =====================================================
    // RECORRIDO POR ANCHURA - BFS
    // =====================================================

    public String recorridoAnchura(
            int nodoInicial
    ) {

        if (nodos.isEmpty()) {

            return "No hay nodos.";
        }

        if (
                nodoInicial < 1
                        ||
                        nodoInicial > nodos.size()
        ) {

            return "Nodo inicial inválido.";
        }

        boolean[] visitado =
                new boolean[nodos.size()];

        Queue<Integer> cola =
                new LinkedList<>();

        List<Integer> recorrido =
                new ArrayList<>();

        cola.add(nodoInicial);

        visitado[nodoInicial - 1] =
                true;

        while (!cola.isEmpty()) {

            int actual =
                    cola.poll();

            recorrido.add(actual);

            List<Integer> vecinos =
                    obtenerVecinos(
                            nodos.get(actual - 1)
                    );

            for (int vecino : vecinos) {

                if (
                        !visitado[vecino - 1]
                ) {

                    visitado[vecino - 1] =
                            true;

                    cola.add(vecino);
                }
            }
        }

        return convertirRecorrido(
                recorrido
        );
    }

    // =====================================================
    // RECORRIDO POR PROFUNDIDAD - DFS
    // =====================================================

    public String recorridoProfundidad(
            int nodoInicial
    ) {

        if (nodos.isEmpty()) {

            return "No hay nodos.";
        }

        if (
                nodoInicial < 1
                        ||
                        nodoInicial > nodos.size()
        ) {

            return "Nodo inicial inválido.";
        }

        boolean[] visitado =
                new boolean[nodos.size()];

        List<Integer> recorrido =
                new ArrayList<>();

        dfs(
                nodoInicial,
                visitado,
                recorrido
        );

        return convertirRecorrido(
                recorrido
        );
    }

    private void dfs(
            int actual,
            boolean[] visitado,
            List<Integer> recorrido
    ) {

        visitado[actual - 1] = true;

        recorrido.add(actual);

        List<Integer> vecinos =
                obtenerVecinos(
                        nodos.get(actual - 1)
                );

        for (int vecino : vecinos) {

            if (
                    !visitado[vecino - 1]
            ) {

                dfs(
                        vecino,
                        visitado,
                        recorrido
                );
            }
        }
    }

    private String convertirRecorrido(
            List<Integer> recorrido
    ) {

        StringBuilder resultado =
                new StringBuilder();

        for (
                int i = 0;
                i < recorrido.size();
                i++
        ) {

            resultado.append(
                    recorrido.get(i)
            );

            if (
                    i <
                            recorrido.size() - 1
            ) {

                resultado.append(
                        " → "
                );
            }
        }

        return resultado.toString();
    }

    // =====================================================
    // LIMPIAR
    // =====================================================

    public void limpiar() {

        nodos.clear();
        aristas.clear();
    }
}