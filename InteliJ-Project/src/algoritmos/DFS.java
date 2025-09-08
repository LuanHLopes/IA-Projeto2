package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;
import java.util.stream.Collectors;

public class DFS {

    public static void executar(Grafo grafo, String labelInicial, String labelFinal, Scanner scanner) {
        System.out.println("Início da execução DFS");

        // 1. Inicialização: Todos os nós começam BRANCO e sem predecessor.
        for (No no : grafo.getTodosNos()) {
            no.setCor(No.Cor.BRANCO);
            no.setPredecessor(null);
        }

        No noInicial = grafo.getNo(labelInicial);
        No noFinal = grafo.getNo(labelFinal);

        if (noInicial == null || noFinal == null) {
            System.out.println("Nó inicial ou final não encontrado.");
            return;
        }

        Deque<No> pilha = new LinkedList<>();
        int nosVisitados = 0;
        int iteracao = 0;

        // Inicia o algoritmo
        noInicial.setCor(No.Cor.CINZA);
        pilha.push(noInicial);

        while (!pilha.isEmpty()) {
            iteracao++;
            System.out.println("Iteração " + iteracao + ":");
            imprimirPilha(pilha);
            System.out.println("Nós visitados: " + nosVisitados);

            No u = pilha.pop();
            nosVisitados++;

            if (u.equals(noFinal)) {
                break; // Encontrou o caminho
            }

            for (Aresta aresta : u.getArestas()) {
                No v = aresta.getDestino();
                if (v.getCor() == No.Cor.BRANCO) {
                    v.setCor(No.Cor.CINZA);
                    v.setPredecessor(u);
                    pilha.push(v);
                }
            }
            u.setCor(No.Cor.PRETO);

            if (!pilha.isEmpty()) {
                System.out.print("\nPressione Enter para a próxima iteração...");
                scanner.nextLine();
                System.out.println();
            }
        }

        System.out.println("\nFim da execução");
        imprimirResultadoFinal(noFinal, nosVisitados);
    }

    private static void imprimirPilha(Deque<No> pilha) {
        // DFS não usa heurística, então imprimimos apenas o nome do nó.
        String conteudoPilha = pilha.stream()
                .map(No::getLabel)
                .collect(Collectors.joining(", "));
        System.out.println("Pilha: [" + conteudoPilha + "]");
    }

    private static void imprimirResultadoFinal(No noFinal, int nosVisitados) {
        if (noFinal.getPredecessor() == null && !noFinal.getCor().equals(No.Cor.CINZA)) {
            System.out.println("Caminho não encontrado.");
            return;
        }

        List<No> caminho = new LinkedList<>();
        No temp = noFinal;
        int custoTotal = 0;

        // Reconstrói o caminho e calcula o custo
        while (temp.getPredecessor() != null) {
            caminho.addFirst(temp);
            No pai = temp.getPredecessor();
            // Encontra a aresta entre o pai e o filho para somar o custo
            for(Aresta a : pai.getArestas()){
                if(a.getDestino().equals(temp)){
                    custoTotal += a.getPeso();
                    break;
                }
            }
            temp = pai;
        }
        caminho.addFirst(temp);

        String caminhoStr = caminho.stream()
                .map(No::getLabel)
                .collect(Collectors.joining(" - "));

        System.out.println("Distância: " + custoTotal);
        System.out.println("Caminho: " + caminhoStr);
        System.out.println("Medida de desempenho (Nós visitados): " + nosVisitados);
    }
}