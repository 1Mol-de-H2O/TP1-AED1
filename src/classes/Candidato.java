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

    private static int[] PESOS = { 0, 1, 2, 1, 1, 3, 2, 2, 3 };

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
        return areasInteresse.copia();
    }

    public ListaIdentificadores<Integer> getCompanheiros() {
        return companheiros.copia();
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
    public boolean verificaPotencialCompanheiro(Candidato v) {
        if (this.id == v.id)
            return false;
        if (!this.estado.equals(v.estado))
            return false;
        if (!this.aceita(v) || !v.aceita(this))
            return false;
        int grau = grauEmComum(v);
        if (grau >= this.grauDesejado && grau >= v.grauDesejado)
            return true;
        return false;
    }

    public void inserePotencialCompanheiro(Candidato v) {
        this.companheiros.add(v.getId());
    }

    public int numeroCompanheiros() {
        return companheiros.getSize();
    }

    public ListaIdentificadores<Integer> listaCompanheiros() {
        return companheiros.copia(); // preciso de aplicar metódo de copia
    }

    public boolean temInteresse(int area) {
        return areasInteresse.contem(area);

    }

    public boolean reside(String sigla) {
        return estado.equalsIgnoreCase(sigla);
    }

    // ---

    private boolean aceita(Candidato v) {
        if (this.sexoInteresse == 'I' || this.sexoInteresse == v.sexo)
            return true;
        return false;
    }

    private int grauEmComum(Candidato v) {
        int grau = 0;
        for (int area = 1; area <= 8; area++) {
            if (this.areasInteresse.contem(area) && v.areasInteresse.contem(area))
                grau += PESOS[area];
        }
        return grau;
    }

    public void reiniciarCompanheiros() {
        this.companheiros = new ListaIdentificadores<Integer>();
    }
}
