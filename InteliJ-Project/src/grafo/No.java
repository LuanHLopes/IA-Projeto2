package grafo;

import java.util.ArrayList;
import java.util.List;

/**
 * // Classe: No
 * // Descrição: Representa um nó (ou vértice) em um grafo. Cada nó tem um nome (label),
 * //            uma lista de conexões (arestas) para outros nós, e atributos
 * //            usados por algoritmos de busca, como cor, predecessor e valor heurístico.
 */
public class No {

    // Enum para controlar o estado do nó durante buscas (ex: DFS, BFS)
    // BRANCO: nó ainda não visitado.
    // CINZA: nó descoberto, mas seus vizinhos ainda não foram todos explorados.
    // PRETO: nó e todos os seus vizinhos já foram explorados.
    public enum Cor { BRANCO, CINZA, PRETO }

    private final String label; // Nome do nó, ex: "A", "B", "Casa"
    private int heuristica; // Custo estimado do nó até o destino (usado no A*)
    private final List<Aresta> arestas; // Lista de "saídas" do nó para seus vizinhos

    // Atributos de controle para os algoritmos de busca
    private Cor cor; // Cor atual do nó na busca
    private No predecessor; // De qual nó viemos para chegar até este (para reconstruir o caminho)

    /**
     * // Função: Construtor da classe No
     * // Descrição: Inicializa um novo objeto Nó com seus valores padrão.
     * //            Todo nó começa com a cor BRANCA, sem predecessor, heurística 0
     * //            e uma lista de arestas vazia.
     * // Entrada: label (String) - O identificador único do nó.
     * // Saída: Nenhuma (cria uma instância da classe).
     * // Pré-Condição: O label não deve ser nulo.
     * // Pós-Condição: Um novo objeto Nó é criado e está pronto para ser usado.
     */
    public No(String label) {
        this.label = label;
        this.heuristica = 0;
        this.arestas = new ArrayList<>();
        this.cor = Cor.BRANCO;
        this.predecessor = null;
    }

    /**
     * // Função: setAresta
     * // Descrição: Cria e adiciona uma nova aresta que sai deste nó em direção
     * //            a um nó de destino, com um peso definido.
     * // Entrada: destino (No) - O nó para onde a aresta aponta.
     * //          peso (int) - O custo para atravessar essa aresta.
     * // Saída: Nenhuma.
     * // Pré-Condição: O nó de destino deve ser um objeto válido.
     * // Pós-Condição: A lista de arestas do nó atual contém uma nova aresta.
     */
    public void setAresta(No destino, int peso) {
        this.arestas.add(new Aresta(destino, peso));
    }

    // --- GETTERS E SETTERS ---
    // Métodos simples para acessar e modificar os atributos privados da classe.
    // Isso é importante para o encapsulamento, garantindo que os dados
    // só sejam alterados de forma controlada.

    public String getLabel() {
        return label;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }

    public List<Aresta> getArestas() {
        return arestas;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public No getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(No predecessor) {
        this.predecessor = predecessor;
    }
}