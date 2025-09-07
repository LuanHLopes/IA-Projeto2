package algoritmos;

import grafo.No;

public record NoAEstrela(No no, int fScore) implements Comparable<NoAEstrela> {

    @Override
    public int compareTo(NoAEstrela outro) {
        return Integer.compare(this.fScore, outro.fScore);
    }
}