package utils;

import grafo.Grafo;

public record DadosArquivo(
        Grafo grafo,
        String noInicialLabel,
        String noFinalLabel
) {}