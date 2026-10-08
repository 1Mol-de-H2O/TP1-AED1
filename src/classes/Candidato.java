package classes;

public class Candidato {
    private String nome;
    private char sexo;
    private String estado;
    private char sexoInteresse;
    private int grauDesejado; //grau de interesse desejado
    private int[] areasInteresse = new int[8];;

    private Candidato(String nome, char sexo, char sexoInteresse, String estado) {
        this.nome = nome;
        this.sexo = sexo;
        this.sexoInteresse = sexoInteresse;
        this.estado = estado;
    }

    public Candidato newInstance(String nome, char sexo, char sexoInteresse, String estado) {
        if(nome != null && !nome.isEmpty() && estado != null && !estado.isEmpty()) {
            return new Candidato(nome, sexo, sexoInteresse, estado);
        }
        return null;
    }

    //retorna areas de interesse
    public int[] getAreasInteresse() {
        return copia(areasInteresse);
    }
    private int[] copia(int[] v) {
        int[] copia = new int[v.length];
        for(int i = 0; i < v.length; i++){
            copia[i] = v[i];
        }
        return copia;
    }

    //inserir/atualizar grau de interesse
    public boolean inserirGrauInteresse(int grau) {
        if(grau > 0 && grau <= 15){
            grauDesejado = grau;
            return true;
        }
        return false;
    }

    //inserir area de interesse pelo código
    public boolean inserirAreaInteresse(int areaInteresse) {
        for(int i = 0; i < areasInteresse.length; i++) {
            if(areasInteresse[i] == 0 && !jaExisteAreaInteresse(areaInteresse)) {
                areasInteresse[i] = areaInteresse;
                return true;
            }
        }
        return false;
    }

    //deletar area de interesse pelo código
    public boolean deletarAreaInteresse(int areaInteresse) {
        for(int i = 0; i < areasInteresse.length; i++) {
            if(areasInteresse[i] == areaInteresse) {
                areasInteresse[i] = 0;
                return true;
            }
        }
        return false;
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
