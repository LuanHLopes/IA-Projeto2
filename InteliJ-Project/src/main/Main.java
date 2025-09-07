package main;

import algoritmos.AEstrela;
import utils.LeitorArquivo;
import utils.DadosArquivo;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static boolean grafoCarregado = false;
    private static DadosArquivo dadosDoGrafo = null;

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

            if (grafoCarregado) {
                switch (escolha) {
                    case 1:
                        carregarArquivo();
                        break;
                    case 2:
                        executarDFS();
                        break;
                    case 3:
                        executarAEstrela();
                        break;
                    case 4:
                        executarBonus();
                        break;
                    case 5:
                        System.out.println("\nEncerrando o programa. Até a próxima!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            } else {
                switch (escolha) {
                    case 1:
                        carregarArquivo();
                        break;
                    case 2:
                        System.out.println("\nEncerrando o programa. Até a próxima!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }


    private static void exibirMenu() {
        System.out.println("\n--- Projeto 2 - Algoritmos de Busca ---");
        System.out.println("========================================");
        System.out.println("1. Carregar Arquivo do Grafo");

        if (grafoCarregado) {
            System.out.println("2. Executar DFS (Pior Solução)");
            System.out.println("3. Executar A* (Melhor Solução)");
            System.out.println("4. Executar Dijkstra com Fita Limitada (Bônus)");
            System.out.println("5. Sair");
        } else{
            System.out.println("2. Sair");
        }

        System.out.println("========================================");
    }

    private static void carregarArquivo() {
        System.out.print("Digite o nome do arquivo (ex: arquivo.txt): ");
        String nomeArquivo = scanner.nextLine();
        String caminhoCompleto = "src/arquivos/" + nomeArquivo;

        try {
            dadosDoGrafo = LeitorArquivo.carregarGrafo(caminhoCompleto);
            grafoCarregado = true;
            System.out.println("\nArquivo lido e grafo montado com sucesso!");

            System.out.println("--- Informações do Grafo Carregado ---");
            System.out.println("Ponto Inicial: " + dadosDoGrafo.noInicialLabel());
            System.out.println("Ponto Final: " + dadosDoGrafo.noFinalLabel());
            String tipoGrafo = dadosDoGrafo.grafo().isOrientado() ? "Grafo Orientado" : "Grafo Não Orientado";
            System.out.println("Tipo de Grafo: " + tipoGrafo);
            System.out.println("----------------------------------------");

        } catch (IOException e) {
            grafoCarregado = false;
            dadosDoGrafo = null;
            System.err.println("\nERRO: Não foi possível ler o arquivo. Verifique o nome e o formato.");
            System.err.println("Detalhes: " + e.getMessage());
        }
    }

    private static void executarDFS() {
        System.out.println("\n--- Executando Busca em Profunidade (DFS) ---");
    }

    private static void executarAEstrela() {
        System.out.println("\n--- Executando A* (A-Estrela) ---");
        AEstrela.executar(
                dadosDoGrafo.grafo(),
                dadosDoGrafo.noInicialLabel(),
                dadosDoGrafo.noFinalLabel(),
                scanner
        );
    }

    private static void executarBonus() {
        System.out.println("\n--- Executando Bônus (Dijkstra com Fita) ---");
    }
}