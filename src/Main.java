import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Scanner;

import classes.Agencia;
import classes.Estados;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);
    private static final Agencia agencia = new Agencia();
    private static String arquivo;

    public static void main(String[] args) {
        arquivo = args.length > 0 ? args[0] : Path.of("src", "candidatos.txt").toString();

        System.out.println("Carregando candidatos de: " + Path.of(arquivo).toAbsolutePath());
        try {
            agencia.lerArquivo(arquivo);
            System.out.println("Arquivo carregado com sucesso. Candidatos cadastrados: "
                    + agencia.getTotal() + "\n");
        } catch (IOException e) {
            System.out.println("Não foi possível carregar os candidatos: " + e.getMessage() + "\n");
        }

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1: {
                    int id = lerInteiro("ID do candidato: ");
                    int grau = lerInteiro("Grau de interesse desejado (1 a 15): ");
                    System.out.print(agencia.defineGrau(id, grau));
                    break;
                }
                case 2: {
                    int id = lerInteiro("ID do candidato: ");
                    System.out.print(agencia.companheirosDe(id));
                    break;
                }
                case 3: {
                    int k = lerInteiro("Quantidade K de candidatos a exibir: ");
                    System.out.print(agencia.topK(k));
                    break;
                }
                case 4:
                    System.out.print(agencia.semCompanheiros());
                    break;
                case 5: {
                    System.out.println("Áreas: 1-Esportes, 2-Artes, 3-Música, 4-Cinema, "
                            + "5-Tecnologia, 6-Animais, 7-Gastronomia, 8-Ciências");
                    int area = lerInteiro("Código da área de interesse (1 a 8): ");
                    System.out.print(agencia.candidatosPorArea(area));
                    break;
                }
                case 6: {
                    System.out.print("Estados aceitos: ");
                    for (int i = 0; i < Estados.quantidade(); i++) {
                        System.out.print(Estados.get(i));
                        if (i < Estados.quantidade() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                    System.out.print("Sigla do estado: ");
                    String sigla = teclado.nextLine().trim();
                    System.out.print(agencia.candidatosPorEstado(sigla));
                    break;
                }
                case 7: {
                    int quantidade = lerInteiro("Quantidade de candidatos fictícios a gerar: ");
                    gerarBaseFicticia(quantidade);
                    break;
                }
                case 0:
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha uma opção de 0 a 7.");
            }
            System.out.println();
        } while (opcao != 0);

        teclado.close();
    }

    private static void exibirMenu() {
        System.out.println("========== AGÊNCIA DE RELACIONAMENTO ==========");
        System.out.println("1 - Inserir/atualizar grau de interesse desejado");
        System.out.println("2 - Listar potenciais companheiros de um candidato");
        System.out.println("3 - Mostrar os K candidatos com mais potenciais companheiros");
        System.out.println("4 - Listar candidatos sem potenciais companheiros");
        System.out.println("5 - Listar candidatos por área de interesse");
        System.out.println("6 - Listar candidatos por estado");
        System.out.println("7 - Gerar candidatos fictícios no arquivo candidatos.txt");
        System.out.println("0 - Sair");
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = teclado.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }


    private static void gerarBaseFicticia(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser positiva.");
            return;
        }

        try {
            gerarCandidatosFicticios(arquivo, quantidade);
        } catch (IOException e) {
            System.out.println("Erro ao gerar candidatos: " + e.getMessage());
            return;
        }

        try {
            agencia.lerArquivo(arquivo);
            System.out.println("Base carregada. Total de candidatos: " + agencia.getTotal());
        } catch (IOException e) {
            System.out.println("O arquivo foi gerado, mas não foi possível carregá-lo: "
                    + e.getMessage());
        }
    }

    private static void gerarCandidatosFicticios(String caminho, int quantidade) throws IOException {
        Random random = new Random();

        String[] nomes = {
                "Ana Silva", "Joao Santos", "Maria Oliveira",
                "Pedro Souza", "Julia Costa", "Lucas Pereira",
                "Beatriz Lima", "Gabriel Alves", "Mariana Rocha",
                "Rafael Martins", "Camila Ferreira", "Felipe Gomes",
                "Isabela Ribeiro", "Bruno Carvalho", "Laura Mendes",
                "Thiago Nunes", "Amanda Barbosa", "Matheus Cardoso",
                "Leticia Dias", "Vinicius Araujo"
        };

        String[] estados = {"MG", "SP", "RJ", "ES", "PR", "SC", "RS"};
        char[] sexos = {'F', 'M'};
        char[] preferencias = {'F', 'M', 'I'};

        Path arquivoPath = Path.of(caminho);
        Path temporario = null;
        try {
            int totalExistente = 0;
            int maiorId = 0;
            boolean arquivoTemDados = Files.exists(arquivoPath) && Files.size(arquivoPath) > 0;

            // conta os registros reais do arquivo e encontra o maior ID
            if (arquivoTemDados) {
                try (BufferedReader leitor = Files.newBufferedReader(arquivoPath)) {
                    String primeiraLinha = leitor.readLine();
                    if (primeiraLinha == null) {
                        arquivoTemDados = false;
                    } else {
                        try {
                            Integer.parseInt(primeiraLinha.trim().split("\\s+")[0]);
                        } catch (RuntimeException e) {
                            throw new IOException("A primeira linha deve conter a quantidade de candidatos.");
                        }

                        String nomeLinha;
                        while ((nomeLinha = leitor.readLine()) != null) {
                            if (nomeLinha.trim().isEmpty() || nomeLinha.trim().startsWith("****")) continue;
                            String estado = leitor.readLine();
                            String sexo = leitor.readLine();
                            String preferencia = leitor.readLine();
                            String qtdAreasLinha = leitor.readLine();
                            if (estado == null || sexo == null || preferencia == null || qtdAreasLinha == null) {
                                throw new IOException("Arquivo incompleto: confira os dados dos candidatos.");
                            }
                            int qtdAreas;
                            try {
                                qtdAreas = Integer.parseInt(qtdAreasLinha.trim());
                            } catch (NumberFormatException e) {
                                throw new IOException("Quantidade de áreas inválida no arquivo.");
                            }
                            if (qtdAreas < 0) {
                                throw new IOException("Quantidade de áreas inválida no arquivo.");
                            }
                            for (int a = 0; a < qtdAreas; a++) {
                                if (leitor.readLine() == null) {
                                    throw new IOException("Arquivo incompleto: faltam áreas de interesse.");
                                }
                            }
                            totalExistente++;
                            String[] partesNome = nomeLinha.trim().split("\\s+");
                            try {
                                int id = Integer.parseInt(partesNome[partesNome.length - 1]);
                                if (id > maiorId) maiorId = id;
                            } catch (NumberFormatException e) {
                                // Se o nome não terminar com um ID, usa a posição do registro.
                                if (totalExistente > maiorId) maiorId = totalExistente;
                            }
                        }
                    }
                }
            }

            // usa um arquivo temporário para atualizar a primeira linha
            Path pasta = arquivoPath.toAbsolutePath().getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            temporario = Files.createTempFile(pasta, "candidatos-", ".tmp");

            try (BufferedWriter escritor = Files.newBufferedWriter(temporario)) {
                escritor.write(String.valueOf(totalExistente + quantidade));
                escritor.newLine();
                escritor.write("****");
                escritor.newLine();

                // copia os registros existentes
                if (arquivoTemDados) {
                    try (BufferedReader leitor = Files.newBufferedReader(arquivoPath)) {
                        leitor.readLine();
                        String linha;
                        while ((linha = leitor.readLine()) != null) {
                            escritor.write(linha);
                            escritor.newLine();
                        }
                    }
                }

                // acrescenta os novos candidatos com IDs após os já cadastrados
                for (int i = 1; i <= quantidade; i++) {
                    int id = maiorId + i;
                    if (i > 1 || totalExistente > 0) {
                        escritor.write("****");
                        escritor.newLine();
                    }
                    escritor.write(nomes[random.nextInt(nomes.length)] + " " + id);
                    escritor.newLine();
                    escritor.write(estados[random.nextInt(estados.length)]);
                    escritor.newLine();
                    escritor.write(String.valueOf(sexos[random.nextInt(sexos.length)]));
                    escritor.newLine();
                    escritor.write(String.valueOf(preferencias[random.nextInt(preferencias.length)]));
                    escritor.newLine();

                    int qtdAreas = 2 + random.nextInt(4);
                    boolean[] usadas = new boolean[9];
                    escritor.write(String.valueOf(qtdAreas));
                    escritor.newLine();

                    int geradas = 0;
                    while (geradas < qtdAreas) {
                        int area = 1 + random.nextInt(8);
                        if (!usadas[area]) {
                            escritor.write(String.valueOf(area));
                            escritor.newLine();
                            usadas[area] = true;
                            geradas++;
                        }
                    }
                }
            }

            try {
                Files.move(temporario, arquivoPath, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(temporario, arquivoPath, StandardCopyOption.REPLACE_EXISTING);
            }
            temporario = null;

            System.out.println(quantidade + " candidatos fictícios acrescentados ao arquivo.");
            System.out.println("Candidatos anteriores preservados: " + totalExistente);
            System.out.println("Novos IDs: " + (maiorId + 1) + " a " + (maiorId + quantidade));

        } catch (IOException e) {
            if (temporario != null) {
                try {
                    Files.deleteIfExists(temporario);
                } catch (IOException ignorada) {
                }
            }
            throw e;
        }
    }
}