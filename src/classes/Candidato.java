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
        this.estado = estado;
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

    //retorna areas de interesse
    public ListaIdentificadores<Integer> getAreasInteresse() {
        return copia(areasInteresse);
    }
    
    private ListaIdentificadores<Integer> copia(ListaIdentificadores<Integer> v) {
    	ListaIdentificadores<Integer> copia = new ListaIdentificadores<Integer>();
        for(int i = 0; i < v.getSize(); i++){
            copia[i] = v[i];
        }
        return copia;
    }

    //inserir grau de interesse
    public boolean inserirGrauInteresse(int grau) {
        if(grau > 0 && grau <= 15){
            grauDesejado = grau;
            return true;
        }
        return false;
    }

    //inserir area de interesse pelo código
    public void inserirAreaInteresse(int areaInteresse) {
        for(int i = 0; i < areasInteresse.length; i++) {
            if(areasInteresse[i] == 0) {
                if(!jaExisteAreaInteresse(areaInteresse)){
                    areasInteresse[i] = areaInteresse;
                    return;
                } else{
                    throw new IllegalArgumentException("Item já existe");
                }
            } else{
                throw new IndexOutOfBoundsException("Vetor cheio");
            }
        }
    }

    //deletar area de interesse pelo código
    public void deletarAreaInteresse(int areaInteresse) {
        for(int i = 0; i < areasInteresse.length; i++) {
            if(areasInteresse[i] == areaInteresse) {
                areasInteresse[i] = 0;
                return;
            }
        }
        throw new IllegalArgumentException("Item não existente");
    }

    //verificar se area de interesse já está cadastrada
    public boolean jaExisteAreaInteresse(int areaInteresse) {
        for(int i = 0; i < areasInteresse.length; i++) {
            if(areasInteresse[i] == areaInteresse) {
                return true;
            }
        }
        return false;
    }
}
