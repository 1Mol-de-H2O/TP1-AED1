import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Scanner;

import classes.Agencia;
import classes.Candidato;
import classes.Estados;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
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
                    String sigla = sc.nextLine().trim();
                    System.out.print(agencia.candidatosPorEstado(sigla));
                    break;
                }
                case 7: {
                    System.out.print("Nome: ");
                    String nome = sc.nextLine().trim();
                    System.out.print("Estado (sigla): ");
                    String estado = sc.nextLine().trim();
                    System.out.print("Sexo (F/M): ");
                    String sexo = sc.nextLine().trim();
                    System.out.print("Sexo do potencial companheiro (F/M/I): ");
                    String sexoInteresse = sc.nextLine().trim();
                    System.out.print("Áreas de interesse (códigos de 1 a 8 separados por espaço, "
                            + "ex.: 1 5 7; deixe vazio para nenhuma): ");
                    String areasTexto = sc.nextLine().trim();
                    cadastrarCandidato(nome, estado, sexo, sexoInteresse, areasTexto);
                    break;
                }
                case 8: {
                    int quantidade = lerInteiro("Quantidade de candidatos fictícios a gerar: ");
                    gerarBaseFicticia(quantidade);
                    break;
                }
                case 0:
                    System.out.println("Sistema encerrado");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha uma opção de 0 a 8.");
            }
            System.out.println();
        } while (opcao != 0);

        sc.close();
    }

    private static void exibirMenu() {
        System.out.println("\t\tAGÊNCIA DE RELACIONAMENTO");
        System.out.println("1 - Inserir/atualizar grau de interesse desejado");
        System.out.println("2 - Listar potenciais companheiros de um candidato");
        System.out.println("3 - Mostrar os K candidatos com mais potenciais companheiros");
        System.out.println("4 - Listar candidatos sem potenciais companheiros");
        System.out.println("5 - Listar candidatos por área de interesse");
        System.out.println("6 - Listar candidatos por estado");
        System.out.println("7 - Cadastrar um candidato (grava no arquivo)");
        System.out.println("8 - Gerar candidatos (grava no arquivo)");
        System.out.println("0 - Sair");
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    private static void cadastrarCandidato(String nome, String estado, String sexo, String sexoInteresse, String areasTexto) {
    	
        if (nome.startsWith("****")) {
            System.out.println("Erro: o nome não pode começar com ****.");
            return;
        }
        if (sexo.length() != 1) {
            System.out.println("Erro: o sexo deve ser uma letra (F ou M).");
            return;
        }
        if (sexoInteresse.length() != 1) {
            System.out.println("Erro: o sexo do potencial companheiro deve ser uma letra (F, M ou I).");
            return;
        }

        String[] partes = areasTexto.isEmpty() ? new String[0] : areasTexto.split("[\\s,;]+");
        int[] areas = new int[partes.length];

        try {
            Candidato teste = Candidato.newInstance(nome, sexo.toUpperCase().charAt(0),
                    sexoInteresse.toUpperCase().charAt(0), estado);
            for (int i = 0; i < partes.length; i++) {
                try {
                    areas[i] = Integer.parseInt(partes[i]);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: área inválida '" + partes[i] + "'.");
                    return;
                }
                teste.inserirAreaInteresse(areas[i]);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage() + ".");
            return;
        }

        String[] blocos = {
                montarBloco(nome, estado.toUpperCase(), sexo.toUpperCase(),
                        sexoInteresse.toUpperCase(), areas)
        };

        int totalAnterior;
        try {
            totalAnterior = acrescentarAoArquivo(arquivo, blocos);
        } catch (IOException e) {
            System.out.println("Erro ao gravar o candidato: " + e.getMessage());
            return;
        }

        System.out.println("Candidato cadastrado com o ID " + (totalAnterior + 1) + ".");
        recarregarBase();
    }

    private static void gerarBaseFicticia(int quantidade) {
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser positiva.");
            return;
        }

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
        String[] sexos = {"F", "M"};
        String[] preferencias = {"F", "M", "I"};

        String[] blocos = new String[quantidade];
        for (int i = 0; i < quantidade; i++) {
            String nome = nomes[random.nextInt(nomes.length)];
            String estado = Estados.get(random.nextInt(Estados.quantidade()));
            String sexo = sexos[random.nextInt(sexos.length)];
            String preferencia = preferencias[random.nextInt(preferencias.length)];

            int qtdAreas = 2 + random.nextInt(4);
            int[] areas = new int[qtdAreas];
            boolean[] usadas = new boolean[9];
            int geradas = 0;
            while (geradas < qtdAreas) {
                int area = 1 + random.nextInt(8);
                if (!usadas[area]) {
                    usadas[area] = true;
                    areas[geradas] = area;
                    geradas++;
                }
            }

            blocos[i] = montarBloco(nome, estado, sexo, preferencia, areas);
        }

        int totalAnterior;
        try {
            totalAnterior = acrescentarAoArquivo(arquivo, blocos);
        } catch (IOException e) {
            System.out.println("Erro ao gerar candidatos: " + e.getMessage());
            return;
        }

        System.out.println(quantidade + " candidatos fictícios acrescentados ao arquivo.");
        System.out.println("Candidatos anteriores preservados: " + totalAnterior);
        System.out.println("Novos IDs: " + (totalAnterior + 1) + " a " + (totalAnterior + quantidade));
        recarregarBase();
    }

    private static String montarBloco(String nome, String estado, String sexo,
                                      String sexoInteresse, int[] areas) {
        String quebra = System.lineSeparator();
        StringBuilder bloco = new StringBuilder();
        bloco.append(nome).append(quebra);
        bloco.append(estado).append(quebra);
        bloco.append(sexo).append(quebra);
        bloco.append(sexoInteresse).append(quebra);
        bloco.append(areas.length).append(quebra);
        for (int i = 0; i < areas.length; i++) {
            bloco.append(areas[i]).append(quebra);
        }
        return bloco.toString();
    }

    private static void recarregarBase() {
        try {
            agencia.lerArquivo(arquivo);
            System.out.println("Base carregada. Total de candidatos: " + agencia.getTotal());
        } catch (IOException e) {
            System.out.println("O arquivo foi atualizado, mas não foi possível carregá-lo: "
                    + e.getMessage());
        }
    }

    private static int contarRegistros(Path arquivoPath) throws IOException {
        if (!Files.exists(arquivoPath) || Files.size(arquivoPath) == 0) {
            return 0;
        }

        int total = 0;
        try (BufferedReader leitor = Files.newBufferedReader(arquivoPath)) {
            String primeiraLinha = leitor.readLine();
            if (primeiraLinha == null) {
                return 0;
            }
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
                total++;
            }
        }
        return total;
    }

    private static int acrescentarAoArquivo(String caminho, String[] blocos) throws IOException {
        Path arquivoPath = Path.of(caminho);
        Path temporario = null;
        try {
            int totalExistente = contarRegistros(arquivoPath);

            Path pasta = arquivoPath.toAbsolutePath().getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            temporario = Files.createTempFile(pasta, "candidatos-", ".tmp");

            try (BufferedWriter escritor = Files.newBufferedWriter(temporario)) {
                escritor.write(String.valueOf(totalExistente + blocos.length));
                escritor.newLine();

                boolean ultimaFoiSeparador = false;

                if (totalExistente > 0) {
                    try (BufferedReader leitor = Files.newBufferedReader(arquivoPath)) {
                        leitor.readLine();
                        String linha;
                        while ((linha = leitor.readLine()) != null) {
                            String limpa = linha.trim();
                            if (limpa.isEmpty()) {
                                continue;
                            }
                            if (limpa.startsWith("****")) {
                                if (ultimaFoiSeparador) {
                                    continue;
                                }
                                ultimaFoiSeparador = true;
                            } else {
                                ultimaFoiSeparador = false;
                            }
                            escritor.write(linha);
                            escritor.newLine();
                        }
                    }
                }

                for (int i = 0; i < blocos.length; i++) {
                    if (!ultimaFoiSeparador) {
                        escritor.write("****");
                        escritor.newLine();
                    }
                    escritor.write(blocos[i]);
                    ultimaFoiSeparador = false;
                }
            }

            try {
                Files.move(temporario, arquivoPath, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(temporario, arquivoPath, StandardCopyOption.REPLACE_EXISTING);
            }
            temporario = null;
            return totalExistente;

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