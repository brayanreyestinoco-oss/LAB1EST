package cr.ac.una.est.lab1est.model;

import java.util.*;

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

    public Nodo agregarNodo(double x, double y) {

        Nodo nodo = new Nodo(
                nodos.size() + 1,
                x,
                y
        );

        nodos.add(nodo);

        return nodo;
    }

    public boolean conectar(
            Nodo nodo1,
            Nodo nodo2,
            int peso) {

        if (nodo1 == null || nodo2 == null) {
            return false;
        }

        if (nodo1 == nodo2) {
            return false;
        }

        if (peso <= 0) {
            return false;
        }

        if (existeConexion(nodo1, nodo2)) {
            return false;
        }

        aristas.add(
                new Arista(
                        nodo1,
                        nodo2,
                        peso
                )
        );

        return true;
    }

    public boolean existeConexion(
            Nodo nodo1,
            Nodo nodo2) {

        for (Arista arista : aristas) {

            if ((arista.getNodo1() == nodo1
                    && arista.getNodo2() == nodo2)
                    ||
                    (arista.getNodo1() == nodo2
                            && arista.getNodo2() == nodo1)) {

                return true;
            }
        }

        return false;
    }

    public int[][] generarMatriz() {

        int cantidad = nodos.size();

        int[][] matriz =
                new int[cantidad][cantidad];

        for (Arista arista : aristas) {

            int i =
                    arista.getNodo1().getNumero() - 1;

            int j =
                    arista.getNodo2().getNumero() - 1;

            matriz[i][j] =
                    arista.getPeso();

            matriz[j][i] =
                    arista.getPeso();
        }

        return matriz;
    }

    public void cargarMatriz(int[][] matriz) {

        limpiar();

        int cantidad = matriz.length;

        for (int i = 0; i < cantidad; i++) {

            agregarNodo(0, 0);
        }

        for (int i = 0; i < cantidad; i++) {

            for (int j = i + 1;
                 j < cantidad;
                 j++) {

                if (matriz[i][j] > 0) {

                    conectar(
                            nodos.get(i),
                            nodos.get(j),
                            matriz[i][j]
                    );
                }
            }
        }
    }

    public Map<Integer, List<String>>
    listaAdyacencia() {

        Map<Integer, List<String>> lista =
                new LinkedHashMap<>();

        for (Nodo nodo : nodos) {

            lista.put(
                    nodo.getNumero(),
                    new ArrayList<>()
            );
        }

        for (Arista arista : aristas) {

            int a =
                    arista.getNodo1().getNumero();

            int b =
                    arista.getNodo2().getNumero();

            int peso =
                    arista.getPeso();

            lista.get(a).add(
                    b + "(" + peso + ")"
            );

            lista.get(b).add(
                    a + "(" + peso + ")"
            );
        }

        return lista;
    }

    public List<Integer> recorridoAnchura(
            int inicio) {

        List<Integer> recorrido =
                new ArrayList<>();

        if (nodos.isEmpty()) {
            return recorrido;
        }

        Map<Integer, List<Integer>> lista =
                obtenerVecinos();

        if (!lista.containsKey(inicio)) {
            return recorrido;
        }

        Set<Integer> visitados =
                new HashSet<>();

        Queue<Integer> cola =
                new LinkedList<>();

        cola.add(inicio);
        visitados.add(inicio);

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            recorrido.add(actual);

            for (int vecino :
                    lista.get(actual)) {

                if (!visitados.contains(vecino)) {

                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        return recorrido;
    }

    public List<Integer> recorridoProfundidad(
            int inicio) {

        List<Integer> recorrido =
                new ArrayList<>();

        if (nodos.isEmpty()) {
            return recorrido;
        }

        Map<Integer, List<Integer>> lista =
                obtenerVecinos();

        if (!lista.containsKey(inicio)) {
            return recorrido;
        }

        Set<Integer> visitados =
                new HashSet<>();

        dfs(
                inicio,
                lista,
                visitados,
                recorrido
        );

        return recorrido;
    }

    private void dfs(
            int actual,
            Map<Integer, List<Integer>> lista,
            Set<Integer> visitados,
            List<Integer> recorrido) {

        visitados.add(actual);

        recorrido.add(actual);

        for (int vecino :
                lista.get(actual)) {

            if (!visitados.contains(vecino)) {

                dfs(
                        vecino,
                        lista,
                        visitados,
                        recorrido
                );
            }
        }
    }

    private Map<Integer, List<Integer>>
    obtenerVecinos() {

        Map<Integer, List<Integer>> lista =
                new LinkedHashMap<>();

        for (Nodo nodo : nodos) {

            lista.put(
                    nodo.getNumero(),
                    new ArrayList<>()
            );
        }

        for (Arista arista : aristas) {

            int a =
                    arista.getNodo1().getNumero();

            int b =
                    arista.getNodo2().getNumero();

            lista.get(a).add(b);
            lista.get(b).add(a);
        }

        return lista;
    }

    public void limpiar() {

        nodos.clear();
        aristas.clear();
    }
}