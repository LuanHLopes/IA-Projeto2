package grafo;

import java.util.ArrayList;
import java.util.List;

public class No {
    public String label;
    public int heuristica;
    private List<Aresta> arestas;

    public No(String label) {
        this.label = label;
        arestas = new ArrayList<>();
        this.heuristica = 0;
    }

    public void setAresta(No destino, int peso) {
        this.arestas.add(new Aresta(destino, peso));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
