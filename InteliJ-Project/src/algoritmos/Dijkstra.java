package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;
import java.util.stream.Collectors;

/**
 * // Classe: Dijkstra
 * // Descrição: Contém a implementação do algoritmo de Dijkstra, usado para encontrar
 * //            o caminho mais curto em um grafo ponderado. Esta versão tem uma
 * //            modificação especial: um limite de custo total (o "limite de fio"),
 * //            que descarta caminhos que se tornam muito "caros".
 */
public class Dijkstra {

    /**
     * // Record: NoDijkstra
     * // Descrição: Uma estrutura auxiliar para facilitar o uso da Fila de Prioridade.
     * //            Ela junta um Nó com a sua distância acumulada desde o ponto inicial.
     * //            Ao implementar 'Comparable', permitimos que a PriorityQueue ordene
     * //            automaticamente os itens, colocando sempre o nó com a menor
     * //            distância na frente, que é a essência do Dijkstra.
     */
    private record NoDijkstra(No no, int distancia) implements Comparable<NoDijkstra> {
        @Override
        public int compareTo(NoDijkstra outro) {
            return Integer.compare(this.distancia, outro.distancia);
        }
    }

    /**
     * // Função: executar
     * // Descrição: Roda o algoritmo de Dijkstra para achar o caminho mais curto entre dois nós,
     * //            mas com a restrição de que o custo total não pode passar do 'limiteFio'.
     * //            Ele usa uma Fila de Prioridade para sempre explorar o nó mais próximo da
     * //            origem. A cada passo, ele verifica se o caminho até um vizinho é mais curto
     * //            que o já conhecido E se ele não estoura o limite de fio.
     * // Entrada: grafo (Grafo) - O grafo para a busca.
     * //          labelInicial (String) - O nome do nó de partida.
     * //          labelFinal (String) - O nome do nó de chegada.
     * //          limiteFio (int) - O custo máximo que o caminho pode ter.
     * //          scanner (Scanner) - Para pausar a execução a cada passo.
     * // Saída: Nenhuma (imprime o passo a passo e o resultado no console).
     * // Pré-Condição: O grafo deve estar montado e os nós de início e fim devem existir.
     * // Pós-Condição: Exibe o caminho mais curto encontrado que respeita o limite,
     * //               ou informa se nenhum caminho foi encontrado sob essa condição.
     */
    public static void executar(Grafo grafo, String labelInicial, String labelFinal, int limiteFio, Scanner scanner) {
        No noInicial = grafo.getNo(labelInicial);
        No noFinal = grafo.getNo(labelFinal);

        // Mapa para guardar a menor distância encontrada do início até cada nó.
        Map<No, Integer> distancias = new HashMap<>();
        // Mapa para reconstruir o caminho no final.
        Map<No, No> predecessores = new HashMap<>();
        // Fila de Prioridade que sempre nos dará o nó mais próximo para visitar.
        PriorityQueue<NoDijkstra> fronteira = new PriorityQueue<>();

        // Inicialização: todas as distâncias começam como infinito.
        for (No no : grafo.getTodosNos()) {
            distancias.put(no, Integer.MAX_VALUE);
        }
        // A distância do início até ele mesmo é 0.
        distancias.put(noInicial, 0);
        fronteira.add(new NoDijkstra(noInicial, 0));

        System.out.println("Início da execução do Dijkstra com limite de fio");
        int iteracao = 0;
        int nosExpandidos = 0;

        while (!fronteira.isEmpty()) {
            iteracao++;
            System.out.println("\nIteração " + iteracao + ":");
            imprimirListaDeControle(fronteira);

            // Pega o nó com a menor distância da fila.
            NoDijkstra itemAtual = fronteira.poll();
            assert itemAtual != null;
            No noAtual = itemAtual.no();
            int distanciaAtual = itemAtual.distancia();

            // Se já encontramos um caminho mais curto para este nó, pulamos. Otimização.
            if (distanciaAtual > distancias.get(noAtual)) {
                continue;
            }

            // A lógica principal do bônus: verificar o limite de fio.
            int fioRestante = limiteFio - distanciaAtual;
            System.out.print("Fio restante: " + Math.max(0, fioRestante));
            if (fioRestante < 0) {
                System.out.println(" – Caminho descartado por falta de fio");
                continue; // Pula para a próxima iteração, descartando este caminho.
            } else {
                System.out.println();
            }

            nosExpandidos++;
            System.out.println("Nós expandidos: " + nosExpandidos);

            // Se chegamos no destino, podemos parar.
            if (noAtual.equals(noFinal)) {
                break;
            }

            // Para cada vizinho do nó atual...
            for (Aresta aresta : noAtual.getArestas()) {
                No vizinho = aresta.getDestino();
                int novaDistancia = distanciaAtual + aresta.getPeso();

                // ...se o novo caminho for mais curto E não estourar o limite...
                if (novaDistancia <= limiteFio && novaDistancia < distancias.get(vizinho)) {
                    // ...atualizamos as informações.
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

    /**
     * // Função: imprimirListaDeControle
     * // Descrição: Função auxiliar que mostra o estado atual da Fila de Prioridade.
     * //            É útil para depurar e entender o comportamento do algoritmo passo a passo.
     * // Entrada: fronteira (Queue<NoDijkstra>) - A Fila de Prioridade do algoritmo.
     * // Saída: Nenhuma (imprime a lista formatada no console).
     * // Pré-Condição: A fila não deve ser nula.
     * // Pós-Condição: O conteúdo da fila é exibido de forma ordenada.
     */
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

    /**
     * // Função: imprimirResultadoFinal
     * // Descrição: Exibe o resultado final da busca. Reconstrói o caminho
     * //            a partir do mapa de predecessores e mostra a distância total
     * //            e a rota, além do número de nós expandidos como métrica.
     * // Entrada: distancias (Map) - Mapa com as distâncias finais.
     * //          predecessores (Map) - Mapa para reconstruir o caminho.
     * //          noFinal (No) - O nó de destino da busca.
     * //          nosExpandidos (int) - Contador de nós processados.
     * // Saída: Nenhuma (imprime o resultado final no console).
     * // Pré-Condição: O algoritmo de Dijkstra deve ter terminado.
     * // Pós-Condição: O resultado é apresentado de forma clara para o usuário.
     */
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
        // Volta do fim para o começo para montar o caminho na ordem correta.
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