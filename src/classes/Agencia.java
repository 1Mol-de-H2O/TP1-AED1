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

    private String descricao(Candidato c) {
        return c.getId() + " - " + c.getNome();
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

    // inserir/atualizar o grau de interesse desejado
    public String defineGrau(int id, int grau) {
        Candidato u = buscaPorId(id);
        if (u == null)
            return "Candidato inexistente.\n";
        if (!u.inserirGrauInteresse(grau))
            return "Grau inválido (use de 1 a 15).\n";
        calculaCompanheiros();
        return "Grau de " + u.getNome() + " atualizado para " + grau
                + ". Potenciais companheiros recalculados.\n";
    }

    // número e nomes dos potenciais companheiros
    public String companheirosDe(int id) {
        Candidato u = buscaPorId(id);
        if (u == null)
            return "Candidato inexistente.\n";

        StringBuilder texto = new StringBuilder();
        texto.append("Candidato: ").append(descricao(u))
                .append(" (grau desejado: ").append(u.getGrauDesejado()).append(")\n");
        texto.append("Número de potenciais companheiros: ").append(u.numeroCompanheiros()).append("\n");

        ListaIdentificadores<Integer>.Iterador iteradorCompanheiros = u.listaCompanheiros().iterador();
        while (iteradorCompanheiros.temProximo()) {
            Candidato v = buscaPorId(iteradorCompanheiros.proximo());
            if (v != null)
                texto.append("  ").append(v.getNome()).append("\n");
        }
        return texto.toString();
    }

    // K candidatos com mais potenciais companheiros
    public String topK(int k) {
        if (k <= 0)
            return "K deve ser positivo.\n";

        StringBuilder texto = new StringBuilder();
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
            texto.append(posicao).append(". ").append(descricao(melhor)).append(" - ")
                    .append(melhor.numeroCompanheiros()).append(" potenciais companheiros\n");
        }
        return texto.toString();
    }

    // candidatos sem nenhum companheiro
    public String semCompanheiros() {
        StringBuilder texto = new StringBuilder();
        int total = 0;
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.numeroCompanheiros() == 0) {
                texto.append("  ").append(descricao(c)).append("\n");
                total++;
            }
        }
        texto.append("Total sem companheiros: ").append(total).append("\n");
        return texto.toString();
    }


    // candidatos com interesse em uma área
    public String candidatosPorArea(int area) {
        if (area < 1 || area > 8)
            return "Área inválida (use de 1 a 8).\n";

        StringBuilder texto = new StringBuilder();
        int total = 0;
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.temInteresse(area)) {
                texto.append("  ").append(descricao(c)).append("\n");
                total++;
            }
        }
        texto.append("Total com interesse em ").append(NOMES_AREAS[area - 1])
                .append(": ").append(total).append("\n");
        return texto.toString();
    }

    // candidatos que residem em um estado
    public String candidatosPorEstado(String sigla) {
        if (!Estados.valido(sigla))
            return "Estado inválido (aceitos: PR, SC, RS, MG, ES, RJ, SP).\n";

        StringBuilder texto = new StringBuilder();
        int total = 0;
        ListaIdentificadores<Candidato>.Iterador iteradorCandidatos = candidatos.iterador();
        while (iteradorCandidatos.temProximo()) {
            Candidato c = iteradorCandidatos.proximo();
            if (c.reside(sigla)) {
                texto.append("  ").append(descricao(c)).append("\n");
                total++;
            }
        }
        texto.append("Total residentes em ").append(sigla.toUpperCase())
                .append(": ").append(total).append("\n");
        return texto.toString();
    }
}