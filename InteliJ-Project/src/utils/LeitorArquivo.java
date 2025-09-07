package utils;

import grafo.Grafo;
import grafo.No;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivo {

    public static DadosArquivo carregarGrafo(String caminhoArquivo) throws IOException {
        Grafo grafo = new Grafo();
        String noInicialLabel = null;
        String noFinalLabel = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.split("%")[0].trim().toLowerCase();

                if (linha.isEmpty()) {
                    continue;
                }

                if (linha.startsWith("ponto_inicial")) {
                    noInicialLabel = extrairConteudo(linha);
                } else if (linha.startsWith("ponto_final")) {
                    noFinalLabel = extrairConteudo(linha);
                } else if (linha.startsWith("orientado")) {
                    String valor = extrairConteudo(linha);
                    grafo.setOrientado(valor.equals("s"));
                } else if (linha.startsWith("pode_ir") || linha.startsWith("pode ir")) {
                    String[] partes = extrairConteudo(linha).split(",");
                    if (partes.length == 3) {
                        String origem = partes[0].trim();
                        String destino = partes[1].trim();
                        int peso = Integer.parseInt(partes[2].trim());
                        grafo.setAresta(origem, destino, peso);
                    }
                } else if (linha.startsWith("h(")) {
                    String[] partes = extrairConteudo(linha).split(",");
                    if (partes.length == 3) {
                        String noLabel = partes[0].trim();
                        int heuristica = Integer.parseInt(partes[2].trim());
                        grafo.setNo(noLabel);
                        No no = grafo.getNo(noLabel);
                        if (no != null) {
                            no.setHeuristica(heuristica);
                        }
                    }
                }
            }
        }

        if (noInicialLabel == null || noFinalLabel == null) {
            throw new IOException("Arquivo de entrada deve especificar 'ponto_inicial' e 'ponto_final'.");
        }

        return new DadosArquivo(grafo, noInicialLabel, noFinalLabel);
    }


    private static String extrairConteudo(String linha) {
        int inicio = linha.indexOf('(');
        int fim = linha.lastIndexOf(')');
        if (inicio != -1 && fim != -1 && fim > inicio) {
            return linha.substring(inicio + 1, fim).trim();
        }
        return "";
    }
}