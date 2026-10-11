package lista;

public class ListaIdentificadores <T> {

    private Celula head;
    private Celula tail;
    private int size;

    class Celula {
        T item;
        Celula prox;

        private Celula(T item) {
            this.item = item;
        }
    }

    public ListaIdentificadores(){
        head = new Celula(null);
        tail = new Celula(null);
        head.prox = tail;
        tail.prox = null;
    }

    public int getSize() {
        return size;
    }

    //adicionar no fim
    public void add(T item) {
        if (item != null) {
            tail.item = item;
            tail.prox = new Celula(null);
            tail = tail.prox;
            size++;
        }  else {
            throw new IllegalArgumentException("Item inválido");
        }
    }

    @Override
    //lista os candidatos
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        Celula atual = head.prox;
        while (atual != tail) {
            sb.append(atual.item);
            sb.append(" ");
            atual = atual.prox;
        }
        sb.append("]");
        return sb.toString();
    }
}
