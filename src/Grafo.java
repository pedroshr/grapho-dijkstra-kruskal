import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

//Feito por : Bernardo Ferrão, Mathias Milbrath e Pedro Rosa
public class Grafo {
    private int[][] matrizAdjacencia;
    private int numVertices;
    public void kruskal() {
        List<Aresta> arestas = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (matrizAdjacencia[i][j] != 0) {
                    arestas.add(new Aresta(i, j, matrizAdjacencia[i][j]));
                }
            }
        }

        Collections.sort(arestas);

        int[] pai = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            pai[i] = i;
        }

        List<Aresta> mst = new ArrayList<>();

        for (Aresta aresta : arestas) {
            int paiOrigem = encontraPai(aresta.origem, pai);
            int paiDestino = encontraPai(aresta.destino, pai);
            if (paiOrigem != paiDestino) {
                mst.add(aresta);
                pai[paiOrigem] = paiDestino;
            }
        }

        System.out.println("Árvore Geradora Mínima (Kruskal):");
        for (Aresta aresta : mst) {
            System.out.printf("(%d, %d) = %d\n", aresta.origem, aresta.destino, aresta.peso);
        }
    }

    private int encontraPai(int vertice, int[] pai) {
        while (pai[vertice] != vertice) {
            vertice = pai[vertice];
        }
        return vertice;
    }

    private static class Aresta implements Comparable<Aresta> {
        private int origem;
        private int destino;
        private int peso;

        public Aresta(int origem, int destino, int peso) {
            this.origem = origem;
            this.destino = destino;
            this.peso = peso;
        }

        public int compareTo(Aresta outra) {
            return Integer.compare(this.peso, outra.peso);
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void dijkstra(int source, int dest) {
        int[] distancia = new int[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distancia[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        distancia[source] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = distanciaMinima(distancia, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && matrizAdjacencia[u][v] != 0 && distancia[u] != Integer.MAX_VALUE
                        && distancia[u] + matrizAdjacencia[u][v] < distancia[v]) {
                    distancia[v] = distancia[u] + matrizAdjacencia[u][v];
                }
            }
        }

        imprimeCaminho(distancia, source, dest);
    }

    public int distanciaMinima(int[] distancia, boolean[] visitado) {
        int minDist = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < numVertices; i++) {
            if (!visitado[i] && distancia[i] <= minDist) {
                minDist = distancia[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public void imprimeCaminho(int[] distancia, int origem, int destino) {
        System.out.println("Caminho mais curto de " + origem + " a " + destino + ":");
        if (distancia[destino] == Integer.MAX_VALUE) {
            System.out.println("Não há caminho possível.");
            return;
        }
        System.out.print(destino);
        int anterior = destino;
        while (anterior != origem) {
            for (int i = 0; i < numVertices; i++) {
                if (matrizAdjacencia[anterior][i] != 0 && distancia[anterior] - matrizAdjacencia[anterior][i] == distancia[i]) {
                    System.out.print(" <- " + i);
                    anterior = i;
                }
            }
        }
        System.out.println("\nDistância total: " + distancia[destino]);
    }

    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        this.matrizAdjacencia = new int[numVertices][numVertices];
    }

    public void adicionarAresta() {
        Scanner sc = new Scanner(System.in);

        // Loop para solicitar a entrada dos vértices até que valores válidos sejam
        // digitados
        int verticeOrigem, verticeDestino;
        do {
            System.out.print("Digite o vértice de origem: ");
            verticeOrigem = sc.nextInt();
            System.out.print("Digite o vértice de destino: ");
            verticeDestino = sc.nextInt();

            if (verticeOrigem < 0 || verticeOrigem >= numVertices || verticeDestino < 0
                    || verticeDestino >= numVertices) {
                System.out.println("Os vértices digitados não estão no intervalo válido.");
            } else {
                break;
            }
        } while (true);

        System.out.print("Digite o peso da aresta: ");
        int peso = sc.nextInt();
        while (peso < 0) {
            System.out.print("Digite um peso válido: ");
            peso = sc.nextInt();
        }

        // Adiciona a aresta na matriz de adjacência
        matrizAdjacencia[verticeOrigem][verticeDestino] = peso;
        matrizAdjacencia[verticeDestino][verticeOrigem] = peso;
    }

    public void imprimirMatriz() {
        // Tamanho do campo para cada elemento da matriz
        int tamanhoCampo = 4;

        // Imprime o cabeçalho da tabela com os índices das colunas
        System.out.print("    ");
        for (int j = 0; j < this.numVertices; j++) {
            System.out.printf("%-" + tamanhoCampo + "d", j);
        }
        System.out.println("");
        // Imprime cada linha da matriz com os índices das linhas à esquerda
        for (int i = 0; i < this.numVertices; i++) {
            System.out.printf("%-" + tamanhoCampo + "d", i);
            for (int j = 0; j < this.numVertices; j++) {
                System.out.printf("%-" + tamanhoCampo + "d", this.matrizAdjacencia[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        System.out.print("Digite o número de vértices (no máximo 20): ");
        int numVertices = sc.nextInt();
        while (numVertices < 1 || numVertices > 20) {
            System.out.print("Digite um número válido (no máximo 20): ");
            numVertices = sc.nextInt();
        }
        Grafo grafo = new Grafo(numVertices);
        while (option != 5) {
            System.out.println("O que gostaria de fazer:");
            System.out.println("1 - Adicionar uma aresta");
            System.out.println("2 - Imprimir");
            System.out.println("3 - Dijkstra's ");
            System.out.println("4 - Kruskal ");
            System.out.println("5 - Sair");
            option = sc.nextInt();
            switch (option) {
                case 1:
                    grafo.adicionarAresta();
                    break;
                case 2:
                    System.out.println("Matriz de adjacência do grafo:");
                    grafo.imprimirMatriz();
                    break;
                case 3:
                    int origem, destino;
                    System.out.println("Digite um vértice para ser a origem:");
                    origem = sc.nextInt();
                    System.out.println("Digite um vértice para ser a destino:");
                    destino = sc.nextInt();
                    grafo.dijkstra(origem, destino);
                    break;
                case 4:
                    grafo.kruskal();
                    break;
                case 5:
                    break;

            }
        }
    }
}