package main;

//import grafo.Grafo;
import grafo.No;
import utils.LeitorArquivo;
import utils.DadosArquivo;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean grafoCarregado = false;

    public static void main(String[] args) {
        while (true) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextInt()) {
                System.out.println("\nOpção inválida. Por favor, digite um número.");
                scanner.next();
                continue;
            }

            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    carregarArquivo();
                    break;
                case 2:
                    if (grafoCarregado) executarDFS();
                    else System.out.println("\nPor favor, carregue um arquivo primeiro (Opção 1).");
                    break;
                case 3:
                    if (grafoCarregado) executarAEstrela();
                    else System.out.println("\nPor favor, carregue um arquivo primeiro (Opção 1).");
                    break;
                case 4:
                    if (grafoCarregado) executarBonus();
                    else System.out.println("\nPor favor, carregue um arquivo primeiro (Opção 1).");
                    break;
                case 5:
                    System.out.println("\nEncerrando o programa. Até a próxima, Teseu!");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }


    private static void exibirMenu() {
        System.out.println("\n--- Labirinto de Creta: Ajude Teseu ---");
        System.out.println("========================================");
        System.out.println("1. Carregar Arquivo do Labirinto");

        if (grafoCarregado) {
            System.out.println("2. Executar DFS (Pior Solução)");
            System.out.println("3. Executar A* (Melhor Solução)");
            System.out.println("4. Executar Dijkstra com Fita (Bônus)");
            System.out.println("5. Sair");
        } else{
            System.out.println("2. Sair");
        }

        System.out.println("========================================");
    }

    private static void carregarArquivo() {
        System.out.print("Digite o nome do arquivo (ex: labirinto.txt): ");
        String nomeArquivo = scanner.nextLine();
        String caminhoCompleto = "src/arquivos/" + nomeArquivo; // << MUDANÇA AQUI

        DadosArquivo dadosArquivo;
        try {
            dadosArquivo = LeitorArquivo.carregarGrafo(caminhoCompleto);
            grafoCarregado = true;
            System.out.println("\nArquivo lido e grafo montado com sucesso!");

            System.out.println("--- Informações do Grafo Carregado ---");
            System.out.println("Ponto Inicial: " + dadosArquivo.noInicialLabel());
            System.out.println("Ponto Final: " + dadosArquivo.noFinalLabel());
            System.out.println("O grafo é orientado? " + dadosArquivo.grafo().isOrientado());

            No noTeste = dadosArquivo.grafo().getNo(dadosArquivo.noInicialLabel());
            if (noTeste != null) {
                System.out.println("Heurística do nó inicial (" + noTeste.getLabel() + "): " + noTeste.getHeuristica());
                System.out.println("Número de conexões do nó inicial: " + noTeste.getArestas().size());
            }
            System.out.println("----------------------------------------");

        } catch (IOException e) {
            grafoCarregado = false;
            System.err.println("\nERRO: Não foi possível ler o arquivo. Verifique o nome e o formato.");
            System.err.println("Detalhes: " + e.getMessage());
        }
    }

    private static void executarDFS() {
        System.out.println("\n--- Executando Busca em Profundidade (DFS) ---");
        // A lógica do DFS será implementada aqui
    }

    private static void executarAEstrela() {
        System.out.println("\n--- Executando A* (A-Estrela) ---");
        // A lógica do A* será implementada aqui
    }

    private static void executarBonus() {
        System.out.println("\n--- Executando Bônus (Dijkstra com Fita) ---");
        // A lógica do algoritmo bônus será implementada aqui
    }
}