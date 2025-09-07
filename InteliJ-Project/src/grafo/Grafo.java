package grafo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Grafo {
    private Map<String, No> nos;
    private boolean orientado;

    public Grafo() {
        this.nos = new HashMap<>();
        this.orientado = false;
    }

    public void setNo(String label){
        nos.computeIfAbsent(label, No::new);
    }

    public void setAresta(String labelOrigem, String labelDestino, int peso){
        setNo(labelOrigem);
        setNo(labelDestino);

        No noOrigem = nos.get(labelOrigem);
        No noDestino = nos.get(labelDestino);

        noOrigem.setAresta(noDestino, peso);

        if(!this.orientado){
            noDestino.setAresta(noOrigem, peso);
        }
    }

    public No getNo(String label) {
        return this.nos.get(label);
    }

    public Collection<No> getTodosNos() {
        return this.nos.values();
    }

    public boolean isOrientado() {
        return orientado;
    }

    public void setOrientado(boolean orientado) {
        this.orientado = orientado;
    }
}