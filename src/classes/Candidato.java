package classes;

import lista.ListaIdentificadores;

public class Candidato {
    private static int contId = 1;

    private int id;
    private String nome;
    private String estado;
    private char sexo;
    private char sexoInteresse;
    private int grauDesejado;

    private ListaIdentificadores<Integer> areasInteresse;
    private ListaIdentificadores<Integer> companheiros;

    private Candidato(String nome, char sexo, char sexoInteresse, String estado) {
        this.id = contId++;
        this.nome = nome;
        this.sexo = sexo;
        this.sexoInteresse = sexoInteresse;
        this.estado = estado.toUpperCase();
        this.grauDesejado = 1;
        this.areasInteresse = new ListaIdentificadores<Integer>();
        this.companheiros = new ListaIdentificadores<Integer>();
    }

    public static Candidato newInstance(String nome, char sexo, char sexoInteresse, String estado) {
        if (nome == null || nome.isEmpty())
            throw new IllegalArgumentException("Nome inválido");
        if (estado == null || !Estados.valido(estado))
            throw new IllegalArgumentException("Estado inválido");
        if (sexo != 'F' && sexo != 'M')
            throw new IllegalArgumentException("Sexo inválido");
        if (sexoInteresse != 'F' && sexoInteresse != 'M' && sexoInteresse != 'I')
            throw new IllegalArgumentException("Sexo de interesse inválido");
        return new Candidato(nome, sexo, sexoInteresse, estado);
    }

    // getters
    public static int getContId() {
        return contId;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }

    public char getSexo() {
        return sexo;
    }

    public char getSexoInteresse() {
        return sexoInteresse;
    }

    public int getGrauDesejado() {
        return grauDesejado;
    }

    public ListaIdentificadores<Integer> getAreasInteresse() {
        return areasInteresse;
    }

    public ListaIdentificadores<Integer> getCompanheiros() {
        return companheiros;
    }

    // inserir grau de interesse
    public boolean inserirGrauInteresse(int grau) {
        if (grau > 0 && grau <= 15) {
            grauDesejado = grau;
            return true;
        }
        return false;
    }

    // inserir area de interesse pelo código
    public void inserirAreaInteresse(int areaInteresse) {
        if (areaInteresse < 1 || areaInteresse > 8)
            throw new IllegalArgumentException("Área inválida");
        if (areasInteresse.contem(areaInteresse))
            throw new IllegalArgumentException("Área já cadastrada");
        areasInteresse.add(areaInteresse);
    }

    // deletar area de interesse pelo código
    public void deletarAreaInteresse(int areaInteresse) {
        if (areasInteresse.remove(areaInteresse) == null) {
            throw new IllegalArgumentException("Área não encontrada");
        }
    }

    // verificar se area de interesse já está cadastrada
    public boolean jaExisteAreaInteresse(int areaInteresse) {
        return areasInteresse.contem(areaInteresse);
    }

    // metódos pedidos ---
    public void verificaPotencialCompanheiro(Candidato u) {

    }

    public void inserePotencialCompanheiro(Candidato v) {
        this.companheiros.add(v.getId());
    }

    public int numeroCompanheiros(Candidato u) {
        return u.companheiros.getSize();
    }

    public ListaIdentificadores<Integer> listaCompanheiros() {
        return this.companheiros; // preciso de aplicar metódo de copia
    }

    public boolean temInteresse(Candidato u, int id) {
        if (u.areasInteresse.contem(id))
            return true;
        else
            return false;
    }

    public boolean reside(Candidato u, String sigla) {
        if (u.estado.equalsIgnoreCase(sigla))
            return true;
        else
            return false;
    }

    // ---

    public void reiniciarCompanheiros() {
        this.companheiros = new ListaIdentificadores<Integer>();
    }
}
