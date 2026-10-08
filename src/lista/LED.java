package lista;

public class LED <T> {

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

    public LED() {
        head = new Celula(null);
        tail = new Celula(null);
        head.prox = tail;
    }

    public int getSize() {
        return size;
    }

    //adicionar no fim
    public void add(T item) {
        if (item != null) {
            Celula aux = tail;
            tail = new Celula(item);
            tail.prox = aux;
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
        while (atual != null) {
            sb.append(atual.item);
            sb.append(" ");
            atual = atual.prox;
        }
        sb.append("]");
        return sb.toString();
    }
}
