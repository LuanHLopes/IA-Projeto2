package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;

public class AEstrela {

    private record NoAEstrela(No no, int fScore) implements Comparable<NoAEstrela> {
        @Override
        public int compareTo(NoAEstrela outro) {
            return Integer.compare(this.fScore, outro.fScore);
        }
    }

    public static void executar(Grafo grafo, String labelInicial, String labelFinal, Scanner scanner) {
        No noInicial = grafo.getNo(labelInicial);
        No noFinal = grafo.getNo(labelFinal);

        if (noInicial == null || noFinal == null) {
            System.out.println("Nó inicial ou final não encontrado no grafo.");
            return;
        }

        PriorityQueue<NoAEstrela> fronteira = new PriorityQueue<>();
        Map<No, No> veioDe = new HashMap<>();

        Map<No, Integer> gScore = new HashMap<>();
        for (No no : grafo.getTodosNos()) {
            gScore.put(no, Integer.MAX_VALUE);
        }
        gScore.put(noInicial, 0);

        int fScoreInicial = noInicial.getHeuristica();
        fronteira.add(new NoAEstrela(noInicial, fScoreInicial));

        System.out.println("Início da execução A*");
        int iteracao = 1;

        int nosExpandidos = 0;
        int nosGerados = 1;

        while (!fronteira.isEmpty()) {
            System.out.println("Iteração " + iteracao + ":");
            imprimirFronteira(fronteira, gScore);

            nosExpandidos++;

            imprimirMetricas(nosExpandidos, nosGerados);

            No atual = fronteira.poll().no();

            if (atual.equals(noFinal)) {
                System.out.println("\nFim da execução");
                imprimirResultadoFinal(veioDe, gScore, atual, nosExpandidos, nosGerados);
                return;
            }

            for (Aresta aresta : atual.getArestas()) {
                No vizinho = aresta.getDestino();
                int pesoAresta = aresta.getPeso();

                int gScoreTentativo = gScore.get(atual) + pesoAresta;

                if (gScoreTentativo < gScore.get(vizinho)) {
                    veioDe.put(vizinho, atual);
                    gScore.put(vizinho, gScoreTentativo);

                    int fScoreNovo = gScoreTentativo + vizinho.getHeuristica();
                    fronteira.add(new NoAEstrela(vizinho, fScoreNovo));
                    nosGerados++;
                }
            }
            iteracao++;

            if (!fronteira.isEmpty()) {
                System.out.print("\nPressione Enter para a próxima iteração...");
                scanner.nextLine();
                System.out.println();
            }
        }

        System.out.println("\nFim da execução");
        System.out.println("Caminho não encontrado.");
    }

    private static void imprimirFronteira(PriorityQueue<NoAEstrela> fronteira, Map<No, Integer> gScore) {
        StringBuilder sb = new StringBuilder("Fila: ");
        List<NoAEstrela> listaOrdenada = new ArrayList<>(fronteira);
        Collections.sort(listaOrdenada);

        for (NoAEstrela noWrapper : listaOrdenada) {
            No no = noWrapper.no();
            int g = gScore.get(no);

            if (g == Integer.MAX_VALUE) continue;

            int h = no.getHeuristica();
            int f = noWrapper.fScore();
            sb.append(String.format("(%s: %d+%d=%d) ", no.getLabel(), g, h, f));
        }
        System.out.println(sb.toString().trim());
    }

    private static void imprimirMetricas(int nosExpandidos, int nosGerados) {
        double fatorRamificacao = (nosExpandidos > 0) ? (double) nosGerados / nosExpandidos : 0.0;
        System.out.printf("Nós expandidos: %d%n", nosExpandidos);
        System.out.printf("Fator de Ramificação da Busca: %.2f%n", fatorRamificacao);
    }

    private static void imprimirResultadoFinal(Map<No, No> veioDe, Map<No, Integer> gScore, No atual, int nosExpandidos, int nosGerados) {
        List<No> caminho = new LinkedList<>();
        No temp = atual;
        while (temp != null) {
            caminho.addFirst(temp);
            temp = veioDe.get(temp);
        }

        System.out.printf("Distância: %d%n", gScore.get(atual));

        StringBuilder sb = new StringBuilder("Caminho: ");
        for (int i = 0; i < caminho.size(); i++) {
            sb.append(caminho.get(i).getLabel());
            if (i < caminho.size() - 1) {
                sb.append(" - ");
            }
        }
        System.out.println(sb);

        System.out.println("\n--- Medidas de Desempenho Finais ---");
        imprimirMetricas(nosExpandidos, nosGerados);
    }
}