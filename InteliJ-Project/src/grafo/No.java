package grafo;

import java.util.ArrayList;
import java.util.List;

public class No {

    public enum Cor { BRANCO, CINZA, PRETO }

    private final String label;
    private int heuristica;
    private final List<Aresta> arestas;

    private Cor cor;
    private No predecessor;

    public No(String label) {
        this.label = label;
        this.heuristica = 0;
        this.arestas = new ArrayList<>();
        this.cor = Cor.BRANCO;
        this.predecessor = null;
    }

    public void setAresta(No destino, int peso) {
        this.arestas.add(new Aresta(destino, peso));
    }

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