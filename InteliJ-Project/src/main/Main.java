package main;

import algoritmos.*;
import utils.LeitorArquivo;
import utils.DadosArquivo;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // Scanner global para não precisar criar um novo a cada leitura
    private static final Scanner scanner = new Scanner(System.in);

    // Variáveis de controle para saber se o grafo já foi carregado
    private static boolean grafoCarregado = false;
    private static DadosArquivo dadosGrafo = null;

    /**
     * // Função: main
     * // Descrição: Ponto de entrada do programa. Fica em loop exibindo o menu
     * //            principal e esperando o usuário escolher uma opção. Ele controla
     * //            o fluxo principal, chamando as outras funções com base na
     * //            escolha do usuário.
     * // Entrada: args (String[]) - Argumentos de linha de comando, que não usamos aqui.
     * // Saída: Nenhuma.
     * // Pré-Condição: Nenhuma.
     * // Pós-Condição: O programa é encerrado quando o usuário escolhe a opção de sair.
     */
    public static void main(String[] args) {
        while (true) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");

            // Validação para garantir que o usuário digitou um número
            if (!scanner.hasNextInt()) {
                System.out.println("\nOpção inválida. Por favor, digite um número.");
                scanner.next(); // Limpa o buffer do scanner
                continue;
            }

            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consome o "Enter" que sobrou

            // Lógica para lidar com as opções do menu
            if (grafoCarregado) {
                // Menu completo, quando o grafo já foi carregado
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
                        return; // Sai do programa
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            } else {
                // Menu inicial, antes de carregar o grafo
                switch (escolha) {
                    case 1:
                        carregarArquivo();
                        break;
                    case 2:
                        System.out.println("\nEncerrando o programa. Até a próxima!");
                        scanner.close();
                        return; // Sai do programa
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    /**
     * // Função: exibirMenu
     * // Descrição: Apenas imprime o menu de opções na tela. O menu é dinâmico:
     * //            se o grafo ainda não foi carregado, mostra menos opções.
     * // Entrada: Nenhuma.
     * // Saída: Nenhuma (só imprime no console).
     * // Pré-Condição: Nenhuma.
     * // Pós-Condição: O texto do menu é exibido na tela do usuário.
     */
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

    /**
     * // Função: carregarArquivo
     * // Descrição: Pede para o usuário o nome de um arquivo, tenta carregar e
     * //            montar o grafo a partir dele. Se conseguir, atualiza as
     * //            variáveis de controle e mostra um resumo do grafo. Se der erro,
     * //            avisa o usuário.
     * // Entrada: Nenhuma (pega o nome do arquivo do console).
     * // Saída: Nenhuma (imprime o resultado da operação no console).
     * // Pré-Condição: Nenhuma.
     * // Pós-Condição: Se o arquivo for válido, 'grafoCarregado' vira 'true' e
     * //               'dadosGrafo' guarda as informações. Se não, as variáveis
     * //               são resetadas e uma mensagem de erro é exibida.
     */
    private static void carregarArquivo() {
        System.out.print("Digite o nome do arquivo (ex: arquivo.txt): ");
        String nomeArquivo = scanner.nextLine();
        String caminhoCompleto = "src/arquivos/" + nomeArquivo;

        try {
            dadosGrafo = LeitorArquivo.carregarGrafo(caminhoCompleto);
            grafoCarregado = true;
            System.out.println("\nArquivo lido e grafo montado com sucesso!");

            System.out.println("--- Informações do Grafo Carregado ---");
            System.out.println("Ponto Inicial: " + dadosGrafo.noInicialLabel());
            System.out.println("Ponto Final: " + dadosGrafo.noFinalLabel());
            String tipoGrafo = dadosGrafo.grafo().isOrientado() ? "Grafo Orientado" : "Grafo Não Orientado";
            System.out.println("Tipo de Grafo: " + tipoGrafo);
            System.out.println("----------------------------------------");

        } catch (IOException e) {
            grafoCarregado = false;
            dadosGrafo = null;
            System.err.println("\nERRO: Não foi possível ler o arquivo. Verifique o nome e o formato.");
            System.err.println("Detalhes: " + e.getMessage());
        }
    }

    /**
     * // Função: executarDFS
     * // Descrição: Chama a classe responsável por rodar o algoritmo de Busca
     * //            em Profundidade (DFS), passando os dados do grafo que já foi carregado.
     * // Entrada: Nenhuma.
     * // Saída: Nenhuma (o resultado do algoritmo é impresso pela classe DFS).
     * // Pré-Condição: Um grafo precisa ter sido carregado com sucesso (grafoCarregado == true).
     * // Pós-Condição: O algoritmo DFS é executado.
     */
    private static void executarDFS() {
        System.out.println("\n--- Executando Busca em Profundidade (DFS) ---");
        DFS.executar(
                dadosGrafo.grafo(),
                dadosGrafo.noInicialLabel(),
                dadosGrafo.noFinalLabel(),
                scanner
        );
    }

    /**
     * // Função: executarAEstrela
     * // Descrição: Inicia a execução do algoritmo de busca A* (A-Estrela),
     * //            utilizando o grafo que está carregado na memória.
     * // Entrada: Nenhuma.
     * // Saída: Nenhuma (a classe AEstrela cuida de mostrar os resultados).
     * // Pré-Condição: O grafo deve ter sido carregado (grafoCarregado == true).
     * // Pós-Condição: O algoritmo A* é executado.
     */
    private static void executarAEstrela() {
        System.out.println("\n--- Executando A* (A-Estrela) ---");
        AEstrela.executar(
                dadosGrafo.grafo(),
                dadosGrafo.noInicialLabel(),
                dadosGrafo.noFinalLabel(),
                scanner
        );
    }

    /**
     * // Função: executarBonus
     * // Descrição: Roda o algoritmo extra do projeto (Dijkstra com limite).
     * //            Primeiro, ele pergunta ao usuário qual o limite (comprimento do fio)
     * //            e depois chama a função que executa o algoritmo.
     * // Entrada: Nenhuma (pede o limite para o usuário via console).
     * // Saída: Nenhuma (a classe Dijkstra mostra o resultado).
     * // Pré-Condição: O grafo precisa estar carregado (grafoCarregado == true).
     * // Pós-Condição: O algoritmo de Dijkstra com limite é executado.
     */
    private static void executarBonus() {
        System.out.println("\n--- Executando Bônus (Dijkstra com Fita Limitada) ---");
        try {
            System.out.print("Qual o comprimento do fio? ");
            int limiteFio = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            Dijkstra.executar(
                    dadosGrafo.grafo(),
                    dadosGrafo.noInicialLabel(),
                    dadosGrafo.noFinalLabel(),
                    limiteFio,
                    scanner
            );
        } catch (InputMismatchException e) {
            System.out.println("Erro: Por favor, digite um número inteiro para o comprimento do fio.");
            scanner.nextLine(); // Limpa o buffer em caso de erro
        }
    }
}