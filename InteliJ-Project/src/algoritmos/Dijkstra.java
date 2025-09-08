package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;
import java.util.stream.Collectors;

public class Dijkstra {

    private record NoDijkstra(No no, int distancia) implements Comparable<NoDijkstra> {
        @Override
        public int compareTo(NoDijkstra outro) {
            return Integer.compare(this.distancia, outro.distancia);
        }
    }

    public static void executar(Grafo grafo, String labelInicial, String labelFinal, int limiteFio, Scanner scanner) {
        No noInicial = grafo.getNo(labelInicial);
        No noFinal = grafo.getNo(labelFinal);

        Map<No, Integer> distancias = new HashMap<>();
        Map<No, No> predecessores = new HashMap<>();
        PriorityQueue<NoDijkstra> fronteira = new PriorityQueue<>();

        for (No no : grafo.getTodosNos()) {
            distancias.put(no, Integer.MAX_VALUE);
        }
        distancias.put(noInicial, 0);
        fronteira.add(new NoDijkstra(noInicial, 0));

        System.out.println("Início da execução do Dijkstra com limite de fio");
        int iteracao = 0;
        int nosExpandidos = 0;

        while (!fronteira.isEmpty()) {
            iteracao++;
            System.out.println("\nIteração " + iteracao + ":");
            imprimirListaDeControle(fronteira);
            System.out.println("Nós expandidos: " + nosExpandidos);

            NoDijkstra itemAtual = fronteira.poll();
            assert itemAtual != null;
            No noAtual = itemAtual.no();
            int distanciaAtual = itemAtual.distancia();

            if (distanciaAtual > distancias.get(noAtual)) {
                continue;
            }

            int fioRestante = limiteFio - distanciaAtual;
            System.out.print("Fio restante: " + Math.max(0, fioRestante));

            if (fioRestante < 0) {
                System.out.println(" – Caminho descartado por falta de fio");
                continue;
            } else {
                System.out.println();
            }

            nosExpandidos++;

            if (noAtual.equals(noFinal)) {
                break;
            }

            for (Aresta aresta : noAtual.getArestas()) {
                No vizinho = aresta.getDestino();
                int novaDistancia = distanciaAtual + aresta.getPeso();

                if (novaDistancia <= limiteFio && novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, noAtual);
                    fronteira.add(new NoDijkstra(vizinho, novaDistancia));
                }
            }

            if (!fronteira.isEmpty()) {
                System.out.print("\nPressione Enter para a próxima iteração...");
                scanner.nextLine();
            }
        }
        imprimirResultadoFinal(distancias, predecessores, noFinal, nosExpandidos);
    }

    private static void imprimirListaDeControle(Queue<NoDijkstra> fronteira) {
        System.out.print("Lista: ");
        if (fronteira.isEmpty()) {
            System.out.println("[]");
            return;
        }

        List<NoDijkstra> listaOrdenada = new ArrayList<>(fronteira);
        Collections.sort(listaOrdenada);

        for (NoDijkstra item : listaOrdenada) {
            System.out.printf("(%s: %d) ", item.no().getLabel(), item.distancia());
        }
        System.out.println();
    }

    private static void imprimirResultadoFinal(Map<No, Integer> distancias, Map<No, No> predecessores, No noFinal, int nosExpandidos) {
        System.out.println("\nFim da execução");
        Integer distanciaFinal = distancias.get(noFinal);

        if (distanciaFinal == null || distanciaFinal == Integer.MAX_VALUE) {
            System.out.println("Caminho não encontrado ou excede o comprimento do fio.");
            return;
        }

        System.out.println("Distância: " + distanciaFinal);
        List<No> caminho = new LinkedList<>();
        No temp = noFinal;
        while (temp != null) {
            caminho.addFirst(temp);
            temp = predecessores.get(temp);
        }

        String caminhoStr = caminho.stream()
                .map(No::getLabel)
                .collect(Collectors.joining(" - "));

        System.out.println("Caminho: " + caminhoStr);
        System.out.println("Medida de desempenho (Nós expandidos): " + nosExpandidos);
    }
}