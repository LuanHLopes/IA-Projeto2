package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;

/**
 * // Classe: AEstrela
 * // Descrição: Implementa o algoritmo de busca A* (A-Star), que encontra o caminho
 * //            de menor custo entre dois nós em um grafo. Ele usa uma heurística para
 * //            guiar a busca de forma mais eficiente que algoritmos não-informados.
 */
public class AEstrela {

    /**
     * // Record: NoAEstrela
     * // Descrição: Uma pequena estrutura de dados para ajudar o algoritmo. Ela "empacota"
     * //            um Nó do grafo junto com seu fScore (custo g + heurística h).
     * //            Implementar 'Comparable' é o truque para que a Fila de Prioridade
     * //            consiga se auto-organizar, mantendo sempre o nó de menor fScore
     * //            no topo, pronto para ser escolhido.
     */
    private record NoAEstrela(No no, int fScore) implements Comparable<NoAEstrela> {
        @Override
        public int compareTo(NoAEstrela outro) {
            return Integer.compare(this.fScore, outro.fScore);
        }
    }

    /**
     * // Função: executar
     * // Descrição: Método principal que executa o A*. Ele gerencia a "fronteira" de nós a serem
     * //            explorados usando uma Fila de Prioridade e mantém os custos (gScore) de cada nó.
     * //            A cada passo, ele escolhe o nó mais promissor da fila, o expande e atualiza
     * //            os custos de seus vizinhos se um caminho melhor for encontrado.
     * // Entrada: grafo (Grafo) - O grafo com os nós e arestas.
     * //          labelInicial (String) - O nome do nó de partida.
     * //          labelFinal (String) - O nome do nó objetivo.
     * //          scanner (Scanner) - Usado para pausar a execução a cada iteração.
     * // Saída: Nenhuma (imprime o passo a passo e o resultado final no console).
     * // Pré-Condição: O grafo deve estar criado, com as heurísticas dos nós definidas.
     * //               Os nós inicial e final devem existir.
     * // Pós-Condição: Ao final, o caminho de menor custo é exibido, junto com métricas de desempenho.
     */
    public static void executar(Grafo grafo, String labelInicial, String labelFinal, Scanner scanner) {
        No noInicial = grafo.getNo(labelInicial);
        No noFinal = grafo.getNo(labelFinal);

        if (noInicial == null || noFinal == null) {
            System.out.println("Nó inicial ou final não encontrado no grafo.");
            return;
        }

        // A Fila de Prioridade é a "fronteira" de nós a serem explorados.
        PriorityQueue<NoAEstrela> fronteira = new PriorityQueue<>();

        // Mapa para reconstruir o caminho no final, guardando "de onde viemos" para cada nó.
        Map<No, No> veioDe = new HashMap<>();

        // Mapa para guardar o gScore: o custo do caminho do início até o nó atual.
        Map<No, Integer> gScore = new HashMap<>();
        for (No no : grafo.getTodosNos()) {
            gScore.put(no, Integer.MAX_VALUE); // Começa tudo com custo infinito
        }
        gScore.put(noInicial, 0); // O custo para chegar no início é zero

        // fScore = gScore + heurística. Para o nó inicial, gScore é 0.
        int fScoreInicial = noInicial.getHeuristica();
        fronteira.add(new NoAEstrela(noInicial, fScoreInicial));

        System.out.println("Início da execução A*");
        int iteracao = 1;
        int nosExpandidos = 0;
        int nosGerados = 1; // Já começa com o nó inicial gerado

        while (!fronteira.isEmpty()) {
            System.out.println("Iteração " + iteracao + ":");
            imprimirFronteira(fronteira, gScore);

            No atual = Objects.requireNonNull(fronteira.poll()).no(); // Pega o nó mais promissor da fila
            nosExpandidos++;
            imprimirMetricas(nosExpandidos, nosGerados);


            if (atual.equals(noFinal)) {
                System.out.println("\nFim da execução: Nó objetivo encontrado!");
                imprimirResultadoFinal(veioDe, gScore, atual, nosExpandidos, nosGerados);
                return;
            }

            // Explora os vizinhos do nó atual
            for (Aresta aresta : atual.getArestas()) {
                No vizinho = aresta.getDestino();
                int pesoAresta = aresta.getPeso();

                // Calcula o custo para chegar neste vizinho passando pelo nó atual
                int gScoreTentativo = gScore.get(atual) + pesoAresta;

                // Se encontramos um caminho mais barato para o vizinho...
                if (gScoreTentativo < gScore.get(vizinho)) {
                    //... atualizamos tudo!
                    veioDe.put(vizinho, atual); // Anota o novo "pai"
                    gScore.put(vizinho, gScoreTentativo); // Atualiza o custo g

                    int fScoreNovo = gScoreTentativo + vizinho.getHeuristica(); // Recalcula o fScore
                    fronteira.add(new NoAEstrela(vizinho, fScoreNovo)); // Adiciona na fronteira para ser explorado
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

    /**
     * // Função: imprimirFronteira
     * // Descrição: Mostra o conteúdo da Fila de Prioridade de forma organizada.
     * //            Para cada nó na fila, exibe seu gScore, heurística (h) e o fScore total (g+h=f).
     * //            Isso ajuda a visualizar por que o A* está fazendo suas escolhas.
     * // Entrada: fronteira (PriorityQueue) - A fila com os nós a serem explorados.
     * //          gScore (Map) - O mapa com os custos para chegar em cada nó.
     * // Saída: Nenhuma (imprime o estado da fila no console).
     * // Pré-Condição: A fronteira e o mapa gScore devem estar inicializados.
     * // Pós-Condição: O estado atual da fronteira é exibido de forma legível.
     */
    private static void imprimirFronteira(PriorityQueue<NoAEstrela> fronteira, Map<No, Integer> gScore) {
        StringBuilder sb = new StringBuilder("Fila: ");
        List<NoAEstrela> listaOrdenada = new ArrayList<>(fronteira);
        Collections.sort(listaOrdenada);

        for (NoAEstrela noWrapper : listaOrdenada) {
            No no = noWrapper.no();
            int g = gScore.get(no);

            if (g == Integer.MAX_VALUE) continue; // Não mostra nós inalcançáveis ainda

            int h = no.getHeuristica();
            int f = noWrapper.fScore();
            sb.append(String.format("(%s: %d+%d=%d) ", no.getLabel(), g, h, f));
        }
        System.out.println(sb.toString().trim());
    }

    /**
     * // Função: imprimirMetricas
     * // Descrição: Exibe as métricas de desempenho da busca em um determinado momento.
     * //            Mostra quantos nós já foram expandidos e o fator de ramificação.
     * // Entrada: nosExpandidos (int) - A contagem de nós já retirados da fila.
     * //          nosGerados (int) - A contagem de nós já adicionados à fila.
     * // Saída: Nenhuma (imprime as métricas no console).
     * // Pré-Condição: Nenhuma.
     * // Pós-Condição: As métricas de desempenho são exibidas.
     */
    private static void imprimirMetricas(int nosExpandidos, int nosGerados) {
        double fatorRamificacao = (nosExpandidos > 0) ? (double) nosGerados / nosExpandidos : 0.0;
        System.out.printf("Nós expandidos: %d%n", nosExpandidos);
        System.out.printf("Fator de Ramificação da Busca: %.2f%n", fatorRamificacao);
    }

    /**
     * // Função: imprimirResultadoFinal
     * // Descrição: Após encontrar o nó final, esta função reconstrói o caminho percorrido
     * //            e exibe o resultado completo, incluindo a distância total (custo),
     * //            a sequência de nós e as métricas finais de desempenho.
     * // Entrada: veioDe (Map) - Mapa para reconstruir o caminho.
     * //          gScore (Map) - Mapa com os custos finais.
     * //          atual (No) - O nó de destino que foi encontrado.
     * //          nosExpandidos (int) - Total de nós expandidos.
     * //          nosGerados (int) - Total de nós gerados.
     * // Saída: Nenhuma (imprime o resumo do resultado no console).
     * // Pré-Condição: O algoritmo deve ter encontrado o caminho.
     * // Pós-Condição: O resultado final é apresentado de forma clara.
     */
    private static void imprimirResultadoFinal(Map<No, No> veioDe, Map<No, Integer> gScore, No atual, int nosExpandidos, int nosGerados) {
        List<No> caminho = new LinkedList<>();
        No temp = atual;
        // Volta do final para o início usando o mapa 'veioDe' para montar a lista do caminho
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