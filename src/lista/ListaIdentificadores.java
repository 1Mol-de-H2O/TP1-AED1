package lista;

public class ListaIdentificadores<T> {

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

	public ListaIdentificadores() {
		head = new Celula(null);
		tail = new Celula(null);
		head.prox = tail;
		tail.prox = null;
	}

	public int getSize() {
		return size;
	}

	public T getIndex(int i) {
		if (i < 0 || i >= size)
			throw new IndexOutOfBoundsException("Valor não aceitável, olhe o escopo" + i);
		Celula atual = head.prox;
		for (int j = 0; j < i; j++) {
			atual = atual.prox;
		}
		return atual.item;
	}

	// adiciona no inicio
	public void addIni(T item) {
		if (item != null) {
			Celula nova = new Celula(item);
			nova.prox = head.prox;
			head.prox = nova;
			size++;
		} else {
			throw new IllegalArgumentException("Item inválido");
		}
	}

	// adicionar no fim
	public void add(T item) {
		if (item != null) {
			tail.item = item;
			tail.prox = new Celula(null);
			tail = tail.prox;
			size++;
		} else {
			throw new IllegalArgumentException("Item inválido");
		}
	}

	// add em qualquer posição
	public void add(int i, T item) {
		if (i >= 0 && i <= size && item != null) {
				Celula atual = head.prox, ant = head;
				int cont = 0;

				while (cont < i && atual != tail) {
					ant = atual;
					atual = atual.prox;
					cont++;
				}
				ant.prox = new Celula(item);
				ant.prox.prox = atual;
				size++;
		} else {
			throw new IllegalArgumentException("Item ou posição errada");
		}
	}
	
	public boolean contem(T item) {
		Celula atual = head.prox;
		while (atual != tail) {
			if (atual.item.equals(item)) {
				return true;
			}
			atual = atual.prox;
		}
		return false;
	}

	public T remove(T item) {
		Celula atual = head.prox, ant = head;

		while (atual != tail && !atual.item.equals(item)) {
			ant = atual;
			atual = atual.prox;
		}

		if (atual == tail) {
			return null;
		} else {
			ant.prox = atual.prox;
			size--;
			return atual.item;
		}
	}
	
	public ListaIdentificadores<T> copia(){
		ListaIdentificadores<T> nova = new ListaIdentificadores<T>();
		Celula atual = head.prox;
		while (atual != tail) {
			nova.add(atual.item);
			atual = atual.prox;
		}
		return nova;
	}

	@Override
	// lista os candidatos
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
