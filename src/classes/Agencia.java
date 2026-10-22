package classes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lista.ListaIdentificadores;

public class Agencia {

    private static final String[] NOMES_AREAS = {
            "Esportes", "Artes", "Música", "Cinema", "Tecnologia",
            "Animais", "Gastronomia", "Ciências"
    };

    private ListaIdentificadores<Candidato> candidatos = new ListaIdentificadores<Candidato>();

    public int getTotal() {
        return candidatos.getSize();
    }

    // Leitura do arquivo
    public void lerArquivo(String caminho) throws IOException {
        ListaIdentificadores<Candidato> lidos = new ListaIdentificadores<Candidato>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminho), StandardCharsets.UTF_8))) {
            String primeira = proximaLinha(br);
            if (primeira == null)
                throw new IOException("Arquivo vazio");
            int n;
            try {
                n = Integer.parseInt(primeira.split("\\s+")[0]);
            } catch (NumberFormatException e) {
                throw new IOException("Primeira linha inválida: " + primeira);
            }

            Candidato.reiniciarContador();
            for (int i = 1; i <= n; i++) {
                lidos.add(lerCandidato(br, i));
            }
        }
        candidatos = lidos;
        calculaCompanheiros();
    }

    private Candidato lerCandidato(BufferedReader br, int numero) throws IOException {
        try {
            String nome = exige(proximaLinha(br), numero);
            String estado = exige(proximaLinha(br), numero);
            String sexo = exige(proximaLinha(br), numero);
            String sexoInteresse = exige(proximaLinha(br), numero);
            int qtdAreas = Integer.parseInt(exige(proximaLinha(br), numero));
            Candidato c = Candidato.newInstance(nome, sexo.toUpperCase().charAt(0),
                    sexoInteresse.toUpperCase().charAt(0), estado);

            for (int i = 0; i < qtdAreas; i++) {
                c.inserirAreaInteresse(Integer.parseInt(exige(proximaLinha(br), numero)));
            }
            return c;
        } catch (IllegalArgumentException e) {
            throw new IOException("Candidato " + numero + ": " + e.getMessage());
        }
    }

    private String proximaLinha(BufferedReader br) throws IOException {
        String linha;
        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (!linha.isEmpty() && !linha.startsWith("****"))
                return linha;
        }
        return null;
    }

    private String exige(String linha, int numero) throws IOException {
        if (linha == null)
            throw new IOException("Fim inesperado do arquivo no candidato " + numero + ".");
        return linha;
    }

    public Candidato buscaPorId(int id) {
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    private void calculaCompanheiros() {
        ListaIdentificadores<Candidato>.Iterador iteradorReinicio = candidatos.iterador();
        while (iteradorReinicio.temProximo())
            iteradorReinicio.proximo().reiniciarCompanheiros();

        ListaIdentificadores<Candidato>.Iterador iteradorU = candidatos.iterador();
        while (iteradorU.temProximo()) {
            Candidato u = iteradorU.proximo();
            ListaIdentificadores<Candidato>.Iterador iteradorV = candidatos.iterador();
            while (iteradorV.temProximo()) {
                Candidato v = iteradorV.proximo();
                if (u.verificaPotencialCompanheiro(v))
                    u.inserePotencialCompanheiro(v);
            }
        }
    }

    private String cabecalho(String titulo) {
        return "\n\t" + titulo + "\n";
    }

    private int digitos(int numero) {
        return String.valueOf(numero).length();
    }

  
    private String plural(int quantidade, String singular, String plural) {
        return quantidade + " " + (quantidade == 1 ? singular : plural);
    }

    // só aceita estados do sul e sudeste
    private String siglasAceitas() {
        StringBuilder siglas = new StringBuilder();
        for (int i = 0; i < Estados.quantidade(); i++) {
            siglas.append(Estados.get(i));
            if (i < Estados.quantidade() - 1)
                siglas.append(", ");
        }
        return siglas.toString();
    }

    private String tabela(ListaIdentificadores<Candidato> lista) {
        int larguraId = 2;
        int larguraNome = 4;

        ListaIdentificadores<Candidato>.Iterador iteradorMedida = lista.iterador();
        while (iteradorMedida.temProximo()) {
            Candidato c = iteradorMedida.proximo();
            larguraId = Math.max(larguraId, digitos(c.getId()));
            larguraNome = Math.max(larguraNome, c.getNome().length());
        }

        String formato = "  %" + larguraId + "s  %-" + larguraNome + "s  %s\n";
        StringBuilder texto = new StringBuilder();
        texto.append(String.format(formato, "ID", "Nome", "UF"));
        texto.append(String.format(formato, "-".repeat(larguraId), "-".repeat(larguraNome), "--"));

        ListaIdentificadores<Candidato>.Iterador iteradorLinhas = lista.iterador();
        while (iteradorLinhas.temProximo()) {
            Candidato c = iteradorLinhas.proximo();
            texto.append(String.format(formato, c.getId(), c.getNome(), c.getEstado()));
        }
        return texto.toString();
    }

    // inserir/atualizar o grau de interesse desejado
    public String defineGrau(int id, int grau) {
        Candidato u = buscaPorId(id);
        if (u == null)
            return "Erro: candidato com ID " + id + " não encontrado.\n";
        if (!u.inserirGrauInteresse(grau))
            return "Erro: grau inválido (use de 1 a 15).\n";
        calculaCompanheiros();
        return "Grau de " + u.getNome() + " (ID " + u.getId() + ") atualizado para " + grau + ".\n"
                + "Potenciais companheiros recalculados.\n";
    }

   // número e nomes dos potenciais companheiros
    public String companheirosDe(int id) {
        Candidato u = buscaPorId(id);
        if (u == null)
            return "Erro: candidato com ID " + id + " não encontrado.\n";

        ListaIdentificadores<Candidato> companheiros = new ListaIdentificadores<Candidato>();
        ListaIdentificadores<Integer>.Iterador iteradorIds = u.listaCompanheiros().iterador();
        while (iteradorIds.temProximo()) {
            Candidato v = buscaPorId(iteradorIds.proximo());
            if (v != null)
                companheiros.add(v);
        }

        StringBuilder texto = new StringBuilder();
        texto.append(cabecalho("Potenciais companheiros"));
        texto.append("Candidato:     ").append(u.getId()).append(" - ").append(u.getNome())
                .append(" (").append(u.getEstado()).append(")\n");
        texto.append("Grau desejado: ").append(u.getGrauDesejado()).append("\n");

        if (companheiros.getSize() == 0) {
            texto.append("Nenhum potencial companheiro encontrado.\n");
            return texto.toString();
        }

        texto.append("Encontrados:   ")
                .append(plural(companheiros.getSize(), "potencial companheiro", "potenciais companheiros"))
                .append("\n\n");
        texto.append(tabela(companheiros));
        return texto.toString();
    }

    // K candidatos com mais potenciais companheiros
    public String topK(int k) {
        if (k <= 0)
            return "Erro: K deve ser positivo.\n";

        ListaIdentificadores<Candidato> ranking = new ListaIdentificadores<Candidato>();
        ListaIdentificadores<Integer> escolhidos = new ListaIdentificadores<Integer>();

        for (int posicao = 1; posicao <= k; posicao++) {
            Candidato melhor = null;
            ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
            while (iteradorCandidatos.temProximo()) {
                Candidato c = iteradorCandidatos.proximo();
                if (escolhidos.contem(c.getId()))
                    continue;
                if (melhor == null || c.numeroCompanheiros() > melhor.numeroCompanheiros())
                    melhor = c;
            }
            if (melhor == null)
                break;
            escolhidos.add(melhor.getId());
            ranking.add(melhor);
        }

        if (ranking.getSize() == 0)
            return "Nenhum candidato cadastrado.\n";

        int larguraPosicao = Math.max(1, digitos(ranking.getSize()));
        int larguraId = 2;
        int larguraNome = 4;
        ListaIdentificadores<Candidato>.Iterador iteradorMedida = ranking.iterador();
        while (iteradorMedida.temProximo()) {
            Candidato c = iteradorMedida.proximo();
            larguraId = Math.max(larguraId, digitos(c.getId()));
            larguraNome = Math.max(larguraNome, c.getNome().length());
        }

        String formato = "  %" + larguraPosicao + "s  %" + larguraId + "s  %-" + larguraNome
                + "s  %12s\n";
        StringBuilder texto = new StringBuilder();
        texto.append(cabecalho("Top " + ranking.getSize() + " - mais potenciais companheiros"));
        texto.append(String.format(formato, "#", "ID", "Nome", "Companheiros"));
        texto.append(String.format(formato, "-".repeat(larguraPosicao), "-".repeat(larguraId),
                "-".repeat(larguraNome), "-".repeat(12)));

        int posicao = 1;
        ListaIdentificadores<Candidato>.Iterador iteradorLinhas = ranking.iterador();
        while (iteradorLinhas.temProximo()) {
            Candidato c = iteradorLinhas.proximo();
            texto.append(String.format(formato, posicao, c.getId(), c.getNome(),
                    c.numeroCompanheiros()));
            posicao++;
        }

        if (ranking.getSize() < k) {
            texto.append("\nAviso: foram pedidos ").append(k).append(", mas há apenas ")
                    .append(plural(candidatos.getSize(), "candidato cadastrado", "candidatos cadastrados"))
                    .append(".\n");
        }
        return texto.toString();
    }

    // candidatos sem nenhum companheiro
    public String semCompanheiros() {
        ListaIdentificadores<Candidato> encontrados = new ListaIdentificadores<Candidato>();
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.numeroCompanheiros() == 0)
                encontrados.add(c);
        }

        StringBuilder texto = new StringBuilder();
        texto.append(cabecalho("Candidatos sem potenciais companheiros"));
        if (encontrados.getSize() == 0) {
            texto.append("Todos os candidatos têm ao menos um potencial companheiro.\n");
            return texto.toString();
        }
        texto.append(tabela(encontrados));
        texto.append("\nTotal: ").append(plural(encontrados.getSize(), "candidato", "candidatos"))
                .append("\n");
        return texto.toString();
    }

    // candidatos com interesse em uma área
    public String candidatosPorArea(int area) {
        if (area < 1 || area > 8)
            return "Erro: área inválida (use de 1 a 8).\n";

        ListaIdentificadores<Candidato> encontrados = new ListaIdentificadores<Candidato>();
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.temInteresse(area))
                encontrados.add(c);
        }

        StringBuilder texto = new StringBuilder();
        texto.append(cabecalho("Interesse em " + NOMES_AREAS[area - 1] + " (área " + area + ")"));
        if (encontrados.getSize() == 0) {
            texto.append("Nenhum candidato com interesse nessa área.\n");
            return texto.toString();
        }
        texto.append(tabela(encontrados));
        texto.append("\nTotal: ").append(plural(encontrados.getSize(), "candidato", "candidatos"))
                .append("\n");
        return texto.toString();
    }

    // candidatos que residem em um estado
    public String candidatosPorEstado(String sigla) {
        if (!Estados.valido(sigla))
            return "Erro: estado inválido. Estados aceitos: " + siglasAceitas() + ".\n";

        ListaIdentificadores<Candidato> encontrados = new ListaIdentificadores<Candidato>();
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.reside(sigla))
                encontrados.add(c);
        }

        StringBuilder texto = new StringBuilder();
        texto.append(cabecalho("Residentes em " + sigla.toUpperCase()));
        if (encontrados.getSize() == 0) {
            texto.append("Nenhum candidato reside nesse estado.\n");
            return texto.toString();
        }
        texto.append(tabela(encontrados));
        texto.append("\nTotal: ").append(plural(encontrados.getSize(), "candidato", "candidatos"))
                .append("\n");
        return texto.toString();
    }
}