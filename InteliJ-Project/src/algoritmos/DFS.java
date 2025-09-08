package algoritmos;

import grafo.Aresta;
import grafo.Grafo;
import grafo.No;

import java.util.*;
import java.util.stream.Collectors;

/**
 * // Classe: DFS
 * // Descrição: Contém a lógica para executar o algoritmo de Busca em Profundidade
 * //            (Depth-First Search). A busca é feita de forma estática, ou seja,
 * //            não precisamos criar um objeto DFS para usá-la.
 */
public class DFS {

    /**
     * // Função: executar
     * // Descrição: Roda o algoritmo DFS a partir de um nó inicial até um nó final.
     * //            Este método gerencia todo o processo: inicializa os nós, controla o
     * //            loop principal da busca usando uma pilha, e no final mostra o resultado.
     * //            Ele também pausa a cada iteração para que o usuário possa acompanhar o passo a passo.
     * // Entrada: grafo (Grafo) - O grafo onde a busca será feita.
     * //          labelInicial (String) - O nome do nó de partida.
     * //          labelFinal (String) - O nome do nó que estamos procurando.
     * //          scanner (Scanner) - Para ler o "Enter" do usuário e pausar as iterações.
     * // Saída: Nenhuma (imprime o processo e o resultado no console).
     * // Pré-Condição: O grafo deve estar montado e os nós inicial e final devem existir.
     * // Pós-Condição: Ao final, exibe o caminho encontrado (se houver), seu custo
     * //               e o número de nós visitados. Os nós do grafo ficam com seus
     * //               atributos 'cor' e 'predecessor' modificados.
     */
    public static void executar(Grafo grafo, String labelInicial, String labelFinal, Scanner scanner) {
        System.out.println("Início da execução DFS");

        // 1. Inicialização: "Pinta" todos os nós de BRANCO e limpa os predecessores.
        // É como resetar o grafo para um estado inicial limpo antes da busca.
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

        // Deque funciona como uma pilha (stack) aqui.
        Deque<No> pilha = new LinkedList<>();
        int nosVisitados = 0;
        int iteracao = 0;

        // Começa o algoritmo pelo nó inicial
        noInicial.setCor(No.Cor.CINZA); // Marca como "sendo visitado"
        pilha.push(noInicial); // Coloca na pilha para explorar

        while (!pilha.isEmpty()) {
            iteracao++;
            System.out.println("Iteração " + iteracao + ":");
            imprimirPilha(pilha);
            System.out.println("Nós visitados: " + nosVisitados);

            No u = pilha.pop(); // Pega o nó do topo da pilha para explorar
            nosVisitados++;

            if (u.equals(noFinal)) {
                break; // Achou! Para o loop.
            }

            // Olha todos os vizinhos do nó 'u'
            for (Aresta aresta : u.getArestas()) {
                No v = aresta.getDestino();
                if (v.getCor() == No.Cor.BRANCO) { // Se o vizinho ainda não foi visitado...
                    v.setCor(No.Cor.CINZA); // Marca ele como "sendo visitado"
                    v.setPredecessor(u); // Anota que chegamos em 'v' a partir de 'u'
                    pilha.push(v); // Adiciona na pilha para ser o próximo a ser explorado
                }
            }
            u.setCor(No.Cor.PRETO); // Marca 'u' como totalmente explorado

            if (!pilha.isEmpty()) {
                System.out.print("\nPressione Enter para a próxima iteração...");
                scanner.nextLine();
                System.out.println();
            }
        }

        System.out.println("\nFim da execução");
        imprimirResultadoFinal(noFinal, nosVisitados);
    }

    /**
     * // Função: imprimirPilha
     * // Descrição: Método auxiliar para mostrar o estado atual da pilha de execução.
     * //            Como o DFS puro não se importa com custos ou heurísticas,
     * //            mostramos apenas o nome (label) de cada nó na pilha.
     * // Entrada: pilha (Deque<No>) - A pilha do algoritmo DFS.
     * // Saída: Nenhuma (imprime a pilha formatada no console).
     * // Pré-Condição: A pilha deve ter sido inicializada.
     * // Pós-Condição: O conteúdo da pilha é exibido na tela.
     */
    private static void imprimirPilha(Deque<No> pilha) {
        String conteudoPilha = pilha.stream()
                .map(No::getLabel)
                .collect(Collectors.joining(", "));
        System.out.println("Pilha: [" + conteudoPilha + "]");
    }

    /**
     * // Função: imprimirResultadoFinal
     * // Descrição: Monta e exibe o resultado final da busca. Se um caminho foi
     * //            encontrado, ele volta do nó final até o inicial (usando os
     * //            predecessores) para reconstruir a rota e calcular o custo total.
     * // Entrada: noFinal (No) - O nó de destino ao término da busca.
     * //          nosVisitados (int) - Total de nós processados pelo algoritmo.
     * // Saída: Nenhuma (imprime o resumo do resultado no console).
     * // Pré-Condição: O algoritmo DFS principal deve ter terminado sua execução.
     * // Pós-Condição: O resultado da busca é apresentado de forma clara para o usuário.
     */
    private static void imprimirResultadoFinal(No noFinal, int nosVisitados) {
        // Se o nó final não tem predecessor, significa que ele nunca foi alcançado.
        if (noFinal.getPredecessor() == null && !noFinal.getCor().equals(No.Cor.CINZA)) {
            System.out.println("Caminho não encontrado.");
            return;
        }

        List<No> caminho = new LinkedList<>();
        No temp = noFinal;
        int custoTotal = 0;

        // Volta de "trás pra frente" (do final para o início) para montar o caminho
        while (temp.getPredecessor() != null) {
            caminho.addFirst(temp); // Adiciona no início da lista para inverter a ordem
            No pai = temp.getPredecessor();
            // Pega o peso da aresta entre o pai e o filho (temp)
            for(Aresta a : pai.getArestas()){
                if(a.getDestino().equals(temp)){
                    custoTotal += a.getPeso();
                    break;
                }
            }
            temp = pai;
        }
        caminho.addFirst(temp); // Adiciona o nó inicial, que não tem predecessor

        String caminhoStr = caminho.stream()
                .map(No::getLabel)
                .collect(Collectors.joining(" - "));

        System.out.println("Distância: " + custoTotal);
        System.out.println("Caminho: " + caminhoStr);
        System.out.println("Medida de desempenho (Nós visitados): " + nosVisitados);
    }
}