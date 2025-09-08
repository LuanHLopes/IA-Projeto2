package utils;

import grafo.Grafo;
import grafo.No;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * // Classe: LeitorArquivo
 * // Descrição: Esta é uma classe utilitária, responsável por toda a lógica de
 * //            leitura e interpretação (parsing) do arquivo de entrada que define o grafo.
 * //            Ela lê o arquivo linha por linha e traduz as instruções do arquivo
 * //            em um objeto Grafo que o resto do programa consegue entender.
 */
public class LeitorArquivo {

    /**
     * // Função: carregarGrafo
     * // Descrição: Abre e processa um arquivo de texto para construir um objeto Grafo.
     * //            Ele lê cada linha, ignora comentários (qualquer coisa após '%'),
     * //            converte tudo para minúsculo e interpreta comandos como 'ponto_inicial',
     * //            'pode_ir', 'h()', etc., para popular o grafo com nós, arestas,
     * //            heurísticas e outras configurações.
     * // Entrada: caminhoArquivo (String) - O caminho para o arquivo .txt que descreve o grafo.
     * // Saída: Um objeto 'DadosArquivo' que contém o grafo montado e os nomes dos
     * //        pontos inicial e final.
     * // Lança: IOException - Se o arquivo não for encontrado, se ocorrer um erro de leitura,
     * //        ou se as informações essenciais 'ponto_inicial' e 'ponto_final' não
     * //        forem encontradas no arquivo.
     * // Pré-Condição: O arquivo no caminho especificado deve existir e seguir o formato esperado.
     * // Pós-Condição: Retorna um 'DadosArquivo' pronto para ser usado pelos algoritmos de busca.
     */
    public static DadosArquivo carregarGrafo(String caminhoArquivo) throws IOException {
        Grafo grafo = new Grafo();
        String noInicialLabel = null;
        String noFinalLabel = null;

        // 'try-with-resources' garante que o 'reader' será fechado automaticamente no final.
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Remove comentários (tudo depois de '%') e espaços em branco desnecessários.
                linha = linha.split("%")[0].trim().toLowerCase();

                if (linha.isEmpty()) {
                    continue; // Pula linhas vazias
                }

                // Interpreta cada tipo de comando do arquivo
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
                    // O formato esperado é h(Nó, heuristica, X) - ignoramos o X.
                    String[] partes = extrairConteudo(linha).split(",");
                    if (partes.length == 3) {
                        String noLabel = partes[0].trim();
                        int heuristica = Integer.parseInt(partes[2].trim());
                        grafo.setNo(noLabel); // Garante que o nó existe
                        No no = grafo.getNo(noLabel);
                        if (no != null) {
                            no.setHeuristica(heuristica);
                        }
                    }
                }
            }
        }

        // Validação final para garantir que o arquivo tinha o mínimo necessário.
        if (noInicialLabel == null || noFinalLabel == null) {
            throw new IOException("Arquivo de entrada deve especificar 'ponto_inicial' e 'ponto_final'.");
        }

        // Empacota tudo em um objeto DadosArquivo e retorna.
        return new DadosArquivo(grafo, noInicialLabel, noFinalLabel);
    }


    /**
     * // Função: extrairConteudo
     * // Descrição: Uma pequena função auxiliar (helper) para manter o código limpo.
     * //            Sua única responsabilidade é pegar uma string e retornar o que
     * //            estiver dentro do primeiro par de parênteses que encontrar.
     * //            Ex: para a entrada "comando(conteudo)", ele retorna "conteudo".
     * // Entrada: linha (String) - A linha de texto completa.
     * // Saída: O conteúdo dentro dos parênteses, ou uma string vazia se não encontrar.
     * // Pré-Condição: Nenhuma.
     * // Pós-Condição: Retorna o texto extraído.
     */
    private static String extrairConteudo(String linha) {
        int inicio = linha.indexOf('(');
        int fim = linha.lastIndexOf(')');
        if (inicio != -1 && fim != -1 && fim > inicio) {
            return linha.substring(inicio + 1, fim).trim();
        }
        return "";
    }
}