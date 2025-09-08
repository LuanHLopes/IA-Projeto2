package utils;

import grafo.Grafo;

/**
 * // Record: DadosArquivo
 * // Descrição: Este é um 'record', uma forma moderna no Java de criar uma classe
 * //            que serve apenas para guardar dados de forma imutável. É basicamente
 * //            um "pacote" de informações. A sua função aqui é carregar de forma
 * //            organizada tudo o que foi lido do arquivo: o grafo completo, o nome
 * //            do nó inicial e o nome do nó final. Isso limpa o código, pois em vez
 * //            de passar 3 variáveis separadas entre as funções, passamos apenas
 * //            um objeto 'DadosArquivo'.
 * //
 * // Componentes (Campos):
 * //   - grafo (Grafo): O objeto do grafo que foi construído a partir do arquivo.
 * //   - noInicialLabel (String): O nome (label) do nó de partida da busca.
 * //   - noFinalLabel (String): O nome (label) do nó de destino da busca.
 */
public record DadosArquivo(
        Grafo grafo,
        String noInicialLabel,
        String noFinalLabel
) {}