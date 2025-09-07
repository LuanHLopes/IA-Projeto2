package grafo;

public class Aresta {
    private No destino;
    private int peso;

    public Aresta(No destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public No getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
}